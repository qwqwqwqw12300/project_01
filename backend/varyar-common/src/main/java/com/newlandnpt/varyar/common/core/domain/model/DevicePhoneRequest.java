package com.newlandnpt.varyar.common.core.domain.model;

import com.newlandnpt.varyar.common.core.domain.entity.DevicePhone;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class DevicePhoneRequest {
    /**
     * 设备Id
     * */
    @NotBlank(message = "设备Id不能为空")
    @Size(min = 0, max = 20, message = "设备Id不能超过20个字符")
    private String deviceId;
    /**
     * 电话列表
     * */
    @NotBlank(message = "电话列表不能为空")
    private List<DevicePhone> list;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<DevicePhone> getList() {
        return list;
    }

    public void setList(List<DevicePhone> list) {
        this.list = list;
    }
}
