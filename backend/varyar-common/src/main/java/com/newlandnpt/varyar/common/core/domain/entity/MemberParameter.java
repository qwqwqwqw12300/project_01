package com.newlandnpt.varyar.common.core.domain.entity;

import java.io.Serializable;

public class MemberParameter implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * 消息推送标识
     * 1: 推送  0:不推送
     */
    private String pushMessage;

    public String getPushMessage() {
        return pushMessage;
    }

    public void setPushMessage(String pushMessage) {
        this.pushMessage = pushMessage;
    }
}
