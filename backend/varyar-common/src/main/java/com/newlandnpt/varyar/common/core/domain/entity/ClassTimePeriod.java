package com.newlandnpt.varyar.common.core.domain.entity;

/**
 * 课堂模式周期
 * @author ljx
 * @date 2023/3/1
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
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 周期
     */
    private String period;

    /**
     * 是否生效
     */
    private String enable;

    public ClassTimePeriod() {
    }

    public ClassTimePeriod(String deviceNo, String periodDisableTag, String uuid, String beginTime, String endTime, String period, String enable) {
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

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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
