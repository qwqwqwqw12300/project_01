package com.newlandnpt.varyar.api.controller.business.rocketmq;

import com.alibaba.fastjson.JSON;
import com.newlandnpt.varyar.cloudBase.constant.VayyarCloudConstants;
import com.newlandnpt.varyar.cloudBase.domain.Presence;
import com.newlandnpt.varyar.common.core.domain.entity.AccessInfo;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.IDeviceService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.newlandnpt.varyar.api.controller.business.rocketmq.AccessCalculateListener.*;
import static com.newlandnpt.varyar.common.constant.CacheConstants.*;

/**
 * MQ监听 - 进出事件
 * @author Bean
 * @date 2023-01-11
 */
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic.presence}", consumerGroup = "${rocketmq.group.presence}")
public class PresenceListener implements RocketMQListener<Presence> {

	private static final Logger log = LoggerFactory.getLogger(PresenceListener.class);

	/**
	 * 进出房间事件
	 * 设备模块接收到信息，mq推送，新增进房间事件
	 * 增加定时任务，规定时间内，没有出房间事件，则新增失联事件
	 */
	@Value("${rocketmq.topic.access}")
	private String accessTopic;
	@Autowired
	private RocketMQTemplate rocketMQTemplate;
	@Autowired
	private IDeviceService deviceService;

	@Autowired
	private RedisCache redisCache;
    @Override
    public void onMessage(Presence presence) {
		SendResult result = null;

		AccessInfo accessInfo = new AccessInfo();
		accessInfo.setDeviceNo(presence.getDeviceId());
		accessInfo.setTime(presence.getTimestampMillis());

		TDevice device = deviceService.selectByDeviceNo(presence.getDeviceNo());

		if(device == null){
			log.warn(">>>> 设备号：{},获取不到设备，忽略进出事件",presence.getDeviceNo());
			return;
		}
		if(device.getParameter() == null || !(device.getParameter() instanceof TDevice.RadarWaveDeviceSettings)){
			log.warn(">>>> 设备号：{},获取不到设备雷达波配置信息，忽略进出事件",presence.getDeviceNo());
			return;
		}
		TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)device.getParameter();


		accessInfo.setDeviceName(device.getName());

    	if(VayyarCloudConstants.EVENT_ROOM_IN.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"进房间事件"  ,JSON.toJSONString(presence));

			int peopleCount = presence.getRoomPresenceDetected()?1:0;
			accessInfo.setType(ENTER_ROOM);
//			int peopleCount = presence.getTrackerTargets()==null?0:
//					presence.getTrackerTargets().size();
			accessInfo.setPeopleCount(peopleCount);
			accessInfo.setAreaName("房间");
			String redisKey = T_DEVICE_VAYYAR_ACCESS_24HOURS_IN_ROOM_KEY + accessInfo.getDeviceNo();
			redisCache.setCacheObject(redisKey,accessInfo);

		}else if(VayyarCloudConstants.EVENT_ROOM_OUT.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"出房间事件"  ,JSON.toJSONString(presence));

			int peopleCount = presence.getRoomPresenceDetected()?1:0;
			accessInfo.setType(LEAVE_ROOM);
//			int peopleCount = presence.getTrackerTargets()==null?0:
//					presence.getTrackerTargets().size();
			accessInfo.setPeopleCount(peopleCount);
			accessInfo.setAreaName("房间");
			String redisKey = T_DEVICE_VAYYAR_ACCESS_24HOURS_OUT_ROOM_KEY + accessInfo.getDeviceNo();
			redisCache.setCacheObject(redisKey,accessInfo);

		}else if(VayyarCloudConstants.EVENT_REGION0_IN.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"进0号区域事件"  ,JSON.toJSONString(presence));
    		int zoneNo = 0;
			int peopleCount = presence.getRegionMap()==null?0:
					presence.getRegionMap().getOrDefault(""+zoneNo,0);
			if(radarWaveDeviceSettings.getRoomZones().size()<(zoneNo+1)){
				log.warn(">>>> 设备号：{},设备无{}号区域配置，忽略进出事件",presence.getDeviceNo(),zoneNo);
				return;
			}
			accessInfo.setType(ENTER_ZONE_0);
//			int peopleCount = presence.getTrackerTargets()==null?0:
//					presence.getTrackerTargets().size();
			accessInfo.setZoneNo(zoneNo);
			accessInfo.setPeopleCount(peopleCount);
			accessInfo.setAreaName(radarWaveDeviceSettings.getRoomZones().get(zoneNo).getName());
		}else if(VayyarCloudConstants.EVENT_REGION0_OUT.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"出0号区域事件"  ,JSON.toJSONString(presence));

			int zoneNo = 0;
			int peopleCount = presence.getRegionMap()==null?0:
					presence.getRegionMap().getOrDefault(""+zoneNo,0);
			if(radarWaveDeviceSettings.getRoomZones().size()<(zoneNo+1)){
				log.warn(">>>> 设备号：{},设备无{}号区域配置，忽略进出事件",presence.getDeviceNo(),zoneNo);
				return;
			}
			accessInfo.setType(LEAVE_ZONE_0);
//			int peopleCount = presence.getTrackerTargets()==null?0:
//					presence.getTrackerTargets().size();
			accessInfo.setZoneNo(zoneNo);
			accessInfo.setPeopleCount(peopleCount);
			accessInfo.setAreaName(radarWaveDeviceSettings.getRoomZones().get(zoneNo).getName());

    	}else if(VayyarCloudConstants.EVENT_REGION1_IN.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"进1号区域事件"  ,JSON.toJSONString(presence));

			int zoneNo = 1;
			int peopleCount = presence.getRegionMap()==null?0:
					presence.getRegionMap().getOrDefault(""+zoneNo,0);
			if(radarWaveDeviceSettings.getRoomZones().size()<(zoneNo+1)){
				log.warn(">>>> 设备号：{},设备无{}号区域配置，忽略进出事件",presence.getDeviceNo(),zoneNo);
				return;
			}
			accessInfo.setType(ENTER_ZONE_1);
//			int peopleCount = presence.getTrackerTargets()==null?0:
//					presence.getTrackerTargets().size();
			accessInfo.setZoneNo(zoneNo);
			accessInfo.setPeopleCount(peopleCount);
			accessInfo.setAreaName(radarWaveDeviceSettings.getRoomZones().get(zoneNo).getName());

    	}else if(VayyarCloudConstants.EVENT_REGION1_OUT.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"出1号区域事件"  ,JSON.toJSONString(presence));

			int zoneNo = 1;
			int peopleCount = presence.getRegionMap()==null?0:
					presence.getRegionMap().getOrDefault(""+zoneNo,0);
			if(radarWaveDeviceSettings.getRoomZones().size()<(zoneNo+1)){
				log.warn(">>>> 设备号：{},设备无{}号区域配置，忽略进出事件",presence.getDeviceNo(),zoneNo);
				return;
			}
			accessInfo.setType(LEAVE_ZONE_1);
//			int peopleCount = presence.getTrackerTargets()==null?0:
//					presence.getTrackerTargets().size();
			accessInfo.setZoneNo(zoneNo);
			accessInfo.setPeopleCount(peopleCount);
			accessInfo.setAreaName(radarWaveDeviceSettings.getRoomZones().get(zoneNo).getName());
    	}else if(VayyarCloudConstants.EVENT_REGION2_IN.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"进2号区域事件"  ,JSON.toJSONString(presence));
			int zoneNo = 2;
			int peopleCount = presence.getRegionMap()==null?0:
					presence.getRegionMap().getOrDefault(""+zoneNo,0);
			if(radarWaveDeviceSettings.getRoomZones().size()<(zoneNo+1)){
				log.warn(">>>> 设备号：{},设备无{}号区域配置，忽略进出事件",presence.getDeviceNo(),zoneNo);
				return;
			}
			accessInfo.setType(ENTER_ZONE_2);
//			int peopleCount = presence.getTrackerTargets()==null?0:
//					presence.getTrackerTargets().size();
			accessInfo.setZoneNo(zoneNo);
			accessInfo.setPeopleCount(peopleCount);
			accessInfo.setAreaName(radarWaveDeviceSettings.getRoomZones().get(zoneNo).getName());
    	}else if(VayyarCloudConstants.EVENT_REGION2_OUT.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"出2号区域事件"  ,JSON.toJSONString(presence));

			int zoneNo = 2;
			int peopleCount = presence.getRegionMap()==null?0:
					presence.getRegionMap().getOrDefault(""+zoneNo,0);
			if(radarWaveDeviceSettings.getRoomZones().size()<(zoneNo+1)){
				log.warn(">>>> 设备号：{},设备无{}号区域配置，忽略进出事件",presence.getDeviceNo(),zoneNo);
				return;
			}
			accessInfo.setType(LEAVE_ZONE_2);
//			int peopleCount = presence.getTrackerTargets()==null?0:
//					presence.getTrackerTargets().size();
			accessInfo.setZoneNo(zoneNo);
			accessInfo.setPeopleCount(peopleCount);
			accessInfo.setAreaName(radarWaveDeviceSettings.getRoomZones().get(zoneNo).getName());

    	}else if(VayyarCloudConstants.EVENT_REGION3_IN.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"进3号区域事件"  ,JSON.toJSONString(presence));

			int zoneNo = 3;
			int peopleCount = presence.getRegionMap()==null?0:
					presence.getRegionMap().getOrDefault(""+zoneNo,0);
			if(radarWaveDeviceSettings.getRoomZones().size()<(zoneNo+1)){
				log.warn(">>>> 设备号：{},设备无{}号区域配置，忽略进出事件",presence.getDeviceNo(),zoneNo);
				return;
			}
			accessInfo.setType(ENTER_ZONE_3);
//			int peopleCount = presence.getTrackerTargets()==null?0:
//					presence.getTrackerTargets().size();
			accessInfo.setZoneNo(zoneNo);
			accessInfo.setPeopleCount(peopleCount);
			accessInfo.setAreaName(radarWaveDeviceSettings.getRoomZones().get(zoneNo).getName());

    	}else if(VayyarCloudConstants.EVENT_REGION3_OUT.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"出3号区域事件"  ,JSON.toJSONString(presence));


			int zoneNo = 3;
			int peopleCount = presence.getRegionMap()==null?0:
					presence.getRegionMap().getOrDefault(""+zoneNo,0);
			if(radarWaveDeviceSettings.getRoomZones().size()<(zoneNo+1)){
				log.warn(">>>> 设备号：{},设备无{}号区域配置，忽略进出事件",presence.getDeviceNo(),zoneNo);
				return;
			}
			accessInfo.setType(LEAVE_ZONE_3);
//			int peopleCount = presence.getTrackerTargets()==null?0:
//					presence.getTrackerTargets().size();
			accessInfo.setZoneNo(zoneNo);
			accessInfo.setPeopleCount(peopleCount);
			accessInfo.setAreaName(radarWaveDeviceSettings.getRoomZones().get(zoneNo).getName());

		}else {
    		log.debug("监听到[{}], 报文对象：{} " ,"未知事件消息"  ,JSON.toJSONString(presence));
    	}

    	// 将进出消息存入redis
		String redisKey = T_DEVICE_VAYYAR_ACCESS_KEY + accessInfo.getDeviceNo()+ DateFormatUtils.format(new Date(),":yyyy-MM-dd");
		boolean hasKey = redisCache.hasKey(redisKey);
		redisCache.setCacheList(redisKey, Arrays.asList(accessInfo));
		if(!hasKey){
			// 初次设值的时候设值过期时间一天
			redisCache.expire(redisKey,2, TimeUnit.DAYS);
		}

    }

}
