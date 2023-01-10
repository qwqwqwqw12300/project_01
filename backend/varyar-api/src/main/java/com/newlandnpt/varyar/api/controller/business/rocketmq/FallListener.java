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
//@RocketMQMessageListener(topic = "${rocketmq.topic.fall}", consumerGroup = "${rocketmq.group.fall}")
public class FallListener  implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        log.debug("----" + System.currentTimeMillis() + "----" + " 监听到跌倒事件消息： " + s);
    }
}
