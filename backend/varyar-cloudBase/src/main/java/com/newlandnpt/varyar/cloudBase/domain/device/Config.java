package com.newlandnpt.varyar.cloudBase.domain.device;

import java.io.Serializable;

public class Config implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String direction;
	
	private AppConfig appConfig;
	
	private WalabotConfig walabotConfig;

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
