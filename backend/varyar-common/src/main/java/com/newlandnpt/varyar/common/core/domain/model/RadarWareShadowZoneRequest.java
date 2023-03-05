package com.newlandnpt.varyar.common.core.domain.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@ApiModel(description = "雷达波屏蔽请求")
public class RadarWareShadowZoneRequest {
    /**
     *  房间子区域id 唯一标识
     * */
    @ApiModelProperty("房间子区域id")
    @Size(min = 0, max = 20, message = "房间子区域id标识不能超过20个字符")
    private String roomZoneId;
    /**
     *  房间Id 唯一标识
     * */
    @ApiModelProperty("房间Id")
    @NotBlank(message = "房间Id不能为空")
    @Size(min = 0, max = 20, message = "房间Id标识不能超过20个字符")
    private String roomId;
    /**
     *  子区域名称
     * */
    /**设备id*/
    @NotNull(message = "设备id不能为空")
    private String deviceId;

    @ApiModelProperty("屏蔽区域名称")
    @NotBlank(message = "子区域名称不能为空")
    @Size(min = 0, max = 50, message = "子区域名称不能超过20个字符")
    private String shadowZoneName;

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


    public String getRoomZoneId() {
        return roomZoneId;
    }

    public void setRoomZoneId(String roomZoneId) {
        this.roomZoneId = roomZoneId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getShadowZoneName() {
        return shadowZoneName;
    }

    public void setShadowZoneName(String shadowZoneName) {
        this.shadowZoneName = shadowZoneName;
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


}
