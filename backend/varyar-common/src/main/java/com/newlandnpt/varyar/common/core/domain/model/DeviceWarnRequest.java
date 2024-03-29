package com.newlandnpt.varyar.common.core.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel("设备预警策略请求")
public class DeviceWarnRequest{

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


    /** 规则编号 */
    private String ruleNo;
    /** 跌倒预警开关 ：1开 0关  */
    private String bedNo;

    /** 跌倒预警开关 ：1开 0关  */
    private String fallWarn;
    /** 无人预警开关 ：1开 0关 */
    private String noBody;
    /** 无人预警连续24小时开关 1开 0关 */
    private String noBodyContinue;

    /**
     * 规则名称
     */
    private String warnRuleName;

    private String  ruleSwitch;
    /**
     * 日期类型
     */
    private String dateType="1";
    /**
     * 星期
     */
    private String[] week;
    /**
     * 开始日期 data yyyy-MM-dd
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date startDate;
    /**
     * 结束日期 data yyyy-MM-dd
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date endDate;

    /**
     * 结束时间 开始时间
     */
    @JsonFormat(pattern = "HH:mm",timezone="GMT+8")
    private Date startTime;
    /**
     * 结束时间 data HH:mm
     */
    @JsonFormat(pattern = "HH:mm",timezone="GMT+8")
    private Date endTime;

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

    public String getRuleNo() {
        return ruleNo;
    }

    public void setRuleNo(String ruleNo) {
        this.ruleNo = ruleNo;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getFallWarn() {
        return fallWarn;
    }

    public void setFallWarn(String fallWarn) {
        this.fallWarn = fallWarn;
    }

    public String getNoBody() {
        return noBody;
    }

    public void setNoBody(String noBody) {
        this.noBody = noBody;
    }

    public String getNoBodyContinue() {
        return noBodyContinue;
    }

    public void setNoBodyContinue(String noBodyContinue) {
        this.noBodyContinue = noBodyContinue;
    }

    public String getWarnRuleName() {
        return warnRuleName;
    }

    public void setWarnRuleName(String warnRuleName) {
        this.warnRuleName = warnRuleName;
    }

    public String getRuleSwitch() {
        return ruleSwitch;
    }

    public void setRuleSwitch(String ruleSwitch) {
        this.ruleSwitch = ruleSwitch;
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
