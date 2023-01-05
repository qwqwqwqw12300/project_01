package com.newlandnpt.varyar.common.core.domain.model;

import com.newlandnpt.varyar.common.annotation.Excel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AdviseRequest {
    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Size(min = 0, max = 10, message = "验证码不能超过10个字符")
    private String captcha;

    /** 建议内容 */
    private String content;

    /**
     * 唯一标识
     */
    @NotBlank(message = "唯一标识不能为空")
    @Size(min = 0, max = 32, message = "唯一标识不能超过32个字符")
    private String uuid;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
