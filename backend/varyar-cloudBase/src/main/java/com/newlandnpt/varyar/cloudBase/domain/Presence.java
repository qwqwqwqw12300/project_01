package com.newlandnpt.varyar.cloudBase.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.newlandnpt.varyar.common.core.domain.vo.TrackerTargetVo;

public class Presence implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** 报文:事件的时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date timestamp;
	
	/** 报文:设备id **/
	private String deviceId;
	
	/** 报文:事件时间戳 EPOCH timestamp of the state event **/
	private Long timestampMillis;
	
	/** 报文:房间内是否有人 **/
	private Boolean roomPresenceDetected;

	/**
	 * 报文:区域信息以及区域是否被占用，json字符串<br>
	 * json： "{\"0\":0,\"1\":0,\"2\":0,\"3\":0}",<br>
	 * 描述：<p>key 	  - 存在的区域,根据创建顺序排序. 第一个区域为 0. 最多可以有4个区域 (最后一个是3).<br> 
	 *		value - 如果区域被占用值为1.</p> 
	 */
	private String presenceRegionMap;
	
	/**报文: 数组 – 检测到的每个目标都是数组里的一个对象 以及其附加信息**/
	private List<TrackerTargetVo> trackerTargets;
	
	/** 自定义:根据presenceRegionMap自动转成map，方便操作 **/
	private Map<String, Integer> regionMap;

	/** 自定义:入库时间 **/
	private Date recordTime;
	
	/** 自定义: 事件类型 **/
	private String eventType;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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

	public List<TrackerTargetVo> getTrackerTargets() {
		return trackerTargets;
	}

	public void setTrackerTargets(List<TrackerTargetVo> trackerTargets) {
		this.trackerTargets = trackerTargets;
	}

	public Map<String, Integer> getRegionMap() {
		return regionMap;
	}

	public void setRegionMap(Map<String, Integer> regionMap) {
		this.regionMap = regionMap;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	/**
	 * 获取设备no
	 * @return
	 */
	public String getDeviceNo() {
		return deviceId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
}
