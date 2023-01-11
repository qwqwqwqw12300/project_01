package com.newlandnpt.varyar.cloudBase.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * 跌倒
 * @author bean
 */
public class Fall implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 自定义id，自增
	 */
	private Long id;

	/** 报文:设备id **/
	private String deviceId;
	
	/** 报文:事件id 唯一id，长度不定，字母数字组合，摔倒时间周期内一致**/
	private String eventId;
	
	/** 报文:事件的时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date timestamp;
	
	/** 报文:事件结束时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTimestamp;
	
	/** 报文:当前阶段的跌倒事件(fall_detected, fall_confirmed, calling, finished, fall_exit) **/
	private String status;
	
	/** 报文:如果发生跌倒 值为fall **/
	private String type;
	
	/** 报文:设备是否在test模式1：true 0：false **/
	private Boolean isSimulated;
	
	/** 报文:最后状态更新时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date statusUpdateTimestamp;
	
	/** 报文: 触发事件目标的json字符串，内容为：targetId，xCm，yCm，zCm字段<br>
	 * json： {\"targetId\":0,\"x_cm\":-94,\"y_cm\":175,\"z_cm\":19},<br>
	**/
	private String extra;
	
	/** 报文: 当前是否激活学习模式 1：true 0：false **/
	private Boolean isLearning;
	
	/** 报文: 是否已激活静音模式1：true 0：false 不会向看护人和面板发送警报**/
	private Boolean isSilent;
	
	/** 报文:事件时间戳 **/
	private Long timestampMillis;
	
	/** 自定义：根据extra字段获取 目标id **/
	private Integer targetId;
	
	/** 自定义：根据extra字段获取 触发坠落事件的目标的X位置**/
	private Integer xCm;
	
	/** 自定义：根据extra字段获取 触发坠落事件的目标的y位置**/
	private Integer yCm;
	
	/** 自定义：根据extra字段获取 触发坠落事件的目标的z位置**/
	private Integer zCm;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Date getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(Date endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getIsSimulated() {
		return isSimulated;
	}

	public void setIsSimulated(Boolean isSimulated) {
		this.isSimulated = isSimulated;
	}

	public Date getStatusUpdateTimestamp() {
		return statusUpdateTimestamp;
	}

	public void setStatusUpdateTimestamp(Date statusUpdateTimestamp) {
		this.statusUpdateTimestamp = statusUpdateTimestamp;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		if(!StringUtils.isBlank(extra)) {
			Map map =  JSONObject.parseObject(extra);
			this.targetId = (Integer) map.get("targetId");
			this.xCm = (Integer) map.get("x_cm");
			this.yCm = (Integer) map.get("y_cm");
			this.zCm = (Integer) map.get("z_cm");
		}
		this.extra = extra;
	}

	public Boolean getIsLearning() {
		return isLearning;
	}

	public void setIsLearning(Boolean isLearning) {
		this.isLearning = isLearning;
	}

	public Boolean getIsSilent() {
		return isSilent;
	}

	public void setIsSilent(Boolean isSilent) {
		this.isSilent = isSilent;
	}

	public Long getTimestampMillis() {
		return timestampMillis;
	}

	public void setTimestampMillis(Long timestampMillis) {
		this.timestampMillis = timestampMillis;
	}

	public Integer getTargetId() {
		return targetId;
	}

	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}

	public Integer getxCm() {
		return xCm;
	}

	public void setxCm(Integer xCm) {
		this.xCm = xCm;
	}

	public Integer getyCm() {
		return yCm;
	}

	public void setyCm(Integer yCm) {
		this.yCm = yCm;
	}

	public Integer getzCm() {
		return zCm;
	}

	public void setzCm(Integer zCm) {
		this.zCm = zCm;
	}
	
}
