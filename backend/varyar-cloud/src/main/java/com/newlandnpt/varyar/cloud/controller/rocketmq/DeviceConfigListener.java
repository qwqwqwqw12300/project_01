package com.newlandnpt.varyar.cloud.controller.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.newlandnpt.varyar.cloudBase.domain.device.config.DeviceConfig;
import com.newlandnpt.varyar.cloudBase.utils.HttpClientUtil;

/**
 * MQ监听 - 设备状态事件
 * @author Bean
 * @date 2023-01-11
 */
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic.deviceConfig}", consumerGroup = "${rocketmq.group.deviceConfig}")
public class DeviceConfigListener implements RocketMQListener<DeviceConfig> {

	private static final Logger log = LoggerFactory.getLogger(DeviceConfigListener.class);
	
	private static final String URL = "http://127.0.0.1:8088/cloud/api/msg/device/test";
	
    @Override
    public void onMessage(DeviceConfig deviceConfig) {
    	log.debug("监听到[{}], 报文对象：{} ", "设备参数下发消息", JSON.toJSONString(deviceConfig));
    	HttpClientUtil.sendPost(URL, deviceConfig);
    }
}
