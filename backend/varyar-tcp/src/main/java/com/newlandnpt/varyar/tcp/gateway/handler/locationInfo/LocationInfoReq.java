package com.newlandnpt.varyar.tcp.gateway.handler.locationInfo;

import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;

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
    private String gpsData="";

    /**
     * LBS数据
     */
    private String lbsData="";

    /**
     * Wi-Fi数据
     */
    private String wifiData="";

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

    @Override
    public void handleMessage(String body) {
        if(StringUtils.isBlank(body)){
            return ;
        }
        String[] str = body.split("@");
        setGpsData(str[0]);
        setLbsData(str[1]);
        setWifiData(str[2]);
    }
}
