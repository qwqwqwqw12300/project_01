package com.newlandnpt.varyar.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 高德地理API相关常量
 * @author lisd
 * @date 2022/12/26
 **/
@Configuration
public class GeoConstant {
    /**
     * 高德API请求服务权限标识
     */
    public static String GEO_KEY;

    /**
     * 高德API猎鹰服务ID
     */
    public static String GEO_SERVICE_ID;

    /**
     * 创建猎鹰轨迹服务
     */
    public static String GEO_SERVICE_ADD;

    /**
     * 终端管理-创建终端
     */
    public static String GEO_TERMINAL_ADD;

    /**
     * 终端管理-查询终端
     */
    public static String GEO_TERMINAL_LIST;

    /**
     * 轨迹管理-创建轨迹
     */
    public static String GEO_TRACE_ADD;

    /**
     * 轨迹管理-轨迹点上传
     */
    public static String GEO_TRACE_POINT_UPLOAD;

    /**
     * 轨迹管理-查询轨迹信息
     */
    public static String GEO_TRACE_SEARCH;

    /**
     * 创建圆形围栏
     */
    public static String GEO_FENCE_CIRCLE;

    /**
     * 查询围栏列表
     */
    public static String GEO_FENCE_LIST;

    /**
     * 查询指定坐标与围栏关系
     */
    public static String GEO_FENCE_STATUS;

    /**
     * 围栏对象管理-增加监测终端对象
     */
    public static String GEO_FENCE_TERMINAL_BIND;

    /**
     * 查询检测对象与围栏关系
     */
    public static String GEO_FENCE_TERMINAL_STATUS;


    public String getGeoKey() {
        return GeoConstant.GEO_KEY;
    }

    @Value("${location.gaode.key}")
    public void setGeoKey(String geoKey) {
        GeoConstant.GEO_KEY = geoKey;
    }

    public String getGeoServiceId() {
        return GeoConstant.GEO_SERVICE_ID;
    }

    @Value("${location.gaode.sid}")
    public void setGeoServiceId(String geoServiceId) {
        GeoConstant.GEO_SERVICE_ID = geoServiceId;
    }

    public String getGeoServiceAdd() {
        return GeoConstant.GEO_SERVICE_ADD;
    }

    @Value("${location.gaode.trackServiceAdd}")
    public void setGeoServiceAdd(String geoServiceAdd) {
        GeoConstant.GEO_SERVICE_ADD = geoServiceAdd;
    }

    public String getGeoTerminalAdd() {
        return GeoConstant.GEO_TERMINAL_ADD;
    }

    @Value("${location.gaode.trackTerminalAdd}")
    public void setGeoTerminalAdd(String geoTerminalAdd) {
        GeoConstant.GEO_TERMINAL_ADD = geoTerminalAdd;
    }

    public String getGeoTerminalList() {
        return GeoConstant.GEO_TERMINAL_LIST;
    }

    @Value("${location.gaode.trackTerminalList}")
    public void setGeoTerminalList(String geoTerminalList) {
        GeoConstant.GEO_TERMINAL_LIST = geoTerminalList;
    }

    public String getGeoTraceAdd() {
        return GeoConstant.GEO_TRACE_ADD;
    }

    @Value("${location.gaode.trackTraceAdd}")
    public void setGeoTraceAdd(String geoTraceAdd) {
        GeoConstant.GEO_TRACE_ADD = geoTraceAdd;
    }

    public String getGeoTracePointUpload() {
        return GeoConstant.GEO_TRACE_POINT_UPLOAD;
    }

    @Value("${location.gaode.trackPointUpload}")
    public void setGeoTracePointUpload(String geoTracePointUpload) {
        GeoConstant.GEO_TRACE_POINT_UPLOAD = geoTracePointUpload;
    }

    public String getGeoTraceSearch() {
        return GeoConstant.GEO_TRACE_SEARCH;
    }

    @Value("${location.gaode.trackTerminalTrsearch}")
    public void setGeoTraceSearch(String geoTraceSearch) {
        GeoConstant.GEO_TRACE_SEARCH = geoTraceSearch;
    }

    public String getGeoFenceCircle() {
        return GeoConstant.GEO_FENCE_CIRCLE;
    }

    @Value("${location.gaode.fenceCircle}")
    public void setGeoFenceCircle(String geoFenceCircle) {
        GeoConstant.GEO_FENCE_CIRCLE = geoFenceCircle;
    }

    public String getGeoFenceList() {
        return GeoConstant.GEO_FENCE_LIST;
    }

    @Value("${location.gaode.fenceList}")
    public void setGeoFenceList(String geoFenceList) {
        GeoConstant.GEO_FENCE_LIST = geoFenceList;
    }

    public String getGeoFenceStatus() {
        return GeoConstant.GEO_FENCE_STATUS;
    }

    @Value("${location.gaode.fenceLocation}")
    public void setGeoFenceStatus(String geoFenceStatus) {
        GeoConstant.GEO_FENCE_STATUS = geoFenceStatus;
    }

    public String getGeoFenceTerminalBind() {
        return GeoConstant.GEO_FENCE_TERMINAL_BIND;
    }

    @Value("${location.gaode.fenceTerminalBind}")
    public void setGeoFenceTerminalBind(String geoFenceTerminalBind) {
        GeoConstant.GEO_FENCE_TERMINAL_BIND = geoFenceTerminalBind;
    }

    public String getGeoFenceTerminalStatus() {
        return GeoConstant.GEO_FENCE_TERMINAL_STATUS;
    }

    @Value("${location.gaode.fenceTerminalStatus}")
    public void setGeoFenceTerminalStatus(String geoFenceTerminalStatus) {
        GeoConstant.GEO_FENCE_TERMINAL_STATUS = geoFenceTerminalStatus;
    }

}
