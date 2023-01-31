package com.newlandnpt.varyar.common.core.domain.config;

import java.io.Serializable;

public class DeviceConfig implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** 自定义 设备id 根据设备id下发参数 **/
	private String deviceId;
	
	private String direction = "downstream";
	
	private AppConfig appConfig;
	
	private WalabotConfig walabotConfig;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public AppConfig getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

	public WalabotConfig getWalabotConfig() {
		return walabotConfig;
	}

	public void setWalabotConfig(WalabotConfig walabotConfig) {
		this.walabotConfig = walabotConfig;
	}
	
	
}
