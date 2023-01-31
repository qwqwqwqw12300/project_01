package com.newlandnpt.varyar.api.controller.business.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.newlandnpt.varyar.cloudBase.constant.VayyarCloudConstants;
import com.newlandnpt.varyar.cloudBase.domain.Presence;

/**
 * MQ监听 - 进出事件
 * @author Bean
 * @date 2023-01-11
 */
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic.presence}", consumerGroup = "${rocketmq.group.presence}")
public class PresenceListener implements RocketMQListener<Presence> {

	private static final Logger log = LoggerFactory.getLogger(PresenceListener.class);
	
    @Override
    public void onMessage(Presence presence) {
    	if(VayyarCloudConstants.EVENT_ROOM_IN.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"进房间事件"  ,JSON.toJSONString(presence));	
    	}else if(VayyarCloudConstants.EVENT_ROOM_OUT.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"出房间事件"  ,JSON.toJSONString(presence));	
    	}else if(VayyarCloudConstants.EVENT_REGION0_IN.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"进0号区域事件"  ,JSON.toJSONString(presence));	
    	}else if(VayyarCloudConstants.EVENT_REGION0_OUT.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"出0号区域事件"  ,JSON.toJSONString(presence));	
    	}else if(VayyarCloudConstants.EVENT_REGION1_IN.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"进1号区域事件"  ,JSON.toJSONString(presence));	
    	}else if(VayyarCloudConstants.EVENT_REGION1_OUT.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"出1号区域事件"  ,JSON.toJSONString(presence));	
    	}else if(VayyarCloudConstants.EVENT_REGION2_IN.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"进2号区域事件"  ,JSON.toJSONString(presence));	
    	}else if(VayyarCloudConstants.EVENT_REGION2_OUT.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"出2号区域事件"  ,JSON.toJSONString(presence));	
    	}else if(VayyarCloudConstants.EVENT_REGION3_IN.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"进3号区域事件"  ,JSON.toJSONString(presence));	
    	}else if(VayyarCloudConstants.EVENT_REGION3_OUT.equals(presence.getEventType())){
    		log.debug("监听到[{}], 报文对象：{} " ,"出3号区域事件"  ,JSON.toJSONString(presence));	
    	}else {
    		log.debug("监听到[{}], 报文对象：{} " ,"未知事件消息"  ,JSON.toJSONString(presence));
    	}
    }
}
