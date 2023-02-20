package com.newlandnpt.varyar.common.core.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel("设备请求")
public class DeviceRequest extends PageRequest {

    @ApiModelProperty("设备id")
    @Size(min = 0, max = 20, message = "设备编号不能超过20个字符")
    private String deviceId;

    @ApiModelProperty("家庭Id")
    @Size(min = 0, max = 20, message = "唯一标识不能超过20个字符")
    private String familyId;

    @ApiModelProperty("房间Id")
    @Size(min = 0, max = 20, message = "房间Id标识不能超过20个字符")
    private String roomId;

    @ApiModelProperty("设备名称")
    @Size(min = 0, max = 50, message = "设备名称不能超过50个字符")
    private String deviceName;

    @ApiModelProperty("设备类型")
    @Size(min = 0, max = 1, message = "设备类型不能超过1个字符")
    private String deviceType;

    @ApiModelProperty("设备编号")
    @Size(min = 0, max = 50, message = "设备编号不能超过50个字符")
    private String deviceNo;

    @ApiModelProperty("位置")
    @Size(min = 0, max = 100, message = "位置信息不能超过100个字符")
    private String location;

    @ApiModelProperty("长度")
    private BigDecimal roomLength;

    @ApiModelProperty("左侧")
    private BigDecimal roomLeft;

    @ApiModelProperty("右侧")
    private BigDecimal roomRight;

    @ApiModelProperty("高度")
    private BigDecimal roomHeight;

    @ApiModelProperty("进入时间")
    private Long entryTime;

    @ApiModelProperty("离开时间")
    private Long departureTime;

    @ApiModelProperty("开始时间 HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "HH:mm")
    private Date endTime;

    @ApiModelProperty("进入标识 （0是 1否）")
    @Size(min = 0, max = 1, message = "进入标识不能超过1个字符")
    private String inMonitorFlag;

    @ApiModelProperty("离开标识 （0是 1否）")
    @Size(min = 0, max = 1, message = "区域类型不能超过1个字符")
    private String outMonitorFlag;

    @ApiModelProperty("区域类型  0：监控区域    1：私人区域")
    @Size(min = 0, max = 1, message = "区域类型不能超过1个字符")
    private String existFlag;

    @ApiModelProperty("迭代监测标志  0：是    1：否")
    @Size(min = 0, max = 1, message = "迭代监测标志不能超过1个字符")
    private String fallFlag;

    @ApiModelProperty("操作标识  1：绑定  2：修改  3：解绑")
    @Size(min = 0, max = 1, message = "操作标识不能超过1个字符")
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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

    public String getExistFlag() {
        return existFlag;
    }

    public void setExistFlag(String existFlag) {
        this.existFlag = existFlag;
    }

    public String getFallFlag() {
        return fallFlag;
    }

    public void setFallFlag(String fallFlag) {
        this.fallFlag = fallFlag;
    }

    public BigDecimal getRoomLength() {
        return roomLength;
    }

    public void setRoomLength(BigDecimal roomLength) {
        this.roomLength = roomLength;
    }

    public BigDecimal getRoomLeft() {
        return roomLeft;
    }

    public void setRoomLeft(BigDecimal roomLeft) {
        this.roomLeft = roomLeft;
    }

    public BigDecimal getRoomRight() {
        return roomRight;
    }

    public void setRoomRight(BigDecimal roomRight) {
        this.roomRight = roomRight;
    }

    public BigDecimal getRoomHeight() {
        return roomHeight;
    }

    public void setRoomHeight(BigDecimal roomHeight) {
        this.roomHeight = roomHeight;
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
