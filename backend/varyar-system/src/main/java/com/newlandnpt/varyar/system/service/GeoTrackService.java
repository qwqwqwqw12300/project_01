package com.newlandnpt.varyar.system.service;

import com.newlandnpt.varyar.system.domain.req.TerminalReq;
import com.newlandnpt.varyar.system.domain.req.TraceReq;
import com.newlandnpt.varyar.system.domain.req.TrackAddReq;

/**
 * 高德API-轨迹服务接口
 * @author lisd
 * @date 2023/1/3
 **/
public interface GeoTrackService {

    /**
     * 创建轨迹服务
     * @param trackAddReq
     * @return resultJson
     */
    String addTrackService(TrackAddReq trackAddReq);

    /**
     * 创建终端
     * @param terminalReq
     * @return resultJson
     */
    String addTerminal(TerminalReq terminalReq);

    /**
     * 查询终端
     * @param terminalReq
     * @return resultJson
     */
    String listTerminal(TerminalReq terminalReq);

    /**
     * 创建轨迹
     * @param traceReq
     * @return resultJson
     */
    String addTrack(TraceReq traceReq);

    /**
     * 轨迹点上传
     * @param traceReq
     * @return resultJson
     */
    String uploadTrackPoints(TraceReq traceReq);

}
