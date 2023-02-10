package com.newlandnpt.varyar.cloudBase.service.impl;

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

import com.newlandnpt.varyar.cloudBase.domain.Fall;
import com.newlandnpt.varyar.cloudBase.service.FallService;
import com.newlandnpt.varyar.common.core.domain.vo.ExtraVo;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.utils.spring.SpringUtils;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.IDeviceService;


@Service("cloud.fallService")
public class FallServiceImpl implements FallService {
	
	private static final Logger log = LoggerFactory.getLogger(FallServiceImpl.class);

	@Autowired
	private IDeviceService tDeviceService;
	
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    
    @Value("${rocketmq.topic.fall}")
    private String fallTopic;
    
    private String CacheKey = com.newlandnpt.varyar.common.constant.CacheConstants.TARGET_LOCATION_FALL_KEY;
	
	@Override
	public void receve(Fall t) {
		String deviceId = t.getDeviceId();
		if(StringUtils.isBlank(deviceId)) {
			return ;
		}
		TDevice device = tDeviceService.loadDeviceFromCacheByNo(deviceId);
		if(device == null){
			return ;
		}
		SendResult result = rocketMQTemplate.syncSend(fallTopic, MessageBuilder.withPayload(t).build());
    	//System.out.println(JSON.toJSONString(result));
        if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
            log.error("MQ推送失败：{}", "摔倒事件");
        }
        ExtraVo extraVo = t.getExtraVo();
        if(extraVo == null){
        	return ;
        }
        if(extraVo.getxCm() == null || extraVo.getyCm() == null || extraVo.getzCm() == null){
        	return ;
        }
        String xCm = extraVo.getxCm().toString();
        String yCm = extraVo.getyCm().toString();
        String zCm = extraVo.getzCm().toString();
        String extraVoCacheKey = CacheKey + deviceId + ":" + xCm+yCm+zCm;
        ExtraVo extraVoCache = SpringUtils.getBean(RedisCache.class).getCacheObject(extraVoCacheKey);
        if("finished".equalsIgnoreCase(t.getStatus()) || "fall_exit".equalsIgnoreCase(t.getStatus())){
        	SpringUtils.getBean(RedisCache.class).deleteObject(extraVoCacheKey);
        }else if("calling".equalsIgnoreCase(t.getStatus())){
        	//确认跌倒才记录
        	//如果redis开关开启，则记录,1分钟超时
        	Boolean targetLocation = SpringUtils.getBean(RedisCache.class).getCacheObject(com.newlandnpt.varyar.common.constant.CacheConstants.TARGET_LOCATION_SWITCH_KEY + deviceId);
        	if(targetLocation != null && targetLocation){
        		SpringUtils.getBean(RedisCache.class).setCacheObject(extraVoCacheKey, extraVoCache, 1, TimeUnit.MINUTES);
        	}
        }
        
        /*
        Map<Integer, ExtraVo> map = SpringUtils.getBean(RedisCache.class).getCacheObject(CacheKey + deviceId);
        if(map == null || map.isEmpty()){
        	map = new LinkedHashMap<Integer, ExtraVo>();
        }
        if("finished".equalsIgnoreCase(t.getStatus()) || "fall_exit".equalsIgnoreCase(t.getStatus())){
        	map.remove(extraVo.getTargetId());
        	if(map == null || map.isEmpty()){
        		SpringUtils.getBean(RedisCache.class).deleteObject(CacheKey + deviceId);
            }else {
            	SpringUtils.getBean(RedisCache.class).setCacheObject(CacheKey + deviceId, map);
            }
        }else if("calling".equalsIgnoreCase(t.getStatus())){
        	//确认跌倒才记录
        	//如果redis开关开启，则记录
        	Boolean targetLocation = SpringUtils.getBean(RedisCache.class).getCacheObject(com.newlandnpt.varyar.common.constant.CacheConstants.TARGET_LOCATION_SWITCH_KEY + deviceId);
        	if(targetLocation != null && targetLocation){
        		Integer targetId = extraVo.getTargetId();
        		map.put(targetId, extraVo);
        		SpringUtils.getBean(RedisCache.class).setCacheObject(CacheKey + deviceId, map);
        	}
        }
        */
	}

}
