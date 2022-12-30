package com.newlandnpt.varyar.common.core.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 会员登录
 *
 */
public class MemberLoginPwdRequest {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Size(min = 0, max = 11, message = "手机号不能超过11个字符")
    private String phone;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 0, max = 100, message = "密码不能超过100个字符")
    private String password;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Size(min = 0, max = 10, message = "验证码不能超过10个字符")
    private String code;

    /**
     * 唯一标识
     */
    @NotBlank(message = "唯一标识不能为空")
    @Size(min = 0, max = 32, message = "唯一标识不能超过32个字符")
    private String uuid;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
