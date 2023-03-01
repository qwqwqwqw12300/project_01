package com.newlandnpt.varyar.system.domain.vo;


import java.io.Serializable;
import java.util.Date;

/**
 * @author chenxw1
 * @date 2023/2/28
 **/
public class SmsEventParamsVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String phone;
    private String deviceName;
    /**
     * 区域 房间或某某区域(长度不能超过10)
     */
    private String field;

    /**
     * 行为 进入或离开(总长度不能超过10)
     */
    private String action;

    /**
     * 限制时间(总长度不能超过10)
     */
    private long time;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
