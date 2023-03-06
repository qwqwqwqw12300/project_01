package com.newlandnpt.varyar.common.utils.tcp.req;

/**
 * 删除时段禁用
 * @author ljx
 * @date 2023/3/4
 */
public class DeletePeriodDisabledReq {

    /**
     * 设备号,设备唯一编号
     */
    private String deviceNo;

    /**
     * 需要删除的所有uuid
     */
    private String[] uuidList;

    public DeletePeriodDisabledReq() {
    }

    public DeletePeriodDisabledReq(String deviceNo, String[] uuidList) {
        this.deviceNo = deviceNo;
        this.uuidList = uuidList;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String[] getUuidList() {
        return uuidList;
    }

    public void setUuidList(String[] uuidList) {
        this.uuidList = uuidList;
    }
}
