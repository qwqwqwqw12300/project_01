package com.newlandnpt.varyar.common.core.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 重置密码
 *
 */
public class MemberInfoRequest {

    /**
     * 旧手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Size(min = 0, max = 11, message = "手机号不能超过11个字符")
    private String oldPhone;

    /**
     * 旧短信验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Size(min = 0, max = 10, message = "验证码不能超过10个字符")
    private String oldCode;

    /**
     * 新手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Size(min = 0, max = 11, message = "手机号不能超过11个字符")
    private String newPhone;

    /**
     * 旧短信验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Size(min = 0, max = 10, message = "验证码不能超过10个字符")
    private String newCode;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 0, max = 100, message = "密码不能超过100个字符")
    private String password;

    /**
     * 唯一标识
     */
    @NotBlank(message = "唯一标识不能为空")
    @Size(min = 0, max = 32, message = "唯一标识不能超过32个字符")
    private String uuid;

    /**
     * 唯一标识
     */
    @NotBlank(message = "唯一标识不能为空")
    @Size(min = 0, max = 32, message = "唯一标识不能超过32个字符")
    private String newuuid;


    public String getOldPhone() {
        return oldPhone;
    }

    public void setOldPhone(String oldPhone) {
        this.oldPhone = oldPhone;
    }

    public String getNewPhone() {
        return newPhone;
    }

    public void setNewPhone(String newPhone) {
        this.newPhone = newPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldCode() {
        return oldCode;
    }

    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
    }

    public String getNewCode() {
        return newCode;
    }

    public void setNewCode(String newCode) {
        this.newCode = newCode;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNewuuid() {
        return newuuid;
    }

    public void setNewuuid(String newuuid) {
        this.newuuid = newuuid;
    }
}
