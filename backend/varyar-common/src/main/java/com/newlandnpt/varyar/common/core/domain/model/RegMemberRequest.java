package com.newlandnpt.varyar.common.core.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 会员注册
 *
 */
@ApiModel(description = "会员用户注册请求")
public class RegMemberRequest {

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    @Size(min = 0, max = 11, message = "手机号不能超过11个字符")
    private String phone;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    @Size(min = 0, max = 200, message = "密码不能超过200个字符")
    private String password;

    /**
     * 验证码
     */
    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码不能为空")
    @Size(min = 0, max = 10, message = "验证码不能超过10个字符")
    private String code;

    /**
     * 唯一标识
     */
    @ApiModelProperty("唯一标识")
    @NotBlank(message = "唯一标识不能为空")
    @Size(min = 0, max = 32, message = "唯一标识不能超过32个字符")
    private String smsUuid;

    /**
     * 登录设备注册类型（0:android 1:ios）
     */
    @ApiModelProperty("登录设备注册类型（0:android 1:ios）")
    @Size(min = 0, max = 1, message = "登录设备注册类型不能超过1个字符")
    private String registrationType;

    /**
     * 会员登录设备注册号
     */
    @ApiModelProperty("会员登录设备注册号")
    @Size(min = 0, max = 100, message = "会员登录设备注册号不能超过100个字符")
    private String registrationId;

    /**
     * 会员昵称
     */
    @ApiModelProperty("会员昵称")
    private String nickname;
    /**
     * 会员头像地址
     */
    @ApiModelProperty("会员头像地址")
    private String avatar;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

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

    public String getSmsUuid() {
        return smsUuid;
    }

    public void setSmsUuid(String smsUuid) {
        this.smsUuid = smsUuid;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }
}
