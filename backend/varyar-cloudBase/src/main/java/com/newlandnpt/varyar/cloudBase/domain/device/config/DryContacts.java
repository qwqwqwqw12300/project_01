package com.newlandnpt.varyar.cloudBase.domain.device.config;

public class DryContacts {

	public static final String primary = "primary";
	
	public static final String secondary = "secondary";
	
	public Integer mode;
	
	public Integer policy;

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public Integer getPolicy() {
		return policy;
	}

	public void setPolicy(Integer policy) {
		this.policy = policy;
	}
	
}
