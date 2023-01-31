package com.newlandnpt.varyar.common.core.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class DeviceFenceRequest {
    /**
     * 设备id
     * */
    @NotBlank(message = "设备Id不能为空")
    @Size(min = 0, max = 20, message = "设备Id不能超过20个字符")
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}
