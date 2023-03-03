package com.newlandnpt.varyar.common.core.domain.model;

import com.newlandnpt.varyar.common.core.domain.entity.DeviceLocationTop;
import com.newlandnpt.varyar.common.core.domain.entity.DeviceLocationWall;
import com.newlandnpt.varyar.common.core.domain.entity.DeviceWarnParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ApiModel("雷达波设备请求")
public class RadarWaveDeviceRequest extends PageRequest {

    @ApiModelProperty("设备id")
    @Size(min = 0, max = 20, message = "设备编号不能超过20个字符")
    private String deviceId;

    @ApiModelProperty("设备名称")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]+$",message = "设备名称限制中英文加数字")
    @Size(min = 0, max = 20, message = "设备名称不能超过20个字符")
    private String deviceName;

    @ApiModelProperty("设备安装位置")
    private String installPosition = "0";
    @ApiModelProperty("顶装")
    private DeviceLocationTop deviceLocationTop =new DeviceLocationTop();
    @ApiModelProperty("墙挂")
    private DeviceLocationWall deviceLocationWall =new DeviceLocationWall();
    @ApiModelProperty("无人预警")
    private DeviceWarnParameter deviceWarnParameter =new DeviceWarnParameter();

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getInstallPosition() {
        return installPosition;
    }

    public void setInstallPosition(String installPosition) {
        this.installPosition = installPosition;
    }

    public DeviceLocationTop getDeviceLocationTop() {
        return deviceLocationTop;
    }

    public void setDeviceLocationTop(DeviceLocationTop deviceLocationTop) {
        this.deviceLocationTop = deviceLocationTop;
    }

    public DeviceLocationWall getDeviceLocationWall() {
        return deviceLocationWall;
    }

    public void setDeviceLocationWall(DeviceLocationWall deviceLocationWall) {
        this.deviceLocationWall = deviceLocationWall;
    }

    public DeviceWarnParameter getDeviceWarnParameter() {
        return deviceWarnParameter;
    }

    public void setDeviceWarnParameter(DeviceWarnParameter deviceWarnParameter) {
        this.deviceWarnParameter = deviceWarnParameter;
    }
}
