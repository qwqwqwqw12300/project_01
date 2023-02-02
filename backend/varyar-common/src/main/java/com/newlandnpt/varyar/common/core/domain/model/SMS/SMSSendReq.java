package com.newlandnpt.varyar.common.core.domain.model.SMS;

public class SMSSendReq {

    /**集团客户名称*/
    private String ecName;
    /**用户名*/
    private String apId;
    /**密码*/
    private String secretKey;
    /**手机号码逗号分隔。(如“1813728****,1813728****,1813728****”)*/
    private String mobiles;
    /**发送短信内容*/
    private String content;
    /**网关签名编码，必填，签名编码在中国移动集团开通帐号后分配，可以在云MAS网页端管理子系统-SMS接口管理功能中下载。*/
    private String sign;
    /**扩展码，根据向移动公司申请的通道填写，如果申请的精确匹配通道，则填写空字符串("")，否则添加移动公司允许的扩展码。*/
    private String addSerial;
    /**按文档要求加密后的mac*/
    private String mac;

    public String getEcName() {
        return ecName;
    }

    public void setEcName(String ecName) {
        this.ecName = ecName;
    }

    public String getApId() {
        return apId;
    }

    public void setApId(String apId) {
        this.apId = apId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getMobiles() {
        return mobiles;
    }

    public void setMobiles(String mobiles) {
        this.mobiles = mobiles;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAddSerial() {
        return addSerial;
    }

    public void setAddSerial(String addSerial) {
        this.addSerial = addSerial;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
