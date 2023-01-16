package com.newlandnpt.varyar.common.core.domain.model;

import javax.validation.constraints.Size;

public class FamilyRequest {
    /**
     * 家庭名称
     */
    @Size(min = 0, max = 50, message = "家庭名称不能超过10个字符")
    private String familyName;

    /**
     * 家庭地址
     */
    @Size(min = 0, max = 50, message = "家庭地址不能超过50个字符")
    private String address;

    /**
     *  家庭Id 唯一标识
     * */
    @Size(min = 0, max = 20, message = "唯一标识不能超过20个字符")
    private String familyId;

    /**
     *  分享对象会员id
     * */
    @Size(min = 0, max = 20, message = "分享对象会员id不能超过20个字符")
    private String shareMemberId;

    /**
     *  共享家庭id
     * */
    @Size(min = 0, max = 20, message = "共享家庭id不能超过20个字符")
    private String shareFamilyId;

    public String getShareFamilyId() {
        return shareFamilyId;
    }

    public void setShareFamilyId(String shareFamilyId) {
        this.shareFamilyId = shareFamilyId;
    }

    public String getShareMemberId() {
        return shareMemberId;
    }

    public void setShareMemberId(String shareMemberId) {
        this.shareMemberId = shareMemberId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }
}
