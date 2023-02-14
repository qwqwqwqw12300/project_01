package com.newlandnpt.varyar.api.controller.business.rocketmq;

import com.alibaba.fastjson2.JSONObject;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.system.service.DeviceEventService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 离开房间事件 延迟消息监听器
 *
 * @author lisd
 * @date 2023/1/13
 **/
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic.accessDelay}", consumerGroup = "${rocketmq.group.accessDelay}")
public class AccessDelayListener implements RocketMQListener<String> {

    private static final Logger log = LoggerFactory.getLogger(AccessDelayListener.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private DeviceEventService deviceEventService;

    private static final String ACCESS_DELAY_TOPIC = "access_delay_";

    @Override
    public void onMessage(String s) {
        log.debug("----" + System.currentTimeMillis() + "----" + " 监听到进/出房间/区域超时事件消息： " + s);

        //定义Redis中 进出事件（区域监控）相关的数据结构与key
        String key =s;
        JSONObject jsonObject = redisCache.getCacheObject(key);

        //判断事件
        if(jsonObject!=null){
            log.debug("超时事件触发");
            String deviceNo = jsonObject.getString("deviceNo");
            String type = jsonObject.getString("type");
            String areaName = jsonObject.getString("areaName");
            int delayTime = jsonObject.getIntValue("delayTime");
            deviceEventService.deviceAccessIssue(deviceNo,areaName , type, delayTime);
            log.info("设备[" + deviceNo + "]进出房间事件结束");
        }else{
            log.debug("不触发");
        }

        log.debug("离开房间超时事件结束");
    }
}
