package com.newlandnpt.varyar.api.controller.business.location;

import com.newlandnpt.varyar.system.domain.req.*;
import com.newlandnpt.varyar.system.service.GeoFenceService;
import com.newlandnpt.varyar.system.service.GeoTrackService;
import com.newlandnpt.varyar.common.annotation.Anonymous;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 地理围栏
 *
 * @author lisd
 * @date 2022/12/25
 **/
@RestController
@RequestMapping("/api/geo")
@Slf4j
public class GeoController {

    @Autowired
    GeoFenceService geoFenceService;

    @Autowired
    GeoTrackService geoTrackService;

    /**
     * 创建圆形地理围栏
     */
    @PostMapping("/fence/circle")
    @Anonymous
    public String addGeoFenceCircle(@RequestBody CircleReq circleReq) {
        return geoFenceService.addCircleFence(circleReq);
    }

    /**
     * 查询围栏
     */
    @PostMapping("/fence/list")
    @Anonymous
    public String getGeoFenceList(@RequestBody FenceReq fenceReq) {
        return geoFenceService.getFenceList(fenceReq);
    }

    /**
     * 查询指定坐标与围栏关系
     */
    @PostMapping("/fence/location")
    @Anonymous
    public String getGeoFenceLocationStatus(@RequestBody FenceReq fenceReq) {
        return geoFenceService.getFenceLocationStatus(fenceReq);
    }

    /**
     * 围栏对象管理-增加监测对象
     */
    @PostMapping("/fence/bind")
    @Anonymous
    public String bindGeoFenceTerminal(@RequestBody FenceReq fenceReq) {
        return geoFenceService.bindFenceTerminal(fenceReq);
    }

    /**
     * 查询检测对象与围栏关系
     */
    @PostMapping("/fence/terminal")
    @Anonymous
    public String getGeoFenceTerminalStatus(@RequestBody FenceReq fenceReq) {
        return geoFenceService.getFenceTerminalStatus(fenceReq);
    }

    /**
     * 创建轨迹服务
     */
    @PostMapping("/track/service/add")
    @Anonymous
    public String addGeoTrackService(@RequestBody TrackAddReq trackAddReq) {
        return geoTrackService.addTrackService(trackAddReq);
    }

    /**
     * 创建终端
     */
    @PostMapping("/track/terminal/add")
    @Anonymous
    public String addGeoTrackTerminal(@RequestBody TerminalReq terminalReq) {
        return geoTrackService.addTerminal(terminalReq);
    }

    /**
     * 查询终端
     */
    @PostMapping("/track/terminal/list")
    @Anonymous
    public String getGeoTerminalList(@RequestBody TerminalReq terminalReq) {
        return geoTrackService.listTerminal(terminalReq);
    }

    /**
     * 创建轨迹
     */
    @PostMapping("/track/trace/add")
    @Anonymous
    public String addGeoTrace(@RequestBody TraceReq traceReq) {
        return geoTrackService.addTrack(traceReq);
    }

    /**
     * 创建轨迹
     */
    @PostMapping("/track/trace/upload")
    @Anonymous
    public String uploadGeoTracePoints(@RequestBody TraceReq traceReq) {
        return geoTrackService.uploadTrackPoints(traceReq);
    }

}
