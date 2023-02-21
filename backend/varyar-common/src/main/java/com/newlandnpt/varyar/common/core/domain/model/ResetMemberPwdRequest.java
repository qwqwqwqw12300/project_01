package com.newlandnpt.varyar.common.core.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 重置密码
 *
 */
@ApiModel(description = "重置密码服务请求")
public class ResetMemberPwdRequest {

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
     * 图形验证码
     */
    @ApiModelProperty("图形验证码")
    @NotBlank(message = "验证码不能为空")
    @Size(min = 0, max = 10, message = "验证码不能超过10个字符")
    private String code;


    /**
     * 唯一标识
     */
    @ApiModelProperty("图形验证码唯一标识")
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
