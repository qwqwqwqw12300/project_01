package com.newlandnpt.varyar.common.core.domain.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@ApiModel(description = "雷达波离床规则请求")
public class RadarWaveLeaveBedRulesRequest {

    /**设备id*/
    @NotNull(message = "设备id不能为空")
    @Size(min = 0, max = 20, message = "设备编号不能超过20个字符")
    private String deviceId;

    @ApiModelProperty("区域Id")
    @Size(min = 0, max = 20, message = "区域Id标识不能超过20个字符")
    private String roomZoneId;

    /**
     * 日期类型：0 按日期 1 按星期
     */
    @ApiModelProperty("日期类型")
    private String dateType="1";
    /**
     * 星期
     */
    @ApiModelProperty("星期")
    private String[] week = {"0", "1", "2", "3", "4", "5", "6"};
    /**
     * 开始日期 data yyyy-MM-dd
     */
    @ApiModelProperty("开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 结束日期 data yyyy-MM-dd
     */
    @ApiModelProperty("结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 结束时间 开始时间
     */
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "HH:mm")
    private Date startTime;
    /**
     * 结束时间 data HH:mm
     */
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "HH:mm")
    private Date endTime;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRoomZoneId() {
        return roomZoneId;
    }

    public void setRoomZoneId(String roomZoneId) {
        this.roomZoneId = roomZoneId;
    }



    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String[] getWeek() {
        return week;
    }

    public void setWeek(String[] week) {
        this.week = week;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
}
