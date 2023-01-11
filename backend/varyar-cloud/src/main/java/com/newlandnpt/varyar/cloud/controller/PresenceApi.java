package com.newlandnpt.varyar.cloud.controller;

import javax.annotation.Resource;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newlandnpt.varyar.cloudBase.domain.Presence;
import com.newlandnpt.varyar.cloudBase.service.PresenceService;

/**
 * 进出区域通知
 * @author bean
 */
@Controller
@RequestMapping("/cloud/notify")
public class PresenceApi {
	
    private static final Logger log = LoggerFactory.getLogger(PresenceApi.class);
    
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    
    @Value("${rocketmq.topic.fall}")
    private String fallTopic;
    
    @Resource(name = "cloud.presenceService")
	private PresenceService presenceService;

    @GetMapping("/presence")
    @ResponseBody
    public void presence(@RequestBody Presence presence) {
    	presenceService.receve(presence);
    	SendResult result = rocketMQTemplate.syncSend(fallTopic, MessageBuilder.withPayload(presence).build());
    	//System.out.println(JSON.toJSONString(result));
        if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
            log.error("MQ推送失败：{}", "摔倒事件");
        }
    }

}
