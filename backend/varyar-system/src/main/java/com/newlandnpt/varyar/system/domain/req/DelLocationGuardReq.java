package com.newlandnpt.varyar.system.domain.req;

import java.util.ArrayList;

/**
 * 删除位置守护请求
 * @author ljx
 * @date 2023/3/2
 */
public class DelLocationGuardReq {

    /**
     * 高德位置守护
     */
    private ArrayList<String> geoLocationGuardId;

    public ArrayList<String> getGeoLocationGuardId() {
        return geoLocationGuardId;
    }

    public void setGeoLocationGuardId(ArrayList<String> geoLocationGuardId) {
        this.geoLocationGuardId = geoLocationGuardId;
    }
}
