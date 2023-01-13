package com.newlandnpt.varyar.common.core.domain.model;

import com.newlandnpt.varyar.common.core.domain.entity.BatchMessage;


import javax.validation.constraints.NotNull;
import java.util.List;

public class BatchMessageRequest {

    @NotNull(message = "消息列表不能为空")
    private List<BatchMessage> msgFlags;

    public List<BatchMessage> getMsgFlags() {
        return msgFlags;
    }

    public void setMsgFlags(List<BatchMessage> msgFlags) {
        this.msgFlags = msgFlags;
    }

}
