package com.newlandnpt.varyar.tcp.gateway.rocketmq;

import com.newlandnpt.varyar.common.core.domain.config.DeviceConfig;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * MQ监听 - 设备参数下发
 * @author Bean
 * @date 2023-01-11
 */
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic.deviceConfig}", consumerGroup = "${rocketmq.group.deviceConfig}",
		selectorType= SelectorType.TAG,selectorExpression="watch")
public class DeviceConfigListener implements RocketMQListener<DeviceConfig> {

	private static final Logger log = LoggerFactory.getLogger(DeviceConfigListener.class);

	@Autowired
	private RocketMQTemplate rocketMQTemplate;
	
	@Value("${rocketmq.topic.deviceConfigErr}")
	private String deviceConfigErrTopic;
	
    @Override
    public void onMessage(DeviceConfig deviceConfig) {



    }
}
