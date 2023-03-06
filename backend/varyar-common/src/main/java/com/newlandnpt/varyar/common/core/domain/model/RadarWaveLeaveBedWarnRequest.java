package com.newlandnpt.varyar.common.core.domain.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@ApiModel(description = "雷达波离床规则请求")
public class RadarWaveLeaveBedWarnRequest {

    /**设备id*/
    @NotNull(message = "设备id不能为空")
    @Size(min = 0, max = 20, message = "设备编号不能超过20个字符")
    private String deviceId;

    @ApiModelProperty("家庭Id")
    @Size(min = 0, max = 20, message = "唯一标识不能超过20个字符")
    private String familyId;

    @ApiModelProperty("房间Id")
    @Size(min = 0, max = 20, message = "房间Id标识不能超过20个字符")
    private String roomId;

    @ApiModelProperty("区域Id")
    @Size(min = 0, max = 20, message = "房间Id标识不能超过20个字符")
    private String roomZoneId;
    /**
     *  子区域名称
     * */
    @ApiModelProperty("子区域名称")
    @NotBlank(message = "子区域名称不能为空")
    @Size(min = 0, max = 50, message = "子区域名称不能超过20个字符")
    private String BedName;
    /**
     * 最左侧的点
     * */
    @ApiModelProperty("最左侧的点")
    @NotNull(message = "最左侧的点不能为空")
    private BigDecimal x1;

    /**
     * 最右侧的点
     * */
    @ApiModelProperty("最右侧的点")
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
    @ApiModelProperty("最远的点")
    @NotNull(message = "最远的点不能为空")
    private BigDecimal y2;
    /**
     * 最底部的点
     * */
    @ApiModelProperty("最底部的点")
    @NotNull(message = "最底部的点不能为空")
    private BigDecimal z1;
    /**
     *最顶部的点
     * */
    @ApiModelProperty("最顶部的点")
    @NotNull(message = "最顶部的点不能为空")
    private BigDecimal z2;
    /**
     * 0:普通区域  1：屏蔽区域  2：床
     * */
    @ApiModelProperty("子区域类型：0:普通区域 1：屏蔽区域 2：床")
    @Size(min = 0, max = 1, message = "子区域类型不能超过1个字符")
    private String zoneType;


    //离床规则对象
    @ApiModelProperty("离床规则对象")
    private LeaveBedWarnParameter leaveBedWarnParameter =new LeaveBedWarnParameter();


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

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

    public String getRoomZoneId() {
        return roomZoneId;
    }

    public void setRoomZoneId(String roomZoneId) {
        this.roomZoneId = roomZoneId;
    }

    public String getBedName() {
        return BedName;
    }

    public void setBedName(String bedName) {
        BedName = bedName;
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

    public String getZoneType() {
        return zoneType;
    }

    public void setZoneType(String zoneType) {
        this.zoneType = zoneType;
    }

    public LeaveBedWarnParameter getLeaveBedWarnParameter() {
        return leaveBedWarnParameter;
    }

    public void setLeaveBedWarnParameter(LeaveBedWarnParameter leaveBedWarnParameter) {
        this.leaveBedWarnParameter = leaveBedWarnParameter;
    }
}
