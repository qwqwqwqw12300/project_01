package com.newlandnpt.varyar.common.core.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel("修改会员名称头像请求")
public class MemberNameAvatarRequest {

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


}
