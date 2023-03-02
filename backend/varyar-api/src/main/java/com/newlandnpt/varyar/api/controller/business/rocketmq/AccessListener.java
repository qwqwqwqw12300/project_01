package com.newlandnpt.varyar.api.controller.business.rocketmq;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
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

import static com.newlandnpt.varyar.common.constant.CacheConstants.T_DEVICE_VAYYAR_ACCESS_KEY;

/**
 * 房间进出计算事件消息 监听器
 *
 * @author lisd
 * @date 2023/1/4
 **/
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic.access}", consumerGroup = "${rocketmq.group.access}")
public class AccessListener implements RocketMQListener<PresenceListener.AccessInfo> {

    private static final Logger log = LoggerFactory.getLogger(AccessListener.class);

    @Value("${rocketmq.topic.accessDelay}")
    private String accessDelayTopic;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /** 事件类型 离开房间 */
    public static final String LEAVE_ROOM = "leave_room";

    /** 事件类型 进入房间 */
    public static final String ENTER_ROOM = "enter_room";

    /** 事件类型 离开区域0 */
    public static final String LEAVE_ZONE_0= "leave_zone_0";
    /** 事件类型 进入区域0 */
    public static final String ENTER_ZONE_0 = "enter_zone_0";

    /** 事件类型 离开区域1 */
    public static final String LEAVE_ZONE_1= "leave_zone_1";
    /** 事件类型 进入区域1 */
    public static final String ENTER_ZONE_1 = "enter_zone_1";

    /** 事件类型 离开区域2 */
    public static final String LEAVE_ZONE_2= "leave_zone_2";
    /** 事件类型 进入区域2 */
    public static final String ENTER_ZONE_2 = "enter_zone_2";

    /** 事件类型 离开区域3 */
    public static final String LEAVE_ZONE_3= "leave_zone_3";
    /** 事件类型 进入区域3 */
    public static final String ENTER_ZONE_3 = "enter_zone_3";

    private static final String ACCESS_DELAY_TOPIC = "access_delay_";

    @Override
    public void onMessage(PresenceListener.AccessInfo accessInfo) {
        log.debug("----" + System.currentTimeMillis() + "----" + " 房间进出计算事件消息： " + JSON.toJSONString(accessInfo));

        // todo 计算并发送事件
    }
}
