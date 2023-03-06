package com.newlandnpt.varyar.tcp.gateway.handler.locationInfo;

import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.common.utils.tcp.domain.LocationInfoResponseMqMsgEntity;
import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 位置信息报文体
 * 报文体格式：gps位置信息@LBS数据@Wi-Fi数据
 * 字段说明如下：
 * 1.GPS位置信息：遵循NEMA报文格式GPS上报位置信息，参见附录A；
 *
 * 2.LBS数据：460,0,9231,2351,-30 : LBS 基站数据,MCC 国家码,460
 * 为中国,0:MNC,0为移动, 9231:LAC,十进制, 2351,CID, 十进制
 * 如 果 GPS 数 据 包 中 状 态 为 1, 或 者 经 纬 度 为 ”
 * 0000.0000N00000.0000E”时,则取 LBS 数据；
 *
 * 3.Wi-Fi数据：wifi!AC:BC:32:78:A2:5F!-97 : 一组WIFI信息，wifi为
 * SSID， AC:BC:32:78:A2:5F为MAC地址，-97为信号强度，变量之间用“!”分隔开WIFI信息可以有多组，
 * 每组之间用“#”分隔
 *
 * @author ljx
 * @date 2023/2/22
 */
public class LocationInfoReq extends MessageHead implements Req {
    /**
     * gps数据
     */
    private LocationInfoResponseMqMsgEntity.gpsDatas gpsData;

    /**
     * LBS数据
     */
    private LocationInfoResponseMqMsgEntity.lbsData lbsData;

    /**
     * Wi-Fi数据
     */
    private List<LocationInfoResponseMqMsgEntity.wifiData> wifiData;

    public LocationInfoReq() {
    }

    public LocationInfoReq(LocationInfoResponseMqMsgEntity.gpsDatas gpsData, LocationInfoResponseMqMsgEntity.lbsData lbsData, List<LocationInfoResponseMqMsgEntity.wifiData> wifiData) {
        this.gpsData = gpsData;
        this.lbsData = lbsData;
        this.wifiData = wifiData;
    }

    public LocationInfoResponseMqMsgEntity.gpsDatas getGpsData() {
        return gpsData;
    }

    public void setGpsData(LocationInfoResponseMqMsgEntity.gpsDatas gpsData) {
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

    @Override
    public void handleMessage(String body) {
        if(StringUtils.isBlank(body)){
            return ;
        }
        String[] str = body.split("@");
        // 设置gps
        LocationInfoResponseMqMsgEntity.gpsDatas gpsData = new LocationInfoResponseMqMsgEntity.gpsDatas();
        String temp = str[0].replaceAll("[A-Z]", "_$0");
        String[] gpsDataSplit = temp.split("_");
        gpsData.setLocationStatus(gpsDataSplit[0]);
        gpsData.setLongitude(gpsDataSplit[1]);
        gpsData.setLatitude(gpsDataSplit[2]);
        gpsData.setLocationTime(gpsDataSplit[3]);
        setGpsData(gpsData);
        // 设置lbs
        String[] lbsDataSplit = str[1].split("!");
        LocationInfoResponseMqMsgEntity.lbsData lbsData =
                new LocationInfoResponseMqMsgEntity.lbsData(lbsDataSplit[0],lbsDataSplit[1],lbsDataSplit[2],lbsDataSplit[3],Objects.isNull(lbsDataSplit[4]) ?"0":lbsDataSplit[4]);
        setLbsData(lbsData);
        // 设置wifi
        List<String> allWifiData = Arrays.asList(str[2].split("#"));
        List<LocationInfoResponseMqMsgEntity.wifiData> wifiData = new ArrayList<>();
        allWifiData.forEach(wifi->{
            String[] wifiDataSplit = wifi.split("!");
            wifiData.add(new LocationInfoResponseMqMsgEntity.wifiData(wifiDataSplit[0],wifiDataSplit[1],wifiDataSplit[2]));
        });
        setWifiData(wifiData);
    }
}
