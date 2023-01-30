package com.newlandnpt.varyar.common.core.domain.model;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

public class RadarRequest {
    /**
     *  房间子区域id 唯一标识
     * */
    @Size(min = 0, max = 20, message = "房间子区域id标识不能超过20个字符")
    private String roomZoneId;
    /**
     *  房间Id 唯一标识
     * */
    @NotBlank(message = "房间Id不能为空")
    @Size(min = 0, max = 20, message = "房间Id标识不能超过20个字符")
    private String roomId;
    /**
     *  子区域名称
     * */
    @NotBlank(message = "子区域名称不能为空")
    @Size(min = 0, max = 50, message = "子区域名称不能超过20个字符")
    private String name;

    /**
     *  区域类型  0：监控区域    1：私人区域
     * */
    @NotBlank(message = "区域类型不能为空")
    @Size(min = 0, max = 1, message = "区域类型不能超过1个字符")
    private String existFlag;
    /**
     *  迭代监测标志  0：是    1：否
     * */
    @NotBlank(message = "迭代监测标志不能为空")
    @Size(min = 0, max = 1, message = "迭代监测标志不能超过1个字符")
    private String fallFlag;

    /**
     * 最左侧的点
     * */
    @NotNull(message = "最左侧的点不能为空")
    private BigDecimal x1;

    /**
     * 最右侧的点
     * */
    @NotNull(message = "最右侧的点不能为空")
    private BigDecimal x2;
    /**
     * 最近的点
     * */
    @NotNull(message = "最近的点不能为空")
    private BigDecimal y1;
    /**
     * 最远的点
     * */
    @NotNull(message = "最远的点不能为空")
    private BigDecimal y2;
    /**
     * 最底部的点
     * */
    @NotNull(message = "最底部的点不能为空")
    private BigDecimal z1;
    /**
     *最顶部的点
     * */
    @NotNull(message = "最顶部的点不能为空")
    private BigDecimal z2;
    /**
     *进入时间   data HH:mm:ss
     * */
    private Long entryTime;
    /**
     *离开时间 data HH:mm:ss
     * */
    private Long departureTime;
    /**
     *开始时间 data HH:mm:ss
     * */
    private Date startTime;
    /**
     *结束时间 data HH:mm:ss
     * */
    private Date endTime;
    /**
     *进入标识 （0是 1否）
     * */
    @NotNull(message = "进入标识不能为空")
    private String inMonitorFlag;
    /**
     *离开标识 （0是 1否）
     * */
    @NotNull(message = "离开标识不能为空")
    private String outMonitorFlag;

    /**设备id*/
    @NotNull(message = "设备id不能为空")
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }


    public BigDecimal getX1() {
        return x1;
    }

    public void setX1(BigDecimal x1) {
        this.x1 = x1;
    }

    public BigDecimal getX2() {
        return x2;
    }

    public void setX2(BigDecimal x2) {
        this.x2 = x2;
    }

    public BigDecimal getY1() {
        return y1;
    }

    public void setY1(BigDecimal y1) {
        this.y1 = y1;
    }

    public BigDecimal getY2() {
        return y2;
    }

    public void setY2(BigDecimal y2) {
        this.y2 = y2;
    }

    public BigDecimal getZ1() {
        return z1;
    }

    public void setZ1(BigDecimal z1) {
        this.z1 = z1;
    }

    public BigDecimal getZ2() {
        return z2;
    }

    public void setZ2(BigDecimal z2) {
        this.z2 = z2;
    }

    public Long getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Long entryTime) {
        this.entryTime = entryTime;
    }

    public Long getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Long departureTime) {
        this.departureTime = departureTime;
    }

    public String getFallFlag() {
        return fallFlag;
    }

    public void setFallFlag(String fallFlag) {
        this.fallFlag = fallFlag;
    }

    public String getRoomZoneId() {
        return roomZoneId;
    }

    public void setRoomZoneId(String roomZoneId) {
        this.roomZoneId = roomZoneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExistFlag() {
        return existFlag;
    }

    public void setExistFlag(String existFlag) {
        this.existFlag = existFlag;
    }

    public String getInMonitorFlag() {
        return inMonitorFlag;
    }

    public void setInMonitorFlag(String inMonitorFlag) {
        this.inMonitorFlag = inMonitorFlag;
    }

    public String getOutMonitorFlag() {
        return outMonitorFlag;
    }

    public void setOutMonitorFlag(String outMonitorFlag) {
        this.outMonitorFlag = outMonitorFlag;
    }
}
