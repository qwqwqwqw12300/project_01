package com.newlandnpt.varyar.cloudBase.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.newlandnpt.varyar.cloudBase.domain.Presence;
import com.newlandnpt.varyar.cloudBase.service.PresenceService;


@Service("cloud.presenceService")
public class PresenceServiceImpl implements PresenceService {
	
	 private static final Logger log = LoggerFactory.getLogger(PresenceServiceImpl.class);
	 
	 @Autowired
	 private RocketMQTemplate rocketMQTemplate;
	 
	 @Value("${rocketmq.topic.presence}")
	 private String presenceTopic;
	    
	
	@Override
	public void receve(Presence t) {
		String deviceId = t.getDeviceId();
		if(StringUtils.isBlank(deviceId)) {
			return ;
		}
		SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    	//System.out.println(JSON.toJSONString(result));
        if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
            log.error("MQ推送失败：{}", "进出区域事件");
        }
	}

}
