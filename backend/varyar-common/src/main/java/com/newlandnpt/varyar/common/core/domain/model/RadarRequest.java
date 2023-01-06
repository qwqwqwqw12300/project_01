package com.newlandnpt.varyar.common.core.domain.model;

import org.apache.poi.hpsf.Decimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class RadarRequest {

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
    private String zoneName;

    /**
     *  区域类型
     * */
    @NotBlank(message = "区域类型不能为空")
    @Size(min = 0, max = 1, message = "房间Id标识不能超过1个字符")
    private String zoneType;
    /**
     * 最左侧的点
     * */
    @NotBlank(message = "最左侧的点不能为空")
    private BigDecimal x1;

    /**
     * 最右侧的点
     * */
    @NotBlank(message = "最右侧的点不能为空")
    private BigDecimal x2;
    /**
     * 最近的点
     * */
    @NotBlank(message = "最近的点不能为空")
    private BigDecimal y1;
    /**
     * 最远的点
     * */
    @NotBlank(message = "最远的点不能为空")
    private BigDecimal y2;
    /**
     * 最底部的点
     * */
    @NotBlank(message = "最底部的点不能为空")
    private BigDecimal z1;
    /**
     *最顶部的点
     * */
    @NotBlank(message = "最顶部的点不能为空")
    private BigDecimal z2;
    /**
     *进入时间
     * */
    @NotBlank(message = "进入时间不能为空")
    private Long entryTime;
    /**
     *离开时间
     * */
    @NotBlank(message = "离开时间不能为空")
    private Long departureTime;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneType() {
        return zoneType;
    }

    public void setZoneType(String zoneType) {
        this.zoneType = zoneType;
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
}