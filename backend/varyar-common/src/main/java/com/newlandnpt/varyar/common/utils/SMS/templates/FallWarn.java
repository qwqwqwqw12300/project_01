package com.newlandnpt.varyar.common.utils.SMS.templates;

import java.util.Arrays;
import java.util.List;

/**
 * 跌倒告警模板
 * @author ljx
 * @date 2023/2/22
 */
public class FallWarn implements SMSTemplate {

    /**
     * 手机尾号
     */
    private String telPhone;

    /**
     * 设备名称(总长度不能超过10)
     */
    private String deviceName;

    @Override
    public String getTemplateId() {
        return null;
    }

    @Override
    public List<String> getParams() {
        return Arrays.asList(telPhone,deviceName);
    }

    @Override
    public Boolean isCorrectParams() {
        return
                ParamsVaildate.isNotNull(getTelPhone()) &&
                ParamsVaildate.isRanger(getTelPhone(), 20) &&
                ParamsVaildate.isNotNull(getDeviceName()) &&
                ParamsVaildate.isRanger(getDeviceName(), 10) &&
                ParamsVaildate.isPattern(getDeviceName(), ParamsVaildate.CHAR_NUMBER);
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
}
