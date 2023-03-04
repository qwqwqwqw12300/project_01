package com.newlandnpt.varyar.cloudBase.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class State implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** 报文:设备id **/
	private String deviceId;

	/** 报文:状态事件的时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date timestamp;

	/** 报文:自上次重新启动或激活后的时间(毫秒) **/
	private Long upTime;

	/** 报文:wifi强度 **/
	private Integer wifiRssi;

	/** 自定义：记录时间 **/
	private Date recordTime;
	
	/** 自定义：首次上送时间 **/
	private Date firstTime;
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Long getUpTime() {
		return upTime;
	}

	public void setUpTime(Long upTime) {
		this.upTime = upTime;
	}

	public Integer getWifiRssi() {
		return wifiRssi;
	}

	public void setWifiRssi(Integer wifiRssi) {
		this.wifiRssi = wifiRssi;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	public Date getFirstTime() {
		return firstTime;
	}

	public void setFirstTime(Date firstTime) {
		this.firstTime = firstTime;
	}
	
	/**
	 * 获取设备no
	 * @return
	 */
	public String getDeviceNo() {
		return deviceId;
	}

}
