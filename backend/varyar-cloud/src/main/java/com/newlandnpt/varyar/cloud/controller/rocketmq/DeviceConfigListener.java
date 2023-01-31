package com.newlandnpt.varyar.cloud.controller.rocketmq;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newlandnpt.varyar.cloudBase.constant.CacheConstants;
import com.newlandnpt.varyar.cloudBase.utils.HttpClientUtil;
import com.newlandnpt.varyar.common.core.domain.config.DeviceConfig;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.common.utils.sign.Base64;
import com.newlandnpt.varyar.common.utils.spring.SpringUtils;


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
	private static final String DeviceConfigURL = "https://api.walabot-home.cn/deviceAdmin/{}/config";
	private static final String AuthenticateURL = "https://api.walabot-home.cn/authenticate";

	@Autowired
	private RocketMQTemplate rocketMQTemplate;
	
	@Value("${rocketmq.topic.deviceConfigErr}")
	private String deviceConfigErrTopic;
	
    @Override
    public void onMessage(DeviceConfig deviceConfig) {
    	log.info("监听到[{}], 报文对象：{} ", "设备参数下发消息", JSON.toJSONString(deviceConfig));
    	String token = SpringUtils.getBean(RedisCache.class).getCacheObject(CacheConstants.VAYYAR_TOKEN_KEY);
    	if(StringUtils.isBlank(token)){
    		String str = CacheConstants.VAYYAR_EMAIL + ":" + CacheConstants.VAYYAR_PWD;
    		String auth = "Basic " + Base64.encode(str.getBytes());
    		//"Basic ZWxpbmtlckBlbGlua2VyLmNjOjEyMzQ1Njc4OTA="
    		String result = HttpClientUtil.sendGet(AuthenticateURL, "", auth);
    		Map map = JSON.parseObject(result, Map.class);
    		JSONObject jsonObject = (JSONObject) map.get("token");
    		Integer expiresIn = (Integer) jsonObject.get("expiresIn");
    		String idToken = (String) jsonObject.get("idToken");
    		String refreshToken = (String) jsonObject.get("refreshToken");
    		if(StringUtils.isBlank(idToken) || expiresIn == null){
    			log.error("设备参数下发, 授权接口错误, 未取到token, 设备id:{}", deviceConfig.getDeviceId());
    			SendResult sendResult = rocketMQTemplate.syncSend(deviceConfigErrTopic, MessageBuilder.withPayload(deviceConfig.getDeviceId()).build());
    			//System.out.println(JSON.toJSONString(result));
    			if (!sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
    				log.error("MQ推送失败：{}", "设备参数下发失败事件");
    			}
    			return ;
    		}
    		//时间比给的时间略短一点
    		Integer timeOutSeconds = expiresIn - 60;
    		//System.out.println(idToken);
    		//过期时间最好比idToken的实际过期时间短
    		token = idToken;
    		log.info("取到token:{}", token);
    		SpringUtils.getBean(RedisCache.class).setCacheObject(CacheConstants.VAYYAR_TOKEN_KEY, token, timeOutSeconds, TimeUnit.SECONDS);
    	}
    	String url = StringUtils.format(DeviceConfigURL, deviceConfig.getDeviceId());
    	//"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiNjMxODQ1NWFiZTM2ZTlhOTU1MDY2ZTFmIiwiZW1haWwiOiJlbGlua2VyQGVsaW5rZXIuY2MiLCJpYXQiOjE2NzQxMTczMDcsImV4cCI6MTY3NDExNzMwN30.MC-EBlLay7GUvDOSAZx5mN3ToJR4dNDDh7HTZk6k3bI"
    	String auth = "Bearer " + token;
    	String result = HttpClientUtil.sendPost(url, deviceConfig, auth);
    	Map map = JSON.parseObject(result, Map.class);
    	JSONObject newConfigJsonObject = (JSONObject) map.get("newConfig");
    	JSONObject oldConfigJsonObject = (JSONObject) map.get("oldConfig");
    	if(newConfigJsonObject == null || oldConfigJsonObject == null){
    		log.error("设备参数下发失败, 设备id:{}", deviceConfig.getDeviceId());
    		log.error("设备参数下发失败, 报文:{}", JSON.toJSONString(deviceConfig));
    		SendResult sendResult = rocketMQTemplate.syncSend(deviceConfigErrTopic, MessageBuilder.withPayload(deviceConfig.getDeviceId()).build());
			//System.out.println(JSON.toJSONString(result));
			if (!sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
				log.error("MQ推送失败：{}", "设备参数下发失败事件");
			}
    	}
    	log.info("参数下发response:" + result);
    }
}
