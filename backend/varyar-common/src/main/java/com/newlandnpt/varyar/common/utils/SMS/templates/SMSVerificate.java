package com.newlandnpt.varyar.common.utils.SMS.templates;

import java.util.Arrays;
import java.util.List;

/**
 * 短信验证模板Feb
 * @author ljx
 * @date 2023/2/22
 */
public class SMSVerificate implements SMSTemplate {

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 验证码有效时间
     */
    private String codeTime;

    public SMSVerificate(String verifyCode, String codeTime) {
        this.verifyCode = verifyCode;
        this.codeTime = codeTime;
    }

    @Override
    public String getTemplateId() {
        return "63d1277ee1514631a87ce7ed4d2bca0f";
    }

    @Override
    public List<String> getParams() {
        return Arrays.asList(verifyCode,codeTime);
    }

    @Override
    public Boolean isCorrectParams() {
        return
                ParamsVaildate.isRanger(getVerifyCode(),20) &&
                ParamsVaildate.isPattern(getVerifyCode(),ParamsVaildate.CHAR_NUMBER) &&
                ParamsVaildate.isRanger(getCodeTime(),10) &&
                ParamsVaildate.isPattern(getCodeTime(),ParamsVaildate.CHAR_NUMBER);
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getCodeTime() {
        return codeTime;
    }

    public void setCodeTime(String codeTime) {
        this.codeTime = codeTime;
    }
}
