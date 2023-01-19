package com.newlandnpt.varyar.api.controller.business.rocketmq;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newlandnpt.varyar.system.service.DeviceEventService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lisd
 * @date 2023/1/18
 **/
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic.deviceActive}", consumerGroup = "${rocketmq.group.deviceActive}")
public class DeviceActiveListener implements RocketMQListener<String> {

    private static final Logger log = LoggerFactory.getLogger(DeviceActiveListener.class);

    @Autowired
    private DeviceEventService deviceEventService;

    @Override
    public void onMessage(String s) {
        log.debug("----" + System.currentTimeMillis() + "----" + " 监听到设备激活事件消息： " + s);
        JSONObject jsonObject = JSON.parseObject(s);
        //消息s格式
        String deviceNo = jsonObject.getString("deviceNo");
        deviceEventService.deviceStateIssue(deviceNo);
        log.debug("设备激活事件结束");
    }
}
