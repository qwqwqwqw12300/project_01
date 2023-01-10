package com.newlandnpt.varyar.api.controller.business.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author lisd
 * @date 2023/1/4
 **/
@Component
@Slf4j
//@RocketMQMessageListener(topic = "${rocketmq.topic.access}", consumerGroup = "${rocketmq.group.access}")
public class AccessListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        log.debug("----" + System.currentTimeMillis() + "----" + " 监听到进出房间事件消息： " + s);
    }
}
