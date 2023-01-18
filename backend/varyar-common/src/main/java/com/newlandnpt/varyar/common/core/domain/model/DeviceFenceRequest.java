package com.newlandnpt.varyar.common.core.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class DeviceFenceRequest {
    /**
     * 电子围栏设备id
     * */
    @NotBlank(message = "电子围栏设备Id不能为空")
    @Size(min = 0, max = 20, message = "电子围栏设备Id不能超过20个字符")
    private String deviceFenceId;

    public String getDeviceFenceId() {
        return deviceFenceId;
    }

    public void setDeviceFenceId(String deviceFenceId) {
        this.deviceFenceId = deviceFenceId;
    }
}
