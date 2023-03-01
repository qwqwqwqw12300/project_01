package com.newlandnpt.varyar.common.utils.tcp.domain;

/**
 * 终端登录mq消息实体
 * @author lin.ju
 * @date 2023/2/28
 */
public class DeviceLoginMqMsgEntity {

    /**
     * 设备编号
     */
    private String deviceNo;

    /**
     * 普通键数量(如值是5则表示终端有5个按键， 序号为1-5)
     */
    private String NormalKeyNum;
    /**
     * 终端有没有SOS键(0=没有, 1=有)
     */
    private String sosKey;

    /**
     * 终端类型(1=GPS, 2=CellID, 3=AGPS)
     */
    private String deviceType;
    /**
     * 终端是否支持区域报警终端侧解决方案功能(0= 不具备, 1=具备)
     */
    private String supportAreaAlert;
    /**
     * 终端是否具备设置呼入号码功能(0=不具备, 1= 具备)
     */
    private String supportCallIn;
    /**
     * 终端软件协议版本号,一共2位,第一位为大版本 号,第二位为小版本号,目前该字段值为21，小版本号 为0为每一版本的基础版本
     */
    private String protocolVersion;

    public DeviceLoginMqMsgEntity() {
    }

    public DeviceLoginMqMsgEntity(String normalKeyNum, String sosKey, String deviceType, String supportAreaAlert, String supportCallIn, String protocolVersion) {
        this.NormalKeyNum = normalKeyNum;
        this.sosKey = sosKey;
        this.deviceType = deviceType;
        this.supportAreaAlert = supportAreaAlert;
        this.supportCallIn = supportCallIn;
        this.protocolVersion = protocolVersion;
    }


    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getNormalKeyNum() {
        return NormalKeyNum;
    }
    public void setNormalKeyNum(String normalKeyNum) {
        this.NormalKeyNum = normalKeyNum;
    }
    public String getSosKey() {
        return sosKey;
    }
    public void setSosKey(String sosKey) {
        this.sosKey = sosKey;
    }
    public String getDeviceType() {
        return deviceType;
    }
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    public String getSupportAreaAlert() {
        return supportAreaAlert;
    }
    public void setSupportAreaAlert(String supportAreaAlert) {
        this.supportAreaAlert = supportAreaAlert;
    }
    public String getSupportCallIn() {
        return supportCallIn;
    }
    public void setSupportCallIn(String supportCallIn) {
        this.supportCallIn = supportCallIn;
    }
    public String getProtocolVersion() {
        return protocolVersion;
    }
    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

}
