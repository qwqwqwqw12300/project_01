package com.newlandnpt.varyar.system.service;

import com.newlandnpt.varyar.system.domain.req.CircleReq;
import com.newlandnpt.varyar.system.domain.req.FenceReq;

/**
 * 高德API-围栏服务接口
 * @author lisd
 * @date 2023/1/3
 **/
public interface GeoFenceService {

    /**
     * 创建圆形地理围栏
     * @param circleReq
     * @return resultJson
     */
    String addCircleFence(CircleReq circleReq);

    /**
     * 查询围栏
     * @param fenceReq
     * @return resultJson
     */
    String getFenceList(FenceReq fenceReq);

    /**
     * 查询坐标与围栏的位置关系
     * @param fenceReq
     * @return resultJson
     */
    String getFenceLocationStatus(FenceReq fenceReq);

    /**
     * 围栏对象管理-增加监测对象
     * @param fenceReq
     * @return resultJson
     */
    String bindFenceTerminal(FenceReq fenceReq);

    /**
     * 查询检测对象与围栏关系
     * @param fenceReq
     * @return resultJson
     */
    String getFenceTerminalStatus(FenceReq fenceReq);

}
