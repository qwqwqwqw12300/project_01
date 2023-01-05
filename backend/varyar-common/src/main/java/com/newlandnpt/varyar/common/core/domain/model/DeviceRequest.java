package com.newlandnpt.varyar.common.core.domain.model;

import org.apache.poi.hpsf.Decimal;

import javax.validation.constraints.Size;

public class DeviceRequest {
    /**
     * 设备id
     * */
    @Size(min = 0, max = 20, message = "设备编号不能超过20个字符")
    private String deviceId;
    /**
     *  家庭Id 唯一标识
     * */
    @Size(min = 0, max = 20, message = "唯一标识不能超过20个字符")
    private String familyId;

    /**
     *  房间Id 唯一标识
     * */
    @Size(min = 0, max = 20, message = "房间Id标识不能超过20个字符")
    private String roomId;

    /**
     *  设备名称
     * */
    @Size(min = 0, max = 50, message = "设备名称不能超过50个字符")
    private String deviceName;

    /**
     *  设备类型
     * */
    @Size(min = 0, max = 1, message = "设备类型不能超过1个字符")
    private String deviceType;
    /**
     * 设备编号
     * */
    @Size(min = 0, max = 50, message = "设备编号不能超过50个字符")
    private String deviceNo;
    /**
     * 位置
     * */
    @Size(min = 0, max = 100, message = "位置信息不能超过100个字符")
    private String location;



    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}
