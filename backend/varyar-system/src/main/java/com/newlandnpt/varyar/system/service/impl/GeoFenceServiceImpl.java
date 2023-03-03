package com.newlandnpt.varyar.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.newlandnpt.varyar.common.constant.GeoConstant;
import com.newlandnpt.varyar.system.domain.req.*;
import com.newlandnpt.varyar.system.service.GeoFenceService;
import com.newlandnpt.varyar.system.service.HttpSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lisd
 * @date 2023/1/3
 **/
@Service
public class GeoFenceServiceImpl implements GeoFenceService {

	private static final Logger log = LoggerFactory.getLogger(GeoFenceServiceImpl.class);

    @Override
    public String addCircleFence(CircleReq circleReq) {
        //构造请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("name", circleReq.getName());
        paramsMap.put("center", circleReq.getCenter());
        paramsMap.put("radius", circleReq.getRadius());
        paramsMap.put("key", GeoConstant.GEO_KEY);
        paramsMap.put("sid", GeoConstant.GEO_SERVICE_ID);
        //发送请求
        String result = HttpSendService.sendPost(paramsMap, GeoConstant.GEO_FENCE_CIRCLE_ADD);
        JSONObject resultJson = JSON.parseObject(result);
        log.info("新增圆形围栏结果：{}", resultJson);
        return result;
    }

    @Override
    public String updateCircleFence(CircleReq circleReq) {
        //构造请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("name", circleReq.getName());
        paramsMap.put("center", circleReq.getCenter());
        paramsMap.put("radius", circleReq.getRadius());
        paramsMap.put("key", GeoConstant.GEO_KEY);
        paramsMap.put("sid", GeoConstant.GEO_SERVICE_ID);
        paramsMap.put("gfid", circleReq.getGfid());
        //发送请求
        String result = HttpSendService.sendPost(paramsMap, GeoConstant.GEO_FENCE_CIRCLE_UPDATE);
        JSONObject resultJson = JSON.parseObject(result);
        log.info("更新圆形围栏结果：{}", resultJson);
        return result;
    }

    @Override
    public String addPolygonFence(PolygonReq polygonReq) {
        //构造请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("name", polygonReq.getName());
        paramsMap.put("points", polygonReq.getPoints());
        paramsMap.put("key", GeoConstant.GEO_KEY);
        paramsMap.put("sid", GeoConstant.GEO_SERVICE_ID);
        //发送请求
        String result = HttpSendService.sendPost(paramsMap, GeoConstant.GEO_FENCE_POLYGON_ADD);
        JSONObject resultJson = JSON.parseObject(result);
        log.info("新增多边形围栏结果：{}", resultJson);
        return result;
    }

    @Override
    public String updatePolygonFence(PolygonReq polygonReq) {
        //构造请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("name", polygonReq.getName());
        paramsMap.put("points", polygonReq.getPoints());
        paramsMap.put("key", GeoConstant.GEO_KEY);
        paramsMap.put("sid", GeoConstant.GEO_SERVICE_ID);
        paramsMap.put("gfid", polygonReq.getGfid());
        //发送请求
        String result = HttpSendService.sendPost(paramsMap, GeoConstant.GEO_FENCE_POLYGON_UPDATE);
        JSONObject resultJson = JSON.parseObject(result);
        log.info("更新多边形围栏结果：{}", resultJson);
        return result;
    }

    @Override
    public String deleteFence(DeleteFenceReq deleteFenceReq) {
        //构造请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("key", GeoConstant.GEO_KEY);
        paramsMap.put("sid", GeoConstant.GEO_SERVICE_ID);
        paramsMap.put("gfids", deleteFenceReq.getGfid());
        //发送请求
        String result = HttpSendService.sendPost(paramsMap, GeoConstant.GEO_FENCE_DELETE);
        JSONObject resultJson = JSON.parseObject(result);
        log.info("删除围栏结果：{}", resultJson);
        return result;
    }

    @Override
    public String getFenceList(FenceReq fenceReq) {
        //构造请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("key", GeoConstant.GEO_KEY);
        paramsMap.put("sid", GeoConstant.GEO_SERVICE_ID);
        paramsMap.put("outputshape", fenceReq.getOutputshape().toString());
        paramsMap.put("gfids", fenceReq.getGfids());
        paramsMap.put("page", fenceReq.getPage().toString());
        paramsMap.put("pagesize", fenceReq.getPagesize().toString());
        //发送请求
        String result = HttpSendService.sendRequest(paramsMap, GeoConstant.GEO_FENCE_LIST);
        JSONObject resultJson = JSON.parseObject(result);
        log.info("查询围栏结果：{}", resultJson);
        return result;
    }

    @Override
    public String getFenceLocationStatus(FenceReq fenceReq) {
        //构造请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("key", GeoConstant.GEO_KEY);
        paramsMap.put("sid", GeoConstant.GEO_SERVICE_ID);
        paramsMap.put("gfids", fenceReq.getGfids());
        paramsMap.put("location", fenceReq.getLocation());
        if (fenceReq.getPage() != null) {
            paramsMap.put("page", fenceReq.getPage().toString());
        }
        if (fenceReq.getPagesize() != null) {
            paramsMap.put("pagesize", fenceReq.getPagesize().toString());
        }
        //发送请求
        String result = HttpSendService.sendRequest(paramsMap, GeoConstant.GEO_FENCE_STATUS);
        JSONObject resultJson = JSON.parseObject(result);
        log.info("查询坐标与围栏关系结果：{}", resultJson);
        return result;
    }

    @Override
    public String bindFenceTerminal(FenceReq fenceReq) {
        //构造请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("key", GeoConstant.GEO_KEY);
        paramsMap.put("sid", GeoConstant.GEO_SERVICE_ID);
        paramsMap.put("gfid", fenceReq.getGfids());
        paramsMap.put("tids", fenceReq.getTids());
        //发送请求
        String result = HttpSendService.sendPost(paramsMap, GeoConstant.GEO_FENCE_TERMINAL_BIND);
        JSONObject resultJson = JSON.parseObject(result);
        log.info("增加监测对象结果：{}", resultJson);
        return result;
    }

    @Override
    public String getFenceTerminalStatus(FenceReq fenceReq) {
        //构造请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("key", GeoConstant.GEO_KEY);
        paramsMap.put("sid", GeoConstant.GEO_SERVICE_ID);
        paramsMap.put("tid", fenceReq.getTids());
        paramsMap.put("gfids", fenceReq.getGfids());
        paramsMap.put("page", fenceReq.getPage().toString());
        paramsMap.put("pagesize", fenceReq.getPagesize().toString());
        //发送请求
        String result = HttpSendService.sendRequest(paramsMap, GeoConstant.GEO_FENCE_TERMINAL_STATUS);
        JSONObject resultJson = JSON.parseObject(result);
        JSONObject resultsJson = JSON.parseObject(JSON.parseObject(resultJson.get("data").toString()).get("results").toString());
        int in = (int) resultsJson.get("in");
        log.info("查询检测对象与围栏关系结果：{}", resultJson);
        return result;
    }

    @Override
    public String addTerminal(TerminalAddReq terminalAddReq) {

        //构造请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("key", GeoConstant.GEO_KEY);
        paramsMap.put("sid", GeoConstant.GEO_SERVICE_ID);
        paramsMap.put("name", terminalAddReq.getName());
        paramsMap.put("desc", terminalAddReq.getDesc());
        paramsMap.put("props", terminalAddReq.getProps());
        //发送请求
        String result = HttpSendService.sendRequest(paramsMap, GeoConstant.GEO_TERMINAL_ADD);
        JSONObject resultJson = JSON.parseObject(result);
        JSONObject resultsJson = JSON.parseObject(JSON.parseObject(resultJson.get("data").toString()).get("results").toString());


        return resultsJson.getString("tid");
    }

    @Override
    public String findTerminalIdByName(String name) {
        //构造请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("key", GeoConstant.GEO_KEY);
        paramsMap.put("sid", GeoConstant.GEO_SERVICE_ID);
        paramsMap.put("name", name);
        //发送请求
        String result = HttpSendService.sendRequest(paramsMap, GeoConstant.GEO_TERMINAL_ADD);
        JSONObject resultJson = JSON.parseObject(result);
        JSONArray resultsJson = resultJson.getJSONArray("data.results");

        if(resultsJson.isEmpty()){
            return null;
        }
        return resultsJson.getJSONObject(0).getString("tid");
    }

    public static void main(String[] args) {

        //构造请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("key", "2ecab3d1b4d0f57a9661b76f0c5cc3a0");
        paramsMap.put("name", "猎鹰");
        //发送请求
        String result = HttpSendService.sendPost(paramsMap, "https://tsapi.amap.com/v1/track/service/add");

        System.out.println(result);
    }
}
