package com.newlandnpt.varyar.api.controller.business.rocketmq;

import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newlandnpt.varyar.system.service.DeviceDisconnectionService;

/**
 * @author lisd
 * @date 2023/1/4
 **/
@Component
//@Slf4j
//@RocketMQMessageListener(topic = "${rocketmq.topic.net}", consumerGroup = "${rocketmq.group.net}")
public class NetListener implements RocketMQListener<String> {

	private static final Logger log = LoggerFactory.getLogger(NetListener.class);
	
    @Autowired
    private DeviceDisconnectionService disconnectionService;

    @Override
    public void onMessage(String s) {
        log.debug("----" + System.currentTimeMillis() + "----" + " 监听到断网事件消息： " + s);
        JSONObject jsonObject = JSON.parseObject(s);
        String deviceNo = jsonObject.get("deviceNo").toString();
        disconnectionService.deviceDisconnectionIssue(deviceNo);
    }
}
