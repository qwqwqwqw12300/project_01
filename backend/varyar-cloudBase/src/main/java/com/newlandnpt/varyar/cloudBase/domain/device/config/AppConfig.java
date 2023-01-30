package com.newlandnpt.varyar.cloudBase.domain.device.config;

import java.util.Map;

public class AppConfig {

	private Integer confirmedToAlertTimeoutSec = 40;
	
	private Map<String, DryContacts> dryContacts;
	
	private Boolean enableAnalytics = true;
	
	private Long learningModeEndTs;
	
	private Long learningModeStartTs;
	
	private Integer ledMode = 1;
	
	private Integer logLevel = 1;
	
	private Boolean otaEnabled = true;
	
	private Integer presenceReportMinRateMills = 30000;
	
	private Boolean reportFallsToMqtt = true;
	
	private Boolean reportPresenceToMqtt = true;
	
	private Boolean silentMode = true;
	
	private Boolean subscriptionExpired = false;
	
	private Float targetPositionChangeThresholdMeters;
	
	private Boolean telemetryEnabled = false;
	
	private Integer telemetryPolicy = 0;
	
	private Integer telemetryTransport = 1;
	
	private Integer volume = 50;

	public Integer getConfirmedToAlertTimeoutSec() {
		return confirmedToAlertTimeoutSec;
	}

	public void setConfirmedToAlertTimeoutSec(Integer confirmedToAlertTimeoutSec) {
		this.confirmedToAlertTimeoutSec = confirmedToAlertTimeoutSec;
	}

	public Map<String, DryContacts> getDryContacts() {
		return dryContacts;
	}

	public void setDryContacts(Map<String, DryContacts> dryContacts) {
		this.dryContacts = dryContacts;
	}

	public Boolean getEnableAnalytics() {
		return enableAnalytics;
	}

	public void setEnableAnalytics(Boolean enableAnalytics) {
		this.enableAnalytics = enableAnalytics;
	}

	public Long getLearningModeEndTs() {
		return learningModeEndTs;
	}

	public void setLearningModeEndTs(Long learningModeEndTs) {
		this.learningModeEndTs = learningModeEndTs;
	}

	public Long getLearningModeStartTs() {
		return learningModeStartTs;
	}

	public void setLearningModeStartTs(Long learningModeStartTs) {
		this.learningModeStartTs = learningModeStartTs;
	}

	public Integer getLedMode() {
		return ledMode;
	}

	public void setLedMode(Integer ledMode) {
		this.ledMode = ledMode;
	}

	public Integer getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(Integer logLevel) {
		this.logLevel = logLevel;
	}

	public Boolean getOtaEnabled() {
		return otaEnabled;
	}

	public void setOtaEnabled(Boolean otaEnabled) {
		this.otaEnabled = otaEnabled;
	}

	public Integer getPresenceReportMinRateMills() {
		return presenceReportMinRateMills;
	}

	public void setPresenceReportMinRateMills(Integer presenceReportMinRateMills) {
		this.presenceReportMinRateMills = presenceReportMinRateMills;
	}

	public Boolean getReportFallsToMqtt() {
		return reportFallsToMqtt;
	}

	public void setReportFallsToMqtt(Boolean reportFallsToMqtt) {
		this.reportFallsToMqtt = reportFallsToMqtt;
	}

	public Boolean getReportPresenceToMqtt() {
		return reportPresenceToMqtt;
	}

	public void setReportPresenceToMqtt(Boolean reportPresenceToMqtt) {
		this.reportPresenceToMqtt = reportPresenceToMqtt;
	}

	public Boolean getSilentMode() {
		return silentMode;
	}

	public void setSilentMode(Boolean silentMode) {
		this.silentMode = silentMode;
	}

	public Boolean getSubscriptionExpired() {
		return subscriptionExpired;
	}

	public void setSubscriptionExpired(Boolean subscriptionExpired) {
		this.subscriptionExpired = subscriptionExpired;
	}

	public Float getTargetPositionChangeThresholdMeters() {
		return targetPositionChangeThresholdMeters;
	}

	public void setTargetPositionChangeThresholdMeters(Float targetPositionChangeThresholdMeters) {
		this.targetPositionChangeThresholdMeters = targetPositionChangeThresholdMeters;
	}

	public Boolean getTelemetryEnabled() {
		return telemetryEnabled;
	}

	public void setTelemetryEnabled(Boolean telemetryEnabled) {
		this.telemetryEnabled = telemetryEnabled;
	}

	public Integer getTelemetryPolicy() {
		return telemetryPolicy;
	}

	public void setTelemetryPolicy(Integer telemetryPolicy) {
		this.telemetryPolicy = telemetryPolicy;
	}

	public Integer getTelemetryTransport() {
		return telemetryTransport;
	}

	public void setTelemetryTransport(Integer telemetryTransport) {
		this.telemetryTransport = telemetryTransport;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	
}
