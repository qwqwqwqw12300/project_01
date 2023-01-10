package com.newlandnpt.varyar.api.controller.business.rocketmq;

import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author lisd
 * @date 2023/1/4
 **/
@Component
//@Slf4j
//@RocketMQMessageListener(topic = "${rocketmq.topic.access}", consumerGroup = "${rocketmq.group.access}")
public class AccessListener implements RocketMQListener<String> {

	private static final Logger log = LoggerFactory.getLogger(AccessListener.class);
	
    @Override
    public void onMessage(String s) {
        log.debug("----" + System.currentTimeMillis() + "----" + " 监听到进出房间事件消息： " + s);
    }
}
