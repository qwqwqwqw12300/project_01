package com.newlandnpt.varyar.common.utils.tcp.domain;

import java.util.List;

/**
 * 位置信息响应 MQ消息实体类
 * @author ljx
 * @date 2023/2/23
 */
public class LocationInfoResponseMqMsgEntity {
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
     * gps数据
     */
    private  gpsDatas gpsData;

    /**
     * LBS数据
     */
    private lbsData lbsData;

    /**
     * Wi-Fi数据
     */
    private List<wifiData> wifiData;

    public LocationInfoResponseMqMsgEntity() {
    }

    public LocationInfoResponseMqMsgEntity(String deviceNo, String iccid, String tranNo, String msgTime, gpsDatas gpsData, LocationInfoResponseMqMsgEntity.lbsData lbsData, List<LocationInfoResponseMqMsgEntity.wifiData> wifiData) {
        this.deviceNo = deviceNo;
        this.iccid = iccid;
        this.tranNo = tranNo;
        this.msgTime = msgTime;
        this.gpsData = gpsData;
        this.lbsData = lbsData;
        this.wifiData = wifiData;
    }

    /**
     * gps数据封装
     */
    public static class gpsDatas {
        /**
         * 定位状态
         */
        private String locationStatus;

        /**
         * 经度
         */
        private String longitude;

        /**
         * 纬度
         */
        private String latitude;

        /**
         * 定位时间
         */
        private String locationTime;

        public String getLocationStatus() {
            return locationStatus;
        }

        public void setLocationStatus(String locationStatus) {
            this.locationStatus = locationStatus;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLocationTime() {
            return locationTime;
        }

        public void setLocationTime(String locationTime) {
            this.locationTime = locationTime;
        }
    }

    /**
     * lbs数据封装
     */
    public static class lbsData {
        /**
         * 国家码
         */
        private String MCC;

        /**
         *
         */
        private String MNC;

        /**
         * 十进制数据
         */
        private String LAC;

        private String CID;

        /**
         * 信号强度
         */
        private String signalIntensity;

        public String getMCC() {
            return MCC;
        }

        public void setMCC(String MCC) {
            this.MCC = MCC;
        }

        public String getMNC() {
            return MNC;
        }

        public void setMNC(String MNC) {
            this.MNC = MNC;
        }

        public String getLAC() {
            return LAC;
        }

        public void setLAC(String LAC) {
            this.LAC = LAC;
        }

        public String getCID() {
            return CID;
        }

        public void setCID(String CID) {
            this.CID = CID;
        }

        public String getSignalIntensity() {
            return signalIntensity;
        }

        public void setSignalIntensity(String signalIntensity) {
            this.signalIntensity = signalIntensity;
        }

        public lbsData() {
        }


        public lbsData(String MCC, String MNC, String LAC, String CID, String signalIntensity) {
            this.MCC = MCC;
            this.MNC = MNC;
            this.LAC = LAC;
            this.CID = CID;
            this.signalIntensity = signalIntensity;
        }
    }

    /**
     * wifi数据封装
     */
    public static class wifiData{
        /**
         * wifi ID
         */
        private String SSID;

        /**
         * mac地址
         */
        private String MAC;

        /**
         * 信号强度
         */
        private String signalIntensity;

        public wifiData() {
        }

        public wifiData(String SSID, String MAC, String signalIntensity) {
            this.SSID = SSID;
            this.MAC = MAC;
            this.signalIntensity = signalIntensity;
        }

        public String getSSID() {
            return SSID;
        }

        public void setSSID(String SSID) {
            this.SSID = SSID;
        }

        public String getMAC() {
            return MAC;
        }

        public void setMAC(String MAC) {
            this.MAC = MAC;
        }

        public String getSignalIntensity() {
            return signalIntensity;
        }

        public void setSignalIntensity(String signalIntensity) {
            this.signalIntensity = signalIntensity;
        }
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

    public gpsDatas getGpsData() {
        return gpsData;
    }

    public void setGpsData(gpsDatas gpsData) {
        this.gpsData = gpsData;
    }

    public LocationInfoResponseMqMsgEntity.lbsData getLbsData() {
        return lbsData;
    }

    public void setLbsData(LocationInfoResponseMqMsgEntity.lbsData lbsData) {
        this.lbsData = lbsData;
    }

    public List<LocationInfoResponseMqMsgEntity.wifiData> getWifiData() {
        return wifiData;
    }

    public void setWifiData(List<LocationInfoResponseMqMsgEntity.wifiData> wifiData) {
        this.wifiData = wifiData;
    }
}
