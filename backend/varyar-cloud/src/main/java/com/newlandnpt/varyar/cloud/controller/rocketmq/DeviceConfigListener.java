package com.newlandnpt.varyar.cloud.controller.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.newlandnpt.varyar.cloudBase.domain.device.DeviceConfig;

/**
 * MQ监听 - 设备状态事件
 * @author Bean
 * @date 2023-01-11
 */
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic.deviceConfig}", consumerGroup = "${rocketmq.group.deviceConfig}")
public class DeviceConfigListener implements RocketMQListener<DeviceConfig> {

	private static final Logger log = LoggerFactory.getLogger(DeviceConfigListener.class);
	
    @Override
    public void onMessage(DeviceConfig deviceConfig) {
    	log.debug("监听到[{}], 报文对象：{} " ,"设备参数下发消息"  ,JSON.toJSONString(deviceConfig));
    }
}
