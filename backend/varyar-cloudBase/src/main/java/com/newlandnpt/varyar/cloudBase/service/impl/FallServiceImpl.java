package com.newlandnpt.varyar.cloudBase.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.newlandnpt.varyar.cloudBase.domain.Fall;
import com.newlandnpt.varyar.cloudBase.mapper.FallMapper;
import com.newlandnpt.varyar.cloudBase.service.FallService;


@Service("cloud.fallService")
public class FallServiceImpl implements FallService {
	
	@Resource(name = "cloud.fallMapper")
	private FallMapper fallMapper;
	
	@Override
	public void receve(Fall t) {
		String deviceId = t.getDeviceId();
		if(StringUtils.isBlank(deviceId)) {
			return ;
		}
//		Device device = deviceMapper.selectByDeviceId(deviceId);
//		if(device == null) {
//			return ;
//		}
		fallMapper.insert(t);
	}

}
