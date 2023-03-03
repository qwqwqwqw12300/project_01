package com.newlandnpt.varyar.common.utils.tcp.req;

import com.newlandnpt.varyar.common.core.domain.entity.DeviceIncomingCall;

import java.util.List;

/**
 * 批量新增与修改通讯录请求
 * @author ljx
 * @date 2023/3/3
 */
public class BatchAddOrUpdateAddressBookReq {
    /**
     * 设备No
     */
    private String deviceNo;

    /**
     *
     */
    public List<DeviceIncomingCall> addressBooks;

    public BatchAddOrUpdateAddressBookReq() {
    }

    public BatchAddOrUpdateAddressBookReq(String deviceNo, List<DeviceIncomingCall> addressBooks) {
        this.deviceNo = deviceNo;
        this.addressBooks = addressBooks;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public List<DeviceIncomingCall> getAddressBooks() {
        return addressBooks;
    }

    public void setAddressBooks(List<DeviceIncomingCall> addressBooks) {
        this.addressBooks = addressBooks;
    }
}
