package com.newlandnpt.varyar.common.core.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ShareFamilyRequest {
    /**
     * 分享对象手机号
     */
    @NotBlank(message = "分享对象手机号不能为空")
    @Size(min = 0, max = 11, message = "分享对象手机号不能超过11个字符")
    private String phone;

    /**
     * 短信验证码
     */
    @NotBlank(message = "短信验证码不能为空")
    @Size(min = 0, max = 4, message = "家庭地址不能超过4个字符")
    private String code;

    /**
     *  家庭Id 唯一标识
     * */
    @NotBlank(message = "家庭Id不能为空")
    @Size(min = 0, max = 20, message = "唯一标识不能超过20个字符")
    private String familyId;
    /**
     * 唯一标识
     */
    @NotBlank(message = "唯一标识不能为空")
    @Size(min = 0, max = 32, message = "唯一标识不能超过32个字符")
    private String smsUuid;

    public String getSmsUuid() {
        return smsUuid;
    }

    public void setSmsUuid(String smsUuid) {
        this.smsUuid = smsUuid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }
}
