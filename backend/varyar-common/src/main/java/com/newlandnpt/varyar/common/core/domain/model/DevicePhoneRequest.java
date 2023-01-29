package com.newlandnpt.varyar.common.core.domain.model;

import com.newlandnpt.varyar.common.core.domain.entity.DevicePhone;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

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
    private List<DevicePhone> list;

    public Map<String ,DevicePhone> mapSet;

    public Map<String, DevicePhone> getMapSet() {
        return mapSet;
    }

    public void setMapSet(Map<String, DevicePhone> mapSet) {
        this.mapSet = mapSet;
    }

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
