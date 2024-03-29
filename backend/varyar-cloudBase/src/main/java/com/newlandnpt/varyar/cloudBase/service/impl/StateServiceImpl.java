package com.newlandnpt.varyar.cloudBase.service.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

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

import com.newlandnpt.varyar.cloudBase.constant.CacheConstants;
import com.newlandnpt.varyar.cloudBase.domain.State;
import com.newlandnpt.varyar.cloudBase.mapper.StateMapper;
import com.newlandnpt.varyar.cloudBase.service.StateService;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.utils.spring.SpringUtils;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.IDeviceService;


@Service("cloud.stateService")
public class StateServiceImpl implements StateService {
	
	private static final Logger log = LoggerFactory.getLogger(StateServiceImpl.class);
	
	@Resource(name = "cloud.stateMapper")
	private StateMapper stateMapper;
	
	@Autowired
	private IDeviceService tDeviceService;
	
	 @Autowired
	 private RocketMQTemplate rocketMQTemplate;
	 
	 @Value("${rocketmq.topic.deviceState}")
	 private String deviceStateTopic;
	 
	 @Value("${rocketmq.topic.deviceActive}")
	 private String deviceActiveTopic;

	 @Override
	 public void receve(State t) {
		 String deviceId = t.getDeviceId();
		 if(StringUtils.isBlank(deviceId)) {
			 return ;
		 }
		 TDevice device = tDeviceService.loadDeviceFromCacheByNo(deviceId);
		 if(device == null){
			 return ;
		 }
		 State obj = stateMapper.selectByDeviceId(deviceId);
		 if(obj == null) {
			 stateMapper.insert(t);
			 SendResult result = rocketMQTemplate.syncSend(deviceActiveTopic, MessageBuilder.withPayload(t).build());
			 //System.out.println(JSON.toJSONString(result));
			 if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
				 log.error("MQ推送失败：{}", "设备激活事件");
			 }
		 } else {
			 stateMapper.update(t);
		 }
		 SpringUtils.getBean(RedisCache.class).setCacheObject(CacheConstants.DEVICE_STATE_KEY + t.getDeviceId(), t, CacheConstants.STATE_EXPIRATION, TimeUnit.MINUTES);
		 SendResult result = rocketMQTemplate.syncSend(deviceStateTopic, MessageBuilder.withPayload(t).build());
		 //System.out.println(JSON.toJSONString(result));
		 if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
			 log.error("MQ推送失败：{}", "设备状态事件");
		 }
	 }

}
