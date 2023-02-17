package com.newlandnpt.varyar.common.core.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel("修改密码请求")
public class PasswordRequest {

    @ApiModelProperty("旧密码")
    @NotBlank(message = "旧密码不能为空")
    @Size(min = 0, max = 200, message = "验证码不能超过200个字符")
    private String oldPassword;

    @ApiModelProperty("新密码")
    @NotBlank(message = "新密码不能为空")
    @Size(min = 0, max = 200, message = "新密码不能超过200个字符")
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
