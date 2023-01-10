package com.newlandnpt.varyar.system.domain.vo;

import lombok.Data;

/**
 * @author lisd
 * @date 2023/1/5
 **/
@Data
public class GeoFenceResultVo {

    /**
     * 围栏id
     */
    private Long gfid;

    /**
     * 围栏名称
     */
    private String gfname;

    /**
     * 坐标点是否在围栏区域内（0：不在 1：在）
     */
    private Integer in;
}
