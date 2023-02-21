package com.newlandnpt.varyar.common.utils.SMS.templates;

import java.util.Arrays;
import java.util.List;

/**
 * 身份验证短信模板
 * @author lin.ju
 * @date 2023/2/21
 */
public class AccountValidate implements SMSTemplate{

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 验证码
     */
    private String code;

    @Override
    public String getTemplateId() {
        return null;
    }

    @Override
    public List<String> getParams() {
        return Arrays.asList(companyName,code);
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
