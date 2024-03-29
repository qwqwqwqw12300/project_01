package com.newlandnpt.varyar.common.core.domain.model;

import javax.validation.constraints.Size;

public class MemberContactsRequest {
    @Size(min = 0, max = 20, message = "会员联络人id不能超过20个字符")
    private String memberContactsId;

    /**
     * 联络人姓名
     */
    @Size(min = 0, max = 50, message = "联络人姓名不能超过50个字符")
    private String name;
    /**
     * 联络人手机号
     */
    @Size(min = 0, max = 11, message = "联络人手机号不能超过11个字符")
    private String phone;
    /**
     * 显示顺序
     * */
    @Size(min = 0, max = 4, message = "显示顺序不能超过4个字符")
    private String orderNum;

    public String getMemberContactsId() {
        return memberContactsId;
    }

    public void setMemberContactsId(String memberContactsId) {
        this.memberContactsId = memberContactsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
