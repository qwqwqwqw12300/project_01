package com.newlandnpt.varyar.common.core.domain.entity;

/**
 * @author lin.ju
 * @date 2023/3/2
 */
public class AccessInfo {

    /**
     * 设备no
     */
    private String deviceNo;

    /**
     * 进出类型
     */
    private String type;

    /**
     * 子区域编号
     */
    private int zoneNo = -1;

    /**
     * 房间人数
     */
    private int peopleCount;

    /**
     * 发生时间
     */
    private long time;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 区域名称
     */
    private String areaName = "房间";


    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getZoneNo() {
        return zoneNo;
    }

    public void setZoneNo(int zoneNo) {
        this.zoneNo = zoneNo;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
