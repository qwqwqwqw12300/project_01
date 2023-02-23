package com.newlandnpt.varyar.common.utils.tcp.domain;
/**
 * sos警告上报响应MQ消息实体类
 * @author ljx
 * @date 2023/2/23
 */
public class SosWarnResponseMqMsgEntity {
    /**
     * 设备号,设备唯一编号
     */
    private String deviceNo;

    /**
     * SIM 卡卡号
     */
    private String iccid;

    /**
     * 交易流水号
     * 时间格式+4位循环数，时间格式 : yyyyMMddHHmmss 4 位循环数范围 0..9999，从 0 开始， 递增赋值，步长为 1， 增加到 9999 后，再从 0 开始
     */
    private String tranNo;

    /**
     * 报文时间
     * 设备或者服务器时间，时间格式为： yyyyMMddHHmmss
     */
    private String msgTime;

    public SosWarnResponseMqMsgEntity(String deviceNo, String iccid, String tranNo, String msgTime) {
        this.deviceNo = deviceNo;
        this.iccid = iccid;
        this.tranNo = tranNo;
        this.msgTime = msgTime;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getTranNo() {
        return tranNo;
    }

    public void setTranNo(String tranNo) {
        this.tranNo = tranNo;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }
}
