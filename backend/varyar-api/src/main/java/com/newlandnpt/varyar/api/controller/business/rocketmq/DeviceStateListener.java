package com.newlandnpt.varyar.api.controller.business.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.newlandnpt.varyar.cloudBase.domain.State;

/**
 * MQ监听 - 设备状态事件
 * @author Bean
 * @date 2023-01-11
 */
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic.deviceState}", consumerGroup = "${rocketmq.group.deviceState}")
public class DeviceStateListener implements RocketMQListener<State> {

	private static final Logger log = LoggerFactory.getLogger(DeviceStateListener.class);
	
    @Override
    public void onMessage(State state) {
    	log.debug("监听到[{}], 报文对象：{} " ,"设备状态消息"  ,JSON.toJSONString(state));
    }
}
