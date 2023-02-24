package com.newlandnpt.varyar.common.utils.SMS.templates;

import java.util.Arrays;
import java.util.List;

/**
 * 短信家庭分享模板
 * @author ljx
 * @date 2023/2/22
 */
public class FamilyShare implements SMSTemplate {

    /**
     * 手机号(中间可能需要掩码)
     */
    private String telNumber;

    /**
     * 下载链接(总长度不能超过20)
     */
    private String downUrl;

    public FamilyShare(String telNumber, String downUrl) {
        this.telNumber = telNumber;
        this.downUrl = downUrl;
    }

    @Override
    public String getTemplateId() {
        return "1a73f48045cc454a8c991fce5f914be7";
    }

    @Override
    public List<String> getParams() {
        return Arrays.asList(telNumber,downUrl);
    }

    @Override
    public Boolean isCorrectParams() {
        return
                ParamsVaildate.isNotNull(getTelNumber()) &&
                ParamsVaildate.isRanger(getTelNumber(),10) &&
                ParamsVaildate.isNotNull(getDownUrl()) &&
                ParamsVaildate.isRanger(getDownUrl(),20) &&
                ParamsVaildate.isPattern(getTelNumber(),ParamsVaildate.CHAR_STAR);
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }
}
