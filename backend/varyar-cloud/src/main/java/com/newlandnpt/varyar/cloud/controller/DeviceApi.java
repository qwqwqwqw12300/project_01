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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newlandnpt.varyar.cloud.controller.rocketmq.DeviceConfigListener;
import com.newlandnpt.varyar.cloudBase.domain.device.config.DeviceConfig;
import com.newlandnpt.varyar.cloudBase.service.FallService;

/**
 * 设备相关接口
 * @author bean
 */
@Controller
@RequestMapping("/cloud/api/msg/device")
public class DeviceApi {
    
	private static final Logger log = LoggerFactory.getLogger(DeviceConfigListener.class);
	
    @Resource(name = "cloud.fallService")
	private FallService fallService;
    
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    
    @Value("${rocketmq.topic.deviceConfig}")
    private String deviceConfigTopic;
    
    /** 接收下发参数，可以复制到其他包 **/
    @GetMapping("/config")
    @ResponseBody
    public void config(@RequestBody DeviceConfig deviceConfig) {
    	
    	SendResult result = rocketMQTemplate.syncSend(deviceConfigTopic, MessageBuilder.withPayload(deviceConfig).build());
    	//System.out.println(JSON.toJSONString(result));
        if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
            log.error("MQ推送失败：{}", "摔倒事件");
        }
    }

    /** 测试接口http post是否收到消息 **/
    @PostMapping("/test")
    @ResponseBody
    public void test(DeviceConfig deviceConfig) {
    	System.out.println("test ok");
    }
}
