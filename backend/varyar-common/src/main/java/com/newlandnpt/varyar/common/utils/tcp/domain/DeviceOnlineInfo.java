package com.newlandnpt.varyar.common.utils.tcp.domain;

/**
 * 设备在线信息
 * @author ljx
 * @date 2023/2/27
 */
public class DeviceOnlineInfo {
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

    /**
     * 当前电量
     */
    private String currentPower;

    /**
     * 当前步数
     */
    private String currentSteps;

    /**
     * gps数据
     */
    private String gpsData;

    /**
     * LBS数据
     */
    private String lbsData;

    /**
     * Wi-Fi数据
     */
    private String wifiData;

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

    public String getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(String currentPower) {
        this.currentPower = currentPower;
    }

    public String getCurrentSteps() {
        return currentSteps;
    }

    public void setCurrentSteps(String currentSteps) {
        this.currentSteps = currentSteps;
    }

    public String getGpsData() {
        return gpsData;
    }

    public void setGpsData(String gpsData) {
        this.gpsData = gpsData;
    }

    public String getLbsData() {
        return lbsData;
    }

    public void setLbsData(String lbsData) {
        this.lbsData = lbsData;
    }

    public String getWifiData() {
        return wifiData;
    }

    public void setWifiData(String wifiData) {
        this.wifiData = wifiData;
    }
}
