package com.newlandnpt.varyar.common.core.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SmsRequest {
    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Size(min = 0, max = 10, message = "验证码不能超过10个字符")
    private String captcha;

    /**
     * 唯一标识
     */
    @NotBlank(message = "唯一标识不能为空")
    @Size(min = 0, max = 32, message = "唯一标识不能超过32个字符")
    private String uuid;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Size(min = 0, max = 11, message = "手机号不能超过11个字符")
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
