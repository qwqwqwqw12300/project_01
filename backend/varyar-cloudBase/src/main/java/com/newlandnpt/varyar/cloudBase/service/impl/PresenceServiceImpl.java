package com.newlandnpt.varyar.cloudBase.service.impl;

import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import com.newlandnpt.varyar.cloudBase.constant.VayyarCloudConstants;
import com.newlandnpt.varyar.cloudBase.domain.Presence;
import com.newlandnpt.varyar.cloudBase.service.PresenceService;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.utils.spring.SpringUtils;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.IDeviceService;


@Service("cloud.presenceService")
public class PresenceServiceImpl implements PresenceService {
	
	 private static final Logger log = LoggerFactory.getLogger(PresenceServiceImpl.class);
	 
	 @Autowired
	 private IDeviceService tDeviceService;
	 
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
		TDevice device = tDeviceService.loadDeviceFromCacheByNo(deviceId);
		if(device == null){
			return ;
		}
		String diviceId = t.getDeviceId();
		//当前进出事件
    	Boolean roomPresenceDetected = t.getRoomPresenceDetected();
    	Map<String, Integer> regionMap = t.getRegionMap();
    	Integer regionDetected0 = regionMap.get("0");
    	Integer regionDetected1 = regionMap.get("1");
    	Integer regionDetected2 = regionMap.get("2");
    	Integer regionDetected3 = regionMap.get("3");
    	
    	//旧的进出事件
    	Boolean oldRoomPresenceDetected = SpringUtils.getBean(RedisCache.class).getCacheObject(CacheConstants.PRESENCE_ROOM_KEY + t.getDeviceId());
    	Integer oldRegionDetected0 = SpringUtils.getBean(RedisCache.class).getCacheObject(CacheConstants.PRESENCE_REGION0_KEY + t.getDeviceId());
    	Integer oldRegionDetected1 = SpringUtils.getBean(RedisCache.class).getCacheObject(CacheConstants.PRESENCE_REGION1_KEY + t.getDeviceId());
    	Integer oldRegionDetected2 = SpringUtils.getBean(RedisCache.class).getCacheObject(CacheConstants.PRESENCE_REGION2_KEY + t.getDeviceId());
    	Integer oldRegionDetected3 = SpringUtils.getBean(RedisCache.class).getCacheObject(CacheConstants.PRESENCE_REGION3_KEY + t.getDeviceId());
    	
    	//新旧事件不同，则更新到缓存
    	if(roomPresenceDetected != oldRoomPresenceDetected){
    		SpringUtils.getBean(RedisCache.class).setCacheObject(CacheConstants.PRESENCE_ROOM_KEY + t.getDeviceId(), roomPresenceDetected);
    	}
    	if(!regionDetected0.equals(oldRegionDetected0)){
    		SpringUtils.getBean(RedisCache.class).setCacheObject(CacheConstants.PRESENCE_REGION0_KEY + t.getDeviceId(), regionDetected0);
    	}
    	if(!regionDetected1.equals(oldRegionDetected1)){
    		SpringUtils.getBean(RedisCache.class).setCacheObject(CacheConstants.PRESENCE_REGION1_KEY + t.getDeviceId(), regionDetected1);
    	}
    	if(!regionDetected2.equals(oldRegionDetected2)){
    		SpringUtils.getBean(RedisCache.class).setCacheObject(CacheConstants.PRESENCE_REGION2_KEY + t.getDeviceId(), regionDetected2);
    	}
    	if(!regionDetected3.equals(oldRegionDetected3)){
    		SpringUtils.getBean(RedisCache.class).setCacheObject(CacheConstants.PRESENCE_REGION3_KEY + t.getDeviceId(), regionDetected3);
    	}
    	
    	//推送房间进出事件
    	if(oldRoomPresenceDetected == null){
    		//原来没有房间是否有人的数据
    		String enentType = roomPresenceDetected ? VayyarCloudConstants.EVENT_ROOM_IN : VayyarCloudConstants.EVENT_ROOM_OUT;
    		t.setEventType(enentType);
    		SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    		if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("MQ推送失败：{}", "进出房间事件");
            }
    	}else {
    		if(oldRoomPresenceDetected && !roomPresenceDetected){
    			t.setEventType(VayyarCloudConstants.EVENT_ROOM_OUT);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
    	            log.error("MQ推送失败：{}", "出房间事件");
    	        }
    		}
    		if(!oldRoomPresenceDetected && roomPresenceDetected){
    			t.setEventType(VayyarCloudConstants.EVENT_ROOM_IN);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
    	            log.error("MQ推送失败：{}", "进房间事件");
    	        }
    		}
    	}
    	
    	//区域0进出事件
    	if(oldRegionDetected0 == null){
    		String enentType = regionDetected0 == 1 ? VayyarCloudConstants.EVENT_REGION0_IN : VayyarCloudConstants.EVENT_REGION0_OUT;
    		t.setEventType(enentType);
    		SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    		if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("MQ推送失败：{}", "进出区域0事件");
            }
    	}else {
    		if(oldRegionDetected0 == 1 && regionDetected0 == 0){
    			t.setEventType(VayyarCloudConstants.EVENT_REGION0_OUT);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
    	            log.error("MQ推送失败：{}", "出区域0事件");
    	        }
    		}
    		if(oldRegionDetected0 == 0 && regionDetected0 == 1){
    			t.setEventType(VayyarCloudConstants.EVENT_REGION0_IN);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
    	            log.error("MQ推送失败：{}", "进区域0事件");
    	        }
    		}
    	}
    	//区域1进出事件
    	if(oldRegionDetected1 == null){
    		String enentType = regionDetected1 == 1 ? VayyarCloudConstants.EVENT_REGION1_IN : VayyarCloudConstants.EVENT_REGION1_OUT;
    		t.setEventType(enentType);
    		SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    		if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("MQ推送失败：{}", "进出区域1事件");
            }
    	}else {
    		if(oldRegionDetected1 == 1 && regionDetected1 == 0){
    			t.setEventType(VayyarCloudConstants.EVENT_REGION1_OUT);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
    	            log.error("MQ推送失败：{}", "出区域1事件");
    	        }
    		}
    		if(oldRegionDetected1 == 0 && regionDetected1 == 1){
    			t.setEventType(VayyarCloudConstants.EVENT_REGION1_IN);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
    	            log.error("MQ推送失败：{}", "进区域1事件");
    	        }
    		}
    	}
    	//区域2进出事件
    	if(oldRegionDetected2 == null){
    		String enentType = regionDetected2 == 1 ? VayyarCloudConstants.EVENT_REGION2_IN : VayyarCloudConstants.EVENT_REGION2_OUT;
    		t.setEventType(enentType);
    		SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    		if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("MQ推送失败：{}", "进出区域2事件");
            }
    	}else {
    		if(oldRegionDetected2 == 1 && regionDetected2 == 0){
    			t.setEventType(VayyarCloudConstants.EVENT_REGION2_OUT);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
    	            log.error("MQ推送失败：{}", "出区域2事件");
    	        }
    		}
    		if(oldRegionDetected2 == 0 && regionDetected2 == 1){
    			t.setEventType(VayyarCloudConstants.EVENT_REGION2_IN);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
    	            log.error("MQ推送失败：{}", "进区域2事件");
    	        }
    		}
    	}
    	//区域3进出事件
    	if(oldRegionDetected3 == null){
    		String enentType = regionDetected3 == 1 ? VayyarCloudConstants.EVENT_REGION3_IN : VayyarCloudConstants.EVENT_REGION3_OUT;
    		t.setEventType(enentType);
    		SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    		if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("MQ推送失败：{}", "进出区域3事件");
            }
    	}else {
    		if(oldRegionDetected3 == 1 && regionDetected3 == 0){
    			t.setEventType(VayyarCloudConstants.EVENT_REGION3_OUT);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
    	            log.error("MQ推送失败：{}", "出区域3事件");
    	        }
    		}
    		if(oldRegionDetected3 == 0 && regionDetected3 == 1){
    			t.setEventType(VayyarCloudConstants.EVENT_REGION3_IN);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
    	            log.error("MQ推送失败：{}", "进区域3事件");
    	        }
    		}
    	}
	}

}
