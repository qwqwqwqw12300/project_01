package com.newlandnpt.varyar.common.core.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel("修改会员手机号请求")
public class MemberInfoRequest {


//    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty("旧手机号")
    @Size(min = 0, max = 11, message = "手机号不能超过11个字符")
    private String oldPhone;

//    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty("旧手机号短信验证码")
    @Size(min = 0, max = 10, message = "验证码不能超过10个字符")
    private String oldCode;

//    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty("新手机号")
    @Size(min = 0, max = 11, message = "手机号不能超过11个字符")
    private String newPhone;

//    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty("新手机号短信验证码")
    @Size(min = 0, max = 10, message = "验证码不能超过10个字符")
    private String newCode;

//    @NotBlank(message = "密码不能为空")
    @ApiModelProperty("密码")
    @Size(min = 0, max = 100, message = "密码不能超过100个字符")
    private String password;

//    @NotBlank(message = "唯一标识不能为空")
    @ApiModelProperty("唯一标识")
    @Size(min = 0, max = 32, message = "唯一标识不能超过32个字符")
    private String uuid;

//    @NotBlank(message = "唯一标识不能为空")
    @ApiModelProperty("唯一标识")
    @Size(min = 0, max = 32, message = "唯一标识不能超过32个字符")
    private String newuuid;

    @ApiModelProperty("认证token")
    @NotBlank(message = "认证token不能为空")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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
