package com.newlandnpt.varyar.common.core.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MessagePushRequest {

    /**
     *  推送标识 唯一标识  1: 推送  0:不推送
     * */
    @NotBlank(message = "推送标识不能为空")
    @Size(min = 0, max = 1, message = "推送标识不能超过1个字符")
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
