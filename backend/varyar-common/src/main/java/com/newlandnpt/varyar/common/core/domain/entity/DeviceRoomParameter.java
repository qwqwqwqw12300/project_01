package com.newlandnpt.varyar.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class DeviceRoomParameter implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     *进入时间  s
     * */
    private Long entryTime;
    /**
     *离开时间 s
     * */
    private Long departureTime;
    /**
     *开始时间 data HH:mm:ss
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;
    /**
     *结束时间 data HH:mm:ss
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;
    /**
     *进入标识 （0是 1否）
     * */
    private String inMonitorFlag;
    /**
     *离开标识 （0是 1否）
     * */
    private String outMonitorFlag;
    /**
     *  区域类型  0：监控区域    1：私人区域
     * */
    private String existFlag;
    /**
     *  迭代监测标志  0：是    1：否
     * */
    private String fallFlag;

    public static long getSerialVersionUID() {
        return serialVersionUID;
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
}
