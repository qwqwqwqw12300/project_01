package com.newlandnpt.varyar.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

/**
 * 课堂模式周期
 * @author ljx
 * @date 2023/3/1 3/6
 */
public class ClassTimePeriod {

    /**
     * 设备号,设备唯一编号
     */
    private String deviceNo;

    /**
     * 时段禁用标签
     */
    private String periodDisableTag;

    /**
     * 时段禁用uuid
     */
    private String uuid;

    /**
     * 开始时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy/MM/dd HH:mm")
    @DateTimeFormat(pattern="yyyy/MM/dd HH:mm")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy/MM/dd HH:mm")
    @DateTimeFormat(pattern="yyyy/MM/dd HH:mm")
    private Date endTime;

    /**
     * 周期
     */
    private String period;

    /**
     * 是否生效
     */
    private String enable = "1";

    public ClassTimePeriod() {
    }

    public ClassTimePeriod(String deviceNo, String periodDisableTag, String uuid, Date beginTime, Date endTime, String period, String enable) {
        this.deviceNo = deviceNo;
        this.periodDisableTag = periodDisableTag;
        this.uuid = uuid;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.period = period;
        this.enable = enable;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getPeriodDisableTag() {
        return periodDisableTag;
    }

    public void setPeriodDisableTag(String periodDisableTag) {
        this.periodDisableTag = periodDisableTag;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }
}
