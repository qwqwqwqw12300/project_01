package com.newlandnpt.varyar.common.core.domain.config;

import java.util.List;
import java.util.Map;

public class WalabotConfig {

	private Boolean bedExitEnabled = false;

	private Integer enterDuration = 3;
	
	private Integer exitDuration = 10;
	
	private Integer fallingSensitivity = 1;
	
	private Integer maxTargetsForFallingTrigger = 0;
	
	private Float sensorHeight = 1.5f;
	
	private Integer sensorMounting = 0;
	
	/**
	 * xMax": 0,
      "xMin": 0,
      "yMax": 0,
      "yMin": 0
	 */
	private Map<String, Integer> thresholdRegion;
	
	private List<SubRegion> trackerSubRegions;
	
	private Float xMax;
	
	private Float xMin;
	
	private Float yMax;
	
	private Float yMin;
	
	private Float zMax;
	
	private Float zMin;

	public Boolean getBedExitEnabled() {
		return bedExitEnabled;
	}

	public void setBedExitEnabled(Boolean bedExitEnabled) {
		this.bedExitEnabled = bedExitEnabled;
	}

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

	public Integer getFallingSensitivity() {
		return fallingSensitivity;
	}

	public void setFallingSensitivity(Integer fallingSensitivity) {
		this.fallingSensitivity = fallingSensitivity;
	}

	public Integer getMaxTargetsForFallingTrigger() {
		return maxTargetsForFallingTrigger;
	}

	public void setMaxTargetsForFallingTrigger(Integer maxTargetsForFallingTrigger) {
		this.maxTargetsForFallingTrigger = maxTargetsForFallingTrigger;
	}

	public Float getSensorHeight() {
		return sensorHeight;
	}

	public void setSensorHeight(Float sensorHeight) {
		this.sensorHeight = sensorHeight;
	}

	public Integer getSensorMounting() {
		return sensorMounting;
	}

	public void setSensorMounting(Integer sensorMounting) {
		this.sensorMounting = sensorMounting;
	}

	public Map<String, Integer> getThresholdRegion() {
		return thresholdRegion;
	}

	public void setThresholdRegion(Map<String, Integer> thresholdRegion) {
		this.thresholdRegion = thresholdRegion;
	}

	public List<SubRegion> getTrackerSubRegions() {
		return trackerSubRegions;
	}

	public void setTrackerSubRegions(List<SubRegion> trackerSubRegions) {
		this.trackerSubRegions = trackerSubRegions;
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
