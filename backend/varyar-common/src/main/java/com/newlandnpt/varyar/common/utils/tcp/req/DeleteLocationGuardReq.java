package com.newlandnpt.varyar.common.utils.tcp.req;

import java.util.List;

/**
 * 删除位置守护请求
 * @author ljx
 * @date 2023/3/5
 */
public class DeleteLocationGuardReq {
    /**
     * 设备No
     */
    private String deviceNo;

    /**
     * uuid
     */
    private List<String> uuidList;


    public DeleteLocationGuardReq(String deviceNo, List<String> uuidList) {
        this.deviceNo = deviceNo;
        this.uuidList = uuidList;
    }

    public DeleteLocationGuardReq() {
    }



    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public List<String> getUuidList() {
        return uuidList;
    }

    public void setUuidList(List<String> uuidList) {
        this.uuidList = uuidList;
    }
}
