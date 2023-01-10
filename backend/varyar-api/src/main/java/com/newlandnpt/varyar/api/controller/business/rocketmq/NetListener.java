package com.newlandnpt.varyar.api.controller.business.rocketmq;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newlandnpt.varyar.system.service.DeviceDisconnectionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lisd
 * @date 2023/1/4
 **/
@Component
@Slf4j
@RocketMQMessageListener(topic = "${rocketmq.topic.net}", consumerGroup = "${rocketmq.group.net}")
public class NetListener implements RocketMQListener<String> {

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
