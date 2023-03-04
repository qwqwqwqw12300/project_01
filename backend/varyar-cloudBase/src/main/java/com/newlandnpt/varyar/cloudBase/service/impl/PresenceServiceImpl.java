package com.newlandnpt.varyar.cloudBase.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.newlandnpt.varyar.system.domain.TRoomZone;
import org.apache.commons.collections4.CollectionUtils;
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
import com.newlandnpt.varyar.common.core.domain.vo.TrackerTargetVo;
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

	 @Value("${cloud.customCalculateRegionDetected:false}")
	 private boolean customCalculateRegionDetected;
	    
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

		if(customCalculateRegionDetected){
			customCalculateRegionDetected(t, device);
		}

		event(t);
		//记录目标坐标
		//如果redis开关开启，则记录
		Boolean targetLocation = SpringUtils.getBean(RedisCache.class).getCacheObject(com.newlandnpt.varyar.common.constant.CacheConstants.TARGET_LOCATION_SWITCH_KEY + deviceId);
		if(targetLocation != null && targetLocation){
			List<TrackerTargetVo> trackerTargets = t.getTrackerTargets();
			//坐标信息存入redis一分钟过期
			SpringUtils.getBean(RedisCache.class).setCacheObject(com.newlandnpt.varyar.common.constant.CacheConstants.TARGET_LOCATION_PRESENCE_KEY + deviceId, trackerTargets, 1, TimeUnit.MINUTES);
		}
	}

	private void customCalculateRegionDetected(Presence t, TDevice device) {
		// 使用自定义规则计算子区域是否有对象
		try{
			Map<String, Integer> regionMap = t.getRegionMap();
			regionMap.put("0",0);
			regionMap.put("1",0);
			regionMap.put("2",0);
			regionMap.put("3",0);

			if(device.getParameter() != null && (device.getParameter() instanceof TDevice.RadarWaveDeviceSettings)){
				log.warn(">>>> 设备号：{},获取不到设备雷达波配置信息，将使用设备提供的区域参数", t.getDeviceId());
				TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings) device.getParameter();

				if(CollectionUtils.isNotEmpty(radarWaveDeviceSettings.getRoomZones())&&
						CollectionUtils.isNotEmpty(t.getTrackerTargets())){
					// 子区域不为空时计算
					// 设备上报追踪对象不为空时计算
					// 过滤出屏蔽区域
					List<TRoomZone> banRoomZones = radarWaveDeviceSettings.getRoomZones()
							.stream()
							.filter(p->"1".equals(p.getZoneType()))
							.collect(Collectors.toList());
					//过滤掉在屏蔽区域内的对象
					List<TrackerTargetVo> filteredTargets = t.getTrackerTargets()
							.stream()
							.filter(p-> banRoomZones.stream()
									.noneMatch(banRoomZone->targetInZoom(p, banRoomZone)))
							.collect(Collectors.toList());
					for(int i=0;i<radarWaveDeviceSettings.getRoomZones().size();i++){
						TRoomZone roomZone = radarWaveDeviceSettings.getRoomZones().get(i);
						if(filteredTargets.stream().anyMatch(p->targetInZoom(p,roomZone))){
							// 任意目标在子区域内则将子区域存在标记置为1
							regionMap.put(""+i,1);
						}
					}

				}

				t.setRegionMap(regionMap);
				log.info(">>>>> 使用自定义规则计算子区域是否有对象后deviceId:{},子区域检测：{}",device.getNo(),regionMap);
			}
		}catch (Exception e){
			log.error(">>>>> 自定义规则计算子区域是否有对象 失败,将使用设备提供的区域参数",e);
		}
	}

	private boolean targetInZoom(TrackerTargetVo p, TRoomZone banRoomZone) {
		return banRoomZone.getX1().multiply(new BigDecimal(100)).compareTo(new BigDecimal(p.getxPosCm())) < 1 &&
				banRoomZone.getX2().multiply(new BigDecimal(100)).compareTo(new BigDecimal(p.getxPosCm())) > -1 &&
				banRoomZone.getY1().multiply(new BigDecimal(100)).compareTo(new BigDecimal(p.getyPosCm())) < 1 &&
				banRoomZone.getY2().multiply(new BigDecimal(100)).compareTo(new BigDecimal(p.getyPosCm())) > -1 &&
				banRoomZone.getZ1().multiply(new BigDecimal(100)).compareTo(new BigDecimal(p.getzPosCm())) < 1 &&
				banRoomZone.getZ2().multiply(new BigDecimal(100)).compareTo(new BigDecimal(p.getzPosCm())) > -1;
	}

	//事件处理
	private void event(Presence t){
		String deviceId = t.getDeviceId();
		//当前进出事件
    	Boolean roomPresenceDetected = t.getRoomPresenceDetected();
    	Map<String, Integer> regionMap = t.getRegionMap();
    	Integer regionDetected0 = regionMap.get("0");
    	Integer regionDetected1 = regionMap.get("1");
    	Integer regionDetected2 = regionMap.get("2");
    	Integer regionDetected3 = regionMap.get("3");
    	
    	//旧的进出事件
    	Boolean oldRoomPresenceDetected = SpringUtils.getBean(RedisCache.class).getCacheObject(com.newlandnpt.varyar.common.constant.CacheConstants.PRESENCE_ROOM_KEY + deviceId);
    	Integer oldRegionDetected0 = SpringUtils.getBean(RedisCache.class).getCacheObject(CacheConstants.PRESENCE_REGION0_KEY + deviceId);
    	Integer oldRegionDetected1 = SpringUtils.getBean(RedisCache.class).getCacheObject(CacheConstants.PRESENCE_REGION1_KEY + deviceId);
    	Integer oldRegionDetected2 = SpringUtils.getBean(RedisCache.class).getCacheObject(CacheConstants.PRESENCE_REGION2_KEY + deviceId);
    	Integer oldRegionDetected3 = SpringUtils.getBean(RedisCache.class).getCacheObject(CacheConstants.PRESENCE_REGION3_KEY + deviceId);
    	
    	//新旧事件不同，则更新到缓存
    	if(roomPresenceDetected != oldRoomPresenceDetected){
    		SpringUtils.getBean(RedisCache.class).setCacheObject(com.newlandnpt.varyar.common.constant.CacheConstants.PRESENCE_ROOM_KEY + deviceId, roomPresenceDetected);
    	}
    	if(!regionDetected0.equals(oldRegionDetected0)){
    		SpringUtils.getBean(RedisCache.class).setCacheObject(CacheConstants.PRESENCE_REGION0_KEY + deviceId, regionDetected0);
    	}
    	if(!regionDetected1.equals(oldRegionDetected1)){
    		SpringUtils.getBean(RedisCache.class).setCacheObject(CacheConstants.PRESENCE_REGION1_KEY + deviceId, regionDetected1);
    	}
    	if(!regionDetected2.equals(oldRegionDetected2)){
    		SpringUtils.getBean(RedisCache.class).setCacheObject(CacheConstants.PRESENCE_REGION2_KEY + deviceId, regionDetected2);
    	}
    	if(!regionDetected3.equals(oldRegionDetected3)){
    		SpringUtils.getBean(RedisCache.class).setCacheObject(CacheConstants.PRESENCE_REGION3_KEY + deviceId, regionDetected3);
    	}
    	
    	//推送房间进出事件
    	if(oldRoomPresenceDetected == null){
    		//原来没有房间是否有人的数据
    		String enentType = roomPresenceDetected ? VayyarCloudConstants.EVENT_ROOM_IN : VayyarCloudConstants.EVENT_ROOM_OUT;
    		t.setEventType(enentType);
    		SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    		log.info("发送进出房间事件: {}", enentType);
    		if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("MQ推送失败：{}", "进出房间事件");
            }
    	}else {
    		if(oldRoomPresenceDetected && !roomPresenceDetected){
    			t.setEventType(VayyarCloudConstants.EVENT_ROOM_OUT);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			log.info("发送进出房间事件: {}", VayyarCloudConstants.EVENT_ROOM_OUT);
    			if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
    	            log.error("MQ推送失败：{}", "出房间事件");
    	        }
    		}
    		if(!oldRoomPresenceDetected && roomPresenceDetected){
    			t.setEventType(VayyarCloudConstants.EVENT_ROOM_IN);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			log.info("发送进出房间事件: {}", VayyarCloudConstants.EVENT_ROOM_IN);
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
    		log.info("发送进出区域0事件: {}", enentType);
    		if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("MQ推送失败：{}", "进出区域0事件");
            }
    	}else {
    		if(oldRegionDetected0 == 1 && regionDetected0 == 0){
    			t.setEventType(VayyarCloudConstants.EVENT_REGION0_OUT);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			log.info("发送进出区域0事件: {}", VayyarCloudConstants.EVENT_REGION0_OUT);
    			if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
    	            log.error("MQ推送失败：{}", "出区域0事件");
    	        }
    		}
    		if(oldRegionDetected0 == 0 && regionDetected0 == 1){
    			t.setEventType(VayyarCloudConstants.EVENT_REGION0_IN);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			log.info("发送进出区域0事件: {}", VayyarCloudConstants.EVENT_REGION0_IN);
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
    		log.info("发送进出区域1事件: {}", enentType);
    		if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("MQ推送失败：{}", "进出区域1事件");
            }
    	}else {
    		if(oldRegionDetected1 == 1 && regionDetected1 == 0){
    			t.setEventType(VayyarCloudConstants.EVENT_REGION1_OUT);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			log.info("发送进出区域1事件: {}", VayyarCloudConstants.EVENT_REGION1_OUT);
    			if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
    	            log.error("MQ推送失败：{}", "出区域1事件");
    	        }
    		}
    		if(oldRegionDetected1 == 0 && regionDetected1 == 1){
    			t.setEventType(VayyarCloudConstants.EVENT_REGION1_IN);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			log.info("发送进出区域1事件: {}", VayyarCloudConstants.EVENT_REGION1_IN);
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
    		log.info("发送进出区域2事件: {}", enentType);
    		if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("MQ推送失败：{}", "进出区域2事件");
            }
    	}else {
    		if(oldRegionDetected2 == 1 && regionDetected2 == 0){
    			t.setEventType(VayyarCloudConstants.EVENT_REGION2_OUT);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			log.info("发送进出区域2事件: {}", VayyarCloudConstants.EVENT_REGION2_OUT);
    			if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
    	            log.error("MQ推送失败：{}", "出区域2事件");
    	        }
    		}
    		if(oldRegionDetected2 == 0 && regionDetected2 == 1){
    			t.setEventType(VayyarCloudConstants.EVENT_REGION2_IN);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			log.info("发送进出区域2事件: {}", VayyarCloudConstants.EVENT_REGION2_IN);
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
    		log.info("发送进出区域3事件: {}", enentType);
    		if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("MQ推送失败：{}", "进出区域3事件");
            }
    	}else {
    		if(oldRegionDetected3 == 1 && regionDetected3 == 0){
    			t.setEventType(VayyarCloudConstants.EVENT_REGION3_OUT);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			log.info("发送进出区域3事件: {}", VayyarCloudConstants.EVENT_REGION3_OUT);
    			if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
    	            log.error("MQ推送失败：{}", "出区域3事件");
    	        }
    		}
    		if(oldRegionDetected3 == 0 && regionDetected3 == 1){
    			t.setEventType(VayyarCloudConstants.EVENT_REGION3_IN);
    			SendResult result = rocketMQTemplate.syncSend(presenceTopic, MessageBuilder.withPayload(t).build());
    			log.info("发送进出区域3事件: {}", VayyarCloudConstants.EVENT_REGION3_IN);
    			if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
    	            log.error("MQ推送失败：{}", "进区域3事件");
    	        }
    		}
    	}
	}

}
