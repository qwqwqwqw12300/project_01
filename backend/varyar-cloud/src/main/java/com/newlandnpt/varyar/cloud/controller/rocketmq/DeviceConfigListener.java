package com.newlandnpt.varyar.cloud.controller.rocketmq;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newlandnpt.varyar.cloudBase.domain.device.config.DeviceConfig;
import com.newlandnpt.varyar.cloudBase.utils.HttpClientUtil;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.common.utils.sign.Base64;
import com.newlandnpt.varyar.system.service.HttpSendService;


/**
 * MQ监听 - 设备状态事件
 * @author Bean
 * @date 2023-01-11
 */
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic.deviceConfig}", consumerGroup = "${rocketmq.group.deviceConfig}")
public class DeviceConfigListener implements RocketMQListener<DeviceConfig> {

	private static final Logger log = LoggerFactory.getLogger(DeviceConfigListener.class);
	
	//private static final String URL = "http://127.0.0.1:8088/cloud/api/msg/device/test";
	private static final String URL = "https://api.walabot-home.cn/deviceAdmin/{}/config";
		
    @Override
    public void onMessage(DeviceConfig deviceConfig) {
    	String url = StringUtils.format(URL, deviceConfig.getDeviceId());
    	log.debug("监听到[{}], 报文对象：{} ", "设备参数下发消息", JSON.toJSONString(deviceConfig));
    	String result = HttpClientUtil.sendPost(url, deviceConfig);
    	System.out.println(result);
    }
    
    //Authorization:Basic ZWxpbmtlckBlbGlua2VyLmNjOjEyMzQ1Njc4OTA=
    public static void main(String[] args) {
		String str = "elinker@elinker.cc:1234567890";
		String ret = Base64.encode(str.getBytes());
		System.out.println(ret);

		String returnJson = HttpClientUtil.sendGet("https://api.walabot-home.cn/authenticate");
		Map map = JSON.parseObject(returnJson, Map.class);
		JSONObject jsonObject = (JSONObject) map.get("token");
		Integer expiresIn = (Integer) jsonObject.get("expiresIn");
		String idToken = (String) jsonObject.get("idToken");
		String refreshToken = (String) jsonObject.get("refreshToken");
		System.out.println(idToken);
		
//		Map<String, String> paramsMap = new LinkedHashMap<String, String>();
		
//		String str2 = HttpSendService.sendPost(paramsMap, "https://api.walabot-home.cn/authenticate");
	}
}
