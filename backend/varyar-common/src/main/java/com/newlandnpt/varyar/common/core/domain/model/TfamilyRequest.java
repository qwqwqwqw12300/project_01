package com.newlandnpt.varyar.common.core.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TfamilyRequest {
    /**
     * 家庭名称
     */
    @NotBlank(message = "家庭名称不能为空")
    @Size(min = 0, max = 50, message = "家庭名称不能超过10个字符")
    private String familyName;

    /**
     * 家庭地址
     */
    @NotBlank(message = "家庭地址不能为空")
    @Size(min = 0, max = 50, message = "家庭地址不能超过50个字符")
    private String address;

    /**
     *  家庭Id 唯一标识
     * */
    @Size(min = 0, max = 20, message = "唯一标识不能超过20个字符")
    private String familyId;

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
