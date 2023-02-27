package com.newlandnpt.varyar.common.utils.SMS.templates;

import java.util.Arrays;
import java.util.List;

/**
 * 进出区域告警模板
 * @author ljx
 * @date 2023/2/22
 */
public class PassOutWarn implements SMSTemplate {

    /**
     * 手机尾号
     */
    private String telPhone;

    /**
     * 设备名称(总长度不能超过10)
     */
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
    private String time;

    public PassOutWarn(String telPhone, String deviceName, String field, String action, String time) {
        this.telPhone = telPhone;
        this.deviceName = deviceName;
        this.field = field;
        this.action = action;
        this.time = time;
    }

    @Override
    public String getTemplateId() {
        return "c190e96fefbb4db28982b707a27ee31d";
    }

    @Override
    public List<String> getParams() {
        return Arrays.asList(telPhone,deviceName,field,action,time);
    }

    @Override
    public Boolean isCorrectParams() {
        return  ParamsVaildate.isRanger(getTelPhone(),20) &&
                ParamsVaildate.isRanger(getAction(),10) &&
                ParamsVaildate.isRanger(getDeviceName(),10) &&
                ParamsVaildate.isRanger(getField(),10) &&
                ParamsVaildate.isRanger(getTime(),10) &&
                ParamsVaildate.isPattern(getTime(), ParamsVaildate.CHAR_NUMBER) &&
                ParamsVaildate.isPattern(getField(), ParamsVaildate.CHAR_NUMBER) &&
                ParamsVaildate.isPattern(getDeviceName(), ParamsVaildate.CHAR_NUMBER) &&
                ParamsVaildate.isPattern(getAction(),ParamsVaildate.CHAR_NUMBER) &&
                ParamsVaildate.isPattern(getTelPhone(),ParamsVaildate.CHAR_NUMBER);
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
