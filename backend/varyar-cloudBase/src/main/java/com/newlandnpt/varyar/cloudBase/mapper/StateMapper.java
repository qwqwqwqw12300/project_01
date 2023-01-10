package com.newlandnpt.varyar.cloudBase.mapper;

import org.springframework.stereotype.Service;

import com.newlandnpt.varyar.cloudBase.domain.State;

@Service("cloud.stateMapper")
public interface StateMapper {
	
	public State selectByDeviceId(String deviceId);

	public int insert(State obj);

	public int update(State obj);

	public int deleteById(String id);

	public int deleteByIds(String[] idArr);
}