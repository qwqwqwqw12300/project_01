package com.newlandnpt.varyar.common.core.domain.model;

import com.newlandnpt.varyar.common.core.domain.entity.BatchMessage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class MessageRequest {
    /**
     *  消息id
     * */
    @NotBlank(message = "消息id不能为空")
    @Size(min = 0, max = 20, message = "消息id不能超过20个字符")
    private String msgId;
    /**
     *  消息标识 0:未处理 1:已处理
     * */
    @Size(min = 0, max = 1, message = "消息标识不能超过1个字符")
    private String msgFlag;

    private List<BatchMessage> msgFlags;

    public List<BatchMessage> getMsgFlags() {
        return msgFlags;
    }

    public void setMsgFlags(List<BatchMessage> msgFlags) {
        this.msgFlags = msgFlags;
    }

    public String getMsgFlag() {
        return msgFlag;
    }

    public void setMsgFlag(String msgFlag) {
        this.msgFlag = msgFlag;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
