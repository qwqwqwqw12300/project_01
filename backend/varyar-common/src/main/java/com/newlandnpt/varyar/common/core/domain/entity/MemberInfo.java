package com.newlandnpt.varyar.common.core.domain.entity;

import java.io.Serializable;

public class MemberInfo implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long memberId;

    private String state;

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
