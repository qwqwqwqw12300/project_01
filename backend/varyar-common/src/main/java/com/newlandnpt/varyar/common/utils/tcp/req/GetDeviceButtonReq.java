package com.newlandnpt.varyar.common.utils.tcp.req;

/**
 * 获取亲情号码
 * @author ljx
 * @date 2023/3/2
 */
public class GetDeviceButtonReq {
    /**
     * 设备号,设备唯一编号
     */
    private String deviceNo;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }
}
