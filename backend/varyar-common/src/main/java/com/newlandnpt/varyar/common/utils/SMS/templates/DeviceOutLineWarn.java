package com.newlandnpt.varyar.common.utils.SMS.templates;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

/**
 * 设备断网告警模板
 *
 * @author ljx
 * @date 2023/2/22
 */
public class DeviceOutLineWarn implements SMSTemplate {
    /**
     * 手机尾号
     */
    @NotNull(message = "手机尾号不能为空")
    @Size(max = 20, message = "手机尾号不能超过20位")
    private String telPhone;

    /**
     * 设备名称
     */
    @NotNull(message = "设备名称不能为空")
    @Size(max = 10, message = "设备名称不能超过10位")
    @Pattern(regexp = "^[\u4e00-\u9fa5a-zA-Z0-9]+$")
    private String deviceName;

    public DeviceOutLineWarn(@NotNull(message = "手机尾号不能为空") @Size(max = 20, message = "手机尾号不能超过20位") String telPhone, @NotNull(message = "设备名称不能为空") @Size(max = 10, message = "设备名称不能超过10位") @Pattern(regexp = "^[\u4e00-\u9fa5a-zA-Z0-9]+$") String deviceName) {
        this.telPhone = telPhone;
        this.deviceName = deviceName;
    }

    @Override
    public String getTemplateId() {
        return "367bb07c1a2f4bd5a1986ea265c11c5f";
    }

    @Override
    public List<String> getParams() {
        return Arrays.asList(telPhone, deviceName);
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
