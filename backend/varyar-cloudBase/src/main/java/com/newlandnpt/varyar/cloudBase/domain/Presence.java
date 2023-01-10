package com.newlandnpt.varyar.cloudBase.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson2.JSONObject;
import com.newlandnpt.varyar.cloudBase.domain.vo.TrackerTargetVo;

public class Presence implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 自定义id
	 */
	private Long id;

	private Date timestamp;
	
	private Long timestampMillis;
	
	private Boolean roomPresenceDetected;
	
	private String deviceId;

	private String presenceRegionMap;
	
	private Map<String, Integer> regionMap;
	
	private List<TrackerTargetVo> trackerTargets;
	
	private Date recordTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Long getTimestampMillis() {
		return timestampMillis;
	}

	public void setTimestampMillis(Long timestampMillis) {
		this.timestampMillis = timestampMillis;
	}

	public Boolean getRoomPresenceDetected() {
		return roomPresenceDetected;
	}

	public void setRoomPresenceDetected(Boolean roomPresenceDetected) {
		this.roomPresenceDetected = roomPresenceDetected;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

//	public Map<String, Integer> getPresenceRegionMap() {
//		return presenceRegionMap;
//	}
//
//	public void setPresenceRegionMap(Map<String, Integer> presenceRegionMap) {
//		this.presenceRegionMap = presenceRegionMap;
//	}

	public List<TrackerTargetVo> getTrackerTargets() {
		return trackerTargets;
	}

	public String getPresenceRegionMap() {
		return presenceRegionMap;
	}

	public void setPresenceRegionMap(String presenceRegionMap) {
		if(!StringUtils.isBlank(presenceRegionMap)) {
			Map map= JSONObject.parseObject(presenceRegionMap);
			this.regionMap = map;
		}
		this.presenceRegionMap = presenceRegionMap;
	}

	public Map<String, Integer> getRegionMap() {
		return regionMap;
	}

	public void setRegionMap(Map<String, Integer> regionMap) {
		this.regionMap = regionMap;
	}

	public void setTrackerTargets(List<TrackerTargetVo> trackerTargets) {
		this.trackerTargets = trackerTargets;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

}
