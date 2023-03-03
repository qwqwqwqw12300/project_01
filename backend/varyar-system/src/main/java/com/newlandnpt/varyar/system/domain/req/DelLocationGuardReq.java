package com.newlandnpt.varyar.system.domain.req;

/**
 * 删除位置守护请求
 * @author ljx
 * @date 2023/3/2
 */
public class DelLocationGuardReq {
    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 高德位置守护
     */
    private Long[] geoLocationGuardId;

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long[] getGeoLocationGuardId() {
        return geoLocationGuardId;
    }

    public void setGeoLocationGuardId(Long[] geoLocationGuardId) {
        this.geoLocationGuardId = geoLocationGuardId;
    }
}
