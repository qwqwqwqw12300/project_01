package com.newlandnpt.varyar.cloudBase.domain.device;

public class AppConfig {

	private Integer confirmedToAlertTimeoutSec;
	
	private Boolean enableAnalytics;

	public Integer getConfirmedToAlertTimeoutSec() {
		return confirmedToAlertTimeoutSec;
	}

	public void setConfirmedToAlertTimeoutSec(Integer confirmedToAlertTimeoutSec) {
		this.confirmedToAlertTimeoutSec = confirmedToAlertTimeoutSec;
	}

	public Boolean getEnableAnalytics() {
		return enableAnalytics;
	}

	public void setEnableAnalytics(Boolean enableAnalytics) {
		this.enableAnalytics = enableAnalytics;
	}
	
	
}
