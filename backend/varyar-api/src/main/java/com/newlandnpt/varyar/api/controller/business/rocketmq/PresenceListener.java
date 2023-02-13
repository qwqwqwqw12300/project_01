package com.newlandnpt.varyar.api.controller.business.rocketmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newlandnpt.varyar.cloudBase.constant.VayyarCloudConstants;
import com.newlandnpt.varyar.cloudBase.domain.Presence;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.base.BaseException;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.IDeviceService;
import org.apache.commons.lang3.SystemUtils;
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

import java.util.Calendar;
import java.util.Collection;

import static com.newlandnpt.varyar.api.controller.business.rocketmq.AccessListener.*;
import static com.newlandnpt.varyar.common.constant.CacheConstants.T_DEVICE_VAYYAR_ACCESS_KEY;
import static com.newlandnpt.varyar.system.domain.TRoomZone.FLAG_YES;

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

    	if(VayyarCloudConstants.EVENT_ROOM_IN.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"进房间事件"  ,JSON.toJSONString(presence));

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("devId",presence.getDeviceId());
			jsonObject.put("type",ENTER_ROOM);
			int peopleCount = presence.getTrackerTargets()==null?0:
					presence.getTrackerTargets().size();
			jsonObject.put("peopleCount",peopleCount);
			jsonObject.put("time",presence.getTimestampMillis());
			Calendar timeStampCalendar = Calendar.getInstance();
			timeStampCalendar.setTime(presence.getTimestamp());
			for(int i = peopleCount-1;i>=0;i++){
				String redisKey = T_DEVICE_VAYYAR_ACCESS_KEY + presence.getDeviceNo()+":"+VayyarCloudConstants.EVENT_ROOM_OUT+":"+i+":*";
				Collection<String> keys = redisCache.keys(redisKey);
				redisCache.deleteObject(keys);
			}

			TDevice device = deviceService.selectByDeviceNo(presence.getDeviceNo());
			if(device == null){
				log.warn(">>>> 设备号：{},获取不到设备，忽略进房间事件",presence.getDeviceNo());
				return;
			}
			if(device.getParameter() == null || !(device.getParameter() instanceof TDevice.RadarWaveDeviceSettings)){
				log.warn(">>>> 设备号：{},获取不到设备雷达波配置信息，忽略进房间事件",presence.getDeviceNo());
				return;
			}
			TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)device.getParameter();
			if(!FLAG_YES.equals(radarWaveDeviceSettings.getDeviceRoomParameter().getInMonitorFlag())){
				log.warn(">>>> 设备号：{},设备未开启进入监控，忽略进房间事件",presence.getDeviceNo());
				return;
			}

			if((radarWaveDeviceSettings.getDeviceRoomParameter().getStartTime()!=null)){
				Calendar startTimeCalendar = Calendar.getInstance();
				startTimeCalendar.setTime(radarWaveDeviceSettings.getDeviceRoomParameter().getStartTime());

				startTimeCalendar.set(Calendar.YEAR,timeStampCalendar.get(Calendar.YEAR));
				startTimeCalendar.set(Calendar.MONTH,timeStampCalendar.get(Calendar.MONTH));
				startTimeCalendar.set(Calendar.DAY_OF_MONTH,timeStampCalendar.get(Calendar.DAY_OF_MONTH));

				if(startTimeCalendar.getTimeInMillis()<presence.getTimestampMillis()){
					log.info(">>>> 设备号：{},设备开启监控时间{},事件触发时间{}，未在监控时间范围内，忽略进房间事件",presence.getDeviceNo()
							,radarWaveDeviceSettings.getDeviceRoomParameter().getStartTime(),presence.getTimestamp());
					return;
				}
			}
			if((radarWaveDeviceSettings.getDeviceRoomParameter().getEndTime()!=null)){
				Calendar endTimeCalendar = Calendar.getInstance();
				endTimeCalendar.setTime(radarWaveDeviceSettings.getDeviceRoomParameter().getEndTime());

				endTimeCalendar.set(Calendar.YEAR,timeStampCalendar.get(Calendar.YEAR));
				endTimeCalendar.set(Calendar.MONTH,timeStampCalendar.get(Calendar.MONTH));
				endTimeCalendar.set(Calendar.DAY_OF_MONTH,timeStampCalendar.get(Calendar.DAY_OF_MONTH));

				if(endTimeCalendar.getTimeInMillis()>presence.getTimestampMillis()){
					log.info(">>>> 设备号：{},设备结束监控时间{},事件触发时间{}，未在监控时间范围内，忽略进房间事件",presence.getDeviceNo()
							,radarWaveDeviceSettings.getDeviceRoomParameter().getEndTime(),presence.getTimestamp());
					return;
				}
			}

			jsonObject.put("delayTime",radarWaveDeviceSettings.getDeviceRoomParameter().getEntryTime());

			result = rocketMQTemplate.syncSend(accessTopic, MessageBuilder.withPayload(jsonObject).build());

		}else if(VayyarCloudConstants.EVENT_ROOM_OUT.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"出房间事件"  ,JSON.toJSONString(presence));
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("devId",presence.getDeviceId());
			jsonObject.put("type",LEAVE_ROOM);
			int peopleCount = presence.getTrackerTargets()==null?0:
					presence.getTrackerTargets().size();
			jsonObject.put("peopleCount",peopleCount);
			jsonObject.put("time",presence.getTimestampMillis());
			Calendar timeStampCalendar = Calendar.getInstance();
			timeStampCalendar.setTime(presence.getTimestamp());

			for(int i = peopleCount-1;i>=0;i++){
				String redisKey = T_DEVICE_VAYYAR_ACCESS_KEY + presence.getDeviceNo()+":"+VayyarCloudConstants.EVENT_ROOM_IN+":"+i+":*";
				Collection<String> keys = redisCache.keys(redisKey);
				redisCache.deleteObject(keys);
			}

			TDevice device = deviceService.selectByDeviceNo(presence.getDeviceNo());
			if(device == null){
				log.warn(">>>> 设备号：{},获取不到设备，忽略进房间事件",presence.getDeviceNo());
				return;
			}
			if(device.getParameter() == null || !(device.getParameter() instanceof TDevice.RadarWaveDeviceSettings)){
				log.warn(">>>> 设备号：{},获取不到设备雷达波配置信息，忽略进房间事件",presence.getDeviceNo());
				return;
			}
			TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)device.getParameter();
			if(!FLAG_YES.equals(radarWaveDeviceSettings.getDeviceRoomParameter().getOutMonitorFlag())){
				log.warn(">>>> 设备号：{},设备未开启离开监控，忽略离开房间事件",presence.getDeviceNo());
				return;
			}



			if((radarWaveDeviceSettings.getDeviceRoomParameter().getStartTime()!=null)){
				Calendar startTimeCalendar = Calendar.getInstance();
				startTimeCalendar.setTime(radarWaveDeviceSettings.getDeviceRoomParameter().getStartTime());

				startTimeCalendar.set(Calendar.YEAR,timeStampCalendar.get(Calendar.YEAR));
				startTimeCalendar.set(Calendar.MONTH,timeStampCalendar.get(Calendar.MONTH));
				startTimeCalendar.set(Calendar.DAY_OF_MONTH,timeStampCalendar.get(Calendar.DAY_OF_MONTH));

				if(startTimeCalendar.getTimeInMillis()<presence.getTimestampMillis()){
					log.info(">>>> 设备号：{},设备开启监控时间{},事件触发时间{}，未在监控时间范围内，忽略离开房间事件",presence.getDeviceNo()
							,radarWaveDeviceSettings.getDeviceRoomParameter().getStartTime(),presence.getTimestamp());
					return;
				}
			}
			if((radarWaveDeviceSettings.getDeviceRoomParameter().getEndTime()!=null)){
				Calendar endTimeCalendar = Calendar.getInstance();
				endTimeCalendar.setTime(radarWaveDeviceSettings.getDeviceRoomParameter().getEndTime());

				endTimeCalendar.set(Calendar.YEAR,timeStampCalendar.get(Calendar.YEAR));
				endTimeCalendar.set(Calendar.MONTH,timeStampCalendar.get(Calendar.MONTH));
				endTimeCalendar.set(Calendar.DAY_OF_MONTH,timeStampCalendar.get(Calendar.DAY_OF_MONTH));

				if(endTimeCalendar.getTimeInMillis()>presence.getTimestampMillis()){
					log.info(">>>> 设备号：{},设备结束监控时间{},事件触发时间{}，未在监控时间范围内，忽略离开房间事件",presence.getDeviceNo()
							,radarWaveDeviceSettings.getDeviceRoomParameter().getEndTime(),presence.getTimestamp());
					return;
				}
			}

			jsonObject.put("delayTime",radarWaveDeviceSettings.getDeviceRoomParameter().getDepartureTime());

			result = rocketMQTemplate.syncSend(accessTopic, MessageBuilder.withPayload(jsonObject).build());

		}else if(VayyarCloudConstants.EVENT_REGION0_IN.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"进0号区域事件"  ,JSON.toJSONString(presence));
			result = sendZoneEvent(presence,ENTER_ZONE_0,0,ZoneInOut.in);
		}else if(VayyarCloudConstants.EVENT_REGION0_OUT.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"出0号区域事件"  ,JSON.toJSONString(presence));
			result = sendZoneEvent(presence,LEAVE_ZONE_0,0,ZoneInOut.out);
    	}else if(VayyarCloudConstants.EVENT_REGION1_IN.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"进1号区域事件"  ,JSON.toJSONString(presence));
			result = sendZoneEvent(presence,ENTER_ZONE_1,1,ZoneInOut.in);
    	}else if(VayyarCloudConstants.EVENT_REGION1_OUT.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"出1号区域事件"  ,JSON.toJSONString(presence));
			result = sendZoneEvent(presence,LEAVE_ZONE_1,1,ZoneInOut.out);
    	}else if(VayyarCloudConstants.EVENT_REGION2_IN.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"进2号区域事件"  ,JSON.toJSONString(presence));
			result = sendZoneEvent(presence,ENTER_ZONE_2,2,ZoneInOut.in);
    	}else if(VayyarCloudConstants.EVENT_REGION2_OUT.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"出2号区域事件"  ,JSON.toJSONString(presence));
			result = sendZoneEvent(presence,LEAVE_ZONE_2,2,ZoneInOut.out);
    	}else if(VayyarCloudConstants.EVENT_REGION3_IN.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"进3号区域事件"  ,JSON.toJSONString(presence));
			result = sendZoneEvent(presence,ENTER_ZONE_3,3,ZoneInOut.in);
    	}else if(VayyarCloudConstants.EVENT_REGION3_OUT.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"出3号区域事件"  ,JSON.toJSONString(presence));
			result = sendZoneEvent(presence,LEAVE_ZONE_3,3,ZoneInOut.out);
		}else {
    		log.debug("监听到[{}], 报文对象：{} " ,"未知事件消息"  ,JSON.toJSONString(presence));
    	}
    	if(result!=null){
			log.debug(" 发送消息结果： " + result);
			log.debug(" 发送时间： " + System.currentTimeMillis());
			if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
				throw new BaseException("发送失败");
			}
		}
    }


    private SendResult sendZoneEvent(Presence presence,String type,int zoneNo,ZoneInOut zoneInOut){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("devId",presence.getDeviceId());
		jsonObject.put("type",type);
		int peopleCount = presence.getRegionMap()==null?0:
				presence.getRegionMap().getOrDefault(""+zoneNo,0);
		jsonObject.put("peopleCount",peopleCount);
		jsonObject.put("time",presence.getTimestampMillis());
		Calendar timeStampCalendar = Calendar.getInstance();
		timeStampCalendar.setTime(presence.getTimestamp());


		//清除缓存
		switch (zoneInOut){
			case in:
				for(int i = peopleCount-1;i>=0;i++){
					String redisKey = T_DEVICE_VAYYAR_ACCESS_KEY + presence.getDeviceNo()+":leave"+type.substring(5)+":"+i+":*";
					Collection<String> keys = redisCache.keys(redisKey);
					redisCache.deleteObject(keys);
				}
				break;
			case out:
				for(int i = peopleCount-1;i>=0;i++){
					String redisKey = T_DEVICE_VAYYAR_ACCESS_KEY + presence.getDeviceNo()+":enter"+type.substring(5)+":"+i+":*";
					Collection<String> keys = redisCache.keys(redisKey);
					redisCache.deleteObject(keys);
				}
				break;
		}

		TDevice device = deviceService.selectByDeviceNo(presence.getDeviceNo());
		if(device == null){
			log.warn(">>>> 设备号：{},获取不到设备，忽略{}{}号区域事件",presence.getDeviceNo(),zoneInOut,zoneNo);
			return null;
		}
		if(device.getParameter() == null || !(device.getParameter() instanceof TDevice.RadarWaveDeviceSettings)){
			log.warn(">>>> 设备号：{},获取不到设备雷达波配置信息，忽略{}{}号区域事件",presence.getDeviceNo(),zoneInOut,zoneNo);
			return null;
		}
		TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)device.getParameter();

		if(radarWaveDeviceSettings.getRoomZones() == null){
			log.warn(">>>> 设备号：{},设备未开启进入监控，忽略{}{}号区域事件",presence.getDeviceNo(),zoneInOut,zoneNo);
			return null;
		}

		switch (zoneInOut){
			case in:
				if(radarWaveDeviceSettings.getRoomZones().size()<(zoneNo+1)||
						!FLAG_YES.equals(radarWaveDeviceSettings.getRoomZones().get(zoneNo).getInMonitorFlag())){
					log.warn(">>>> 设备号：{},设备未开启进入监控，忽略{}{}号区域事件",presence.getDeviceNo(),zoneInOut,zoneNo);
					return null;
				}
				jsonObject.put("zoneId",radarWaveDeviceSettings.getRoomZones().get(zoneNo).getRoomZoneId());
				jsonObject.put("delayTime",radarWaveDeviceSettings.getRoomZones().get(zoneNo).getEntryTime());
				break;
			case out:
				if(radarWaveDeviceSettings.getRoomZones().size()<(zoneNo+1)||
						!FLAG_YES.equals(radarWaveDeviceSettings.getRoomZones().get(zoneNo).getOutMonitorFlag())){
					log.warn(">>>> 设备号：{},设备未开启进入监控，忽略{}{}号区域事件",presence.getDeviceNo(),zoneInOut,zoneNo);
					return null;
				}
				jsonObject.put("zoneId",radarWaveDeviceSettings.getRoomZones().get(zoneNo).getRoomZoneId());
				jsonObject.put("delayTime",radarWaveDeviceSettings.getRoomZones().get(zoneNo).getDepartureTime());

				break;
			default:
				throw new IllegalArgumentException("未适配的zoneInOut枚举类型："+zoneInOut);
		}


		if((radarWaveDeviceSettings.getDeviceRoomParameter().getStartTime()!=null)){
			Calendar startTimeCalendar = Calendar.getInstance();
			startTimeCalendar.setTime(radarWaveDeviceSettings.getDeviceRoomParameter().getStartTime());

			startTimeCalendar.set(Calendar.YEAR,timeStampCalendar.get(Calendar.YEAR));
			startTimeCalendar.set(Calendar.MONTH,timeStampCalendar.get(Calendar.MONTH));
			startTimeCalendar.set(Calendar.DAY_OF_MONTH,timeStampCalendar.get(Calendar.DAY_OF_MONTH));

			if(startTimeCalendar.getTimeInMillis()<presence.getTimestampMillis()){
				log.info(">>>> 设备号：{},设备开启监控时间{},事件触发时间{}，未在监控时间范围内，忽略{}{}号区域事件",presence.getDeviceNo()
						,radarWaveDeviceSettings.getDeviceRoomParameter().getStartTime(),presence.getTimestamp()
						,zoneInOut,zoneNo);
				return null;
			}
		}
		if((radarWaveDeviceSettings.getDeviceRoomParameter().getEndTime()!=null)){
			Calendar endTimeCalendar = Calendar.getInstance();
			endTimeCalendar.setTime(radarWaveDeviceSettings.getDeviceRoomParameter().getEndTime());

			endTimeCalendar.set(Calendar.YEAR,timeStampCalendar.get(Calendar.YEAR));
			endTimeCalendar.set(Calendar.MONTH,timeStampCalendar.get(Calendar.MONTH));
			endTimeCalendar.set(Calendar.DAY_OF_MONTH,timeStampCalendar.get(Calendar.DAY_OF_MONTH));

			if(endTimeCalendar.getTimeInMillis()>presence.getTimestampMillis()){
				log.info(">>>> 设备号：{},设备结束监控时间{},事件触发时间{}，未在监控时间范围内，忽略{}{}号区域事件",presence.getDeviceNo()
						,radarWaveDeviceSettings.getDeviceRoomParameter().getEndTime(),presence.getTimestamp()
						,zoneInOut,zoneNo);
				return null;
			}
		}

		return rocketMQTemplate.syncSend(accessTopic, MessageBuilder.withPayload(jsonObject).build());
	}

	private enum ZoneInOut{
    	in,
		out
	}
}
