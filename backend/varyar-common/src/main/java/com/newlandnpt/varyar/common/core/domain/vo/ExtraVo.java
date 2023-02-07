package com.newlandnpt.varyar.common.core.domain.vo;

import java.io.Serializable;

import com.alibaba.fastjson2.annotation.JSONField;

/**
 * 跌倒事件目标信息VO
 * @author Bean
 * @date 2023-02-07
 */
public class ExtraVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @JSONField(name = "targetId")
	private Integer targetId;
	
    @JSONField(name = "x_cm")
	private Integer xCm;
	
    @JSONField(name = "y_cm")
	private Integer yCm;
	
    @JSONField(name = "z_cm")
	private Integer zCm;
	
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
