package com.newlandnpt.varyar.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;

/**
 * 设备电子围栏对象 t_device_fence
 *
 * @author chenxw
 * @date 2023-01-06
 */
@ApiModel("设备电子围栏")
public class TDeviceFence extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("设备电子围栏id")
    private Long deviceFenceId;

    @ApiModelProperty("高德API服务-电子围栏id")
    @Excel(name = "高德API服务-电子围栏id")
    private Long geoFenceId;

    @ApiModelProperty("设备id")
    @Excel(name = "设备id")
    private Long deviceId;

    @ApiModelProperty("设备编号")
    @Excel(name = "设备编号")
    private String deviceNo;

    @ApiModelProperty("地址")
    @Excel(name = "地址")
    private String address;

    @ApiModelProperty("经度")
    @Excel(name = "经度")
    private String longitude;

    @ApiModelProperty("纬度")
    @Excel(name = "纬度")
    private String latitude;

    @ApiModelProperty("半径")
    @Excel(name = "半径")
    private String radius;

    public void setDeviceFenceId(Long deviceFenceId)
    {
        this.deviceFenceId = deviceFenceId;
    }

    public Long getDeviceFenceId()
    {
        return deviceFenceId;
    }
    public void setGeoFenceId(Long geoFenceId)
    {
        this.geoFenceId = geoFenceId;
    }

    public Long getGeoFenceId()
    {
        return geoFenceId;
    }
    public void setDeviceId(Long deviceId)
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId()
    {
        return deviceId;
    }
    public void setDeviceNo(String deviceNo)
    {
        this.deviceNo = deviceNo;
    }

    public String getDeviceNo()
    {
        return deviceNo;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public String getLongitude()
    {
        return longitude;
    }
    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getLatitude()
    {
        return latitude;
    }
    public void setRadius(String radius)
    {
        this.radius = radius;
    }

    public String getRadius()
    {
        return radius;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("deviceFenceId", getDeviceFenceId())
                .append("geoFenceId", getGeoFenceId())
                .append("deviceId", getDeviceId())
                .append("deviceNo", getDeviceNo())
                .append("address", getAddress())
                .append("longitude", getLongitude())
                .append("latitude", getLatitude())
                .append("radius", getRadius())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
