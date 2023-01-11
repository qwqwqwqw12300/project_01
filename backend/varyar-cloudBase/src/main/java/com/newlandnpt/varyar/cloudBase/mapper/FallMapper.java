package com.newlandnpt.varyar.cloudBase.mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.newlandnpt.varyar.cloudBase.domain.Fall;

@Service("cloud.fallMapper")
public interface FallMapper {
	
	public List<Fall> selectByDeviceId(String deviceId);

	public int insert(Fall obj);

	public int deleteById(String id);

}