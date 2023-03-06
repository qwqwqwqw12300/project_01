package com.newlandnpt.varyar.common.utils.tcp.req;

/**
 * 新增和修改时段禁用
 * @author ljx
 * @date 2023/3/4
 */
public class AddOrUpdatePeriodDisabledReq {

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


    public AddOrUpdatePeriodDisabledReq() {
    }

    public AddOrUpdatePeriodDisabledReq(String deviceNo, String periodDisableTag, String uuid, String beginTime, String endTime) {
        this.deviceNo = deviceNo;
        this.periodDisableTag = periodDisableTag;
        this.uuid = uuid;
        this.beginTime = beginTime;
        this.endTime = endTime;
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
}
