package com.newlandnpt.varyar.cloudBase.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.newlandnpt.varyar.cloudBase.domain.State;
import com.newlandnpt.varyar.cloudBase.mapper.StateMapper;
import com.newlandnpt.varyar.cloudBase.service.StateService;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.utils.spring.SpringUtils;


@Service("cloud.stateService")
public class StateServiceImpl implements StateService {
	
	@Resource(name = "cloud.stateMapper")
	private StateMapper stateMapper;

	public static final String CACHE_KEY = "cloud:state:";
	
	@Override
	public void receve(State t) {
		String deviceId = t.getDeviceId();
		if(StringUtils.isBlank(deviceId)) {
			return ;
		}
//		Device device = deviceMapper.selectByDeviceId(deviceId);
//		if(device == null) {
//			return ;
//		}
		State obj = stateMapper.selectByDeviceId(deviceId);
		if(obj == null) {
			stateMapper.insert(t);
		} else {
			stateMapper.update(t);
		}
		SpringUtils.getBean(RedisCache.class).setCacheObject(CACHE_KEY + t.getDeviceId(), t);
	}

}
