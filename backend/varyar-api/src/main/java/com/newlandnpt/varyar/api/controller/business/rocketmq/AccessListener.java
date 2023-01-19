package com.newlandnpt.varyar.api.controller.business.rocketmq;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 离开房间事件消息监听器
 *
 * @author lisd
 * @date 2023/1/4
 **/
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic.access}", consumerGroup = "${rocketmq.group.access}")
public class AccessListener implements RocketMQListener<String> {

    private static final Logger log = LoggerFactory.getLogger(AccessListener.class);

    @Value("${rocketmq.topic.accessDelay}")
    private String accessDelayTopic;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 事件类型 离开房间
     */
    private static final String LEAVE_ROOM = "leave_room";

    /**
     * 事件类型 进入房间
     */
    private static final String ENTER_ROOM = "enter_room";

    private static final String ACCESS_DELAY_TOPIC = "access_delay_";

    @Override
    public void onMessage(String s) {
        log.debug("----" + System.currentTimeMillis() + "----" + " 监听到离开房间事件消息： " + s);
        JSONObject jsonObject = JSON.parseObject(s);
        //消息s格式
        String deviceNo = jsonObject.getString("devId");
        String type = jsonObject.getString("type");
        String timeStr = jsonObject.getString("time");
        int delayTime = jsonObject.getIntValue("delayTime");
        //离开房间事件 Redis缓存key
        String redisKey = CacheConstants.T_DEVICE_KEY + ACCESS_DELAY_TOPIC + deviceNo;

        //若解析s 判断为离开房间事件 则根据deviceNo发送延迟消息
        if (LEAVE_ROOM.equals(type)) {
            SendResult result;
            JSONObject msgObject = new JSONObject();
            msgObject.put("deviceNo", deviceNo);
            msgObject.put("time", timeStr);
            MqDelayTime mqDelayTime = new MqDelayTime();
            int delayLevel = mqDelayTime.getDelayLevel(delayTime);
            log.info("为设备[" + deviceNo + "]发送进出房间延迟消息");
            //发送延迟消息
            List<Long> timestampList = new ArrayList<>();
            if (redisCache.hasKey(redisKey)) {
                timestampList = redisCache.getCacheList(redisKey);
                redisCache.deleteObject(redisKey);
            }
            timestampList.add(System.currentTimeMillis());
            redisCache.setCacheList(redisKey, timestampList);
            result = rocketMQTemplate.syncSend(accessDelayTopic, MessageBuilder.withPayload(msgObject).build(), 3000, delayLevel);
            log.info(" 发送消息结果： " + result);
        }
        //若解析s 判断为进入房间事件 则根据deviceNo更新Redis
        if (ENTER_ROOM.equals(type)) {
            List<Long> valueList = redisCache.getCacheList(redisKey);
            valueList.remove(valueList.size() - 1);
            //更新redis
            redisCache.deleteObject(redisKey);
            redisCache.setCacheList(redisKey, valueList);
            log.info("更新设备[" + deviceNo + "]对应房间内状态的Redis数据: [" + valueList + "]");
        }
    }
}
