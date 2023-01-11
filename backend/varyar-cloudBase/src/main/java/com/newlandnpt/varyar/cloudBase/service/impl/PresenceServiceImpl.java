package com.newlandnpt.varyar.cloudBase.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.newlandnpt.varyar.cloudBase.domain.Presence;
import com.newlandnpt.varyar.cloudBase.service.PresenceService;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.utils.spring.SpringUtils;


@Service("cloud.presenceService")
public class PresenceServiceImpl implements PresenceService {
	
	public static final String CACHE_KEY = "cloud:presence:";
	
	@Override
	public void receve(Presence t) {
		String deviceId = t.getDeviceId();
		if(StringUtils.isBlank(deviceId)) {
			return ;
		}
	
		SpringUtils.getBean(RedisCache.class).setCacheObject(CACHE_KEY + t.getDeviceId(), t);
	}

}
