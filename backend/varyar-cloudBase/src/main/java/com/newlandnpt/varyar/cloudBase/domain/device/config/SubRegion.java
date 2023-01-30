package com.newlandnpt.varyar.cloudBase.domain.device.config;

/**
 * 子区域
 * @author Bean
 * @date 2023-01-30
 */
public class SubRegion {

	/** 进入时间 秒 **/
	private Integer enterDuration = 3;
	
	/** 离开时间 秒 **/
	private Integer exitDuration = 10;
	
	/** 存在监测 **/
	private Boolean isFallingDetection = true;
	
	/** 跌倒监测 **/
	private Boolean isPresenceDetection = true;
	
	private Integer mode = 0;
	
	/** 子区域名称 **/
	private String name;
	
	/** 最右测的点 **/
	private Float xMax;
	
	/** 最左测的点 **/
	private Float xMin;
	
	/** 最远的点 **/
	private Float yMax;
	
	/** 最近的点 **/
	private Float yMin;
	
	/** 最顶部的点 **/
	private Float zMax;
	
	/** 最底部的点 **/
	private Float zMin;

	public Integer getEnterDuration() {
		return enterDuration;
	}

	public void setEnterDuration(Integer enterDuration) {
		this.enterDuration = enterDuration;
	}

	public Integer getExitDuration() {
		return exitDuration;
	}

	public void setExitDuration(Integer exitDuration) {
		this.exitDuration = exitDuration;
	}

	public Boolean getIsFallingDetection() {
		return isFallingDetection;
	}

	public void setIsFallingDetection(Boolean isFallingDetection) {
		this.isFallingDetection = isFallingDetection;
	}

	public Boolean getIsPresenceDetection() {
		return isPresenceDetection;
	}

	public void setIsPresenceDetection(Boolean isPresenceDetection) {
		this.isPresenceDetection = isPresenceDetection;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getxMax() {
		return xMax;
	}

	public void setxMax(Float xMax) {
		this.xMax = xMax;
	}

	public Float getxMin() {
		return xMin;
	}

	public void setxMin(Float xMin) {
		this.xMin = xMin;
	}

	public Float getyMax() {
		return yMax;
	}

	public void setyMax(Float yMax) {
		this.yMax = yMax;
	}

	public Float getyMin() {
		return yMin;
	}

	public void setyMin(Float yMin) {
		this.yMin = yMin;
	}

	public Float getzMax() {
		return zMax;
	}

	public void setzMax(Float zMax) {
		this.zMax = zMax;
	}

	public Float getzMin() {
		return zMin;
	}

	public void setzMin(Float zMin) {
		this.zMin = zMin;
	}
}
