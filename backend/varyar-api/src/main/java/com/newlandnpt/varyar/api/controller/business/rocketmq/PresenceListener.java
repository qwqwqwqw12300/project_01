package com.newlandnpt.varyar.api.controller.business.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.newlandnpt.varyar.cloudBase.domain.Presence;

/**
 * MQ监听 - 进出事件
 * @author Bean
 * @date 2023-01-11
 */
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic.presence}", consumerGroup = "${rocketmq.group.presence}")
public class PresenceListener implements RocketMQListener<Presence> {

	private static final Logger log = LoggerFactory.getLogger(PresenceListener.class);
	
    @Override
    public void onMessage(Presence presence) {
    	log.debug("监听到[{}], 报文对象：{} " ,"进出区域事件消息"  ,JSON.toJSONString(presence));
    }
}
