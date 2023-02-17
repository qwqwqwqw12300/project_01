package com.newlandnpt.varyar.common.core.domain.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "批量消息")
public class BatchMessage {
    /**
     *  消息id
     * */
    @ApiModelProperty("消息id")
    private String msgId;
    /**
     *  消息标识 0:未处理 1:已处理
     * */
    @ApiModelProperty("消息标识 0:未处理 1:已处理")
    private String msgFlag;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgFlag() {
        return msgFlag;
    }

    public void setMsgFlag(String msgFlag) {
        this.msgFlag = msgFlag;
    }
}
