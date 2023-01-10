package com.newlandnpt.varyar.system.domain.req;

import lombok.Data;

/**
 * @author lisd
 * @date 2023/1/5
 **/
@Data
public class LocationMsgReq {
    /**
     * 设备编号
     */
    private String deviceNo;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

}
