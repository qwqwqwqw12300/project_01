package com.newlandnpt.varyar.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newlandnpt.varyar.common.constant.GeoConstant;
import com.newlandnpt.varyar.system.domain.req.TerminalReq;
import com.newlandnpt.varyar.system.domain.req.TraceReq;
import com.newlandnpt.varyar.system.domain.req.TrackAddReq;
import com.newlandnpt.varyar.system.service.GeoTrackService;
import com.newlandnpt.varyar.system.service.HttpSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lisd
 * @date 2023/1/3
 **/
@Service
@Slf4j
@SuppressWarnings({"ALL", "pmd:LowerCamelCaseVariableNamingRule"})
public class GeoTrackServiceImpl implements GeoTrackService {
    @Override
    public String addTrackService(TrackAddReq trackAddReq) {
        //构造请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("key", GeoConstant.GEO_KEY);
        paramsMap.put("name", trackAddReq.getName());
        paramsMap.put("desc", trackAddReq.getDesc());
        //发送请求
        String result = HttpSendService.sendPost(paramsMap, GeoConstant.GEO_SERVICE_ADD);
        JSONObject resultJson = JSON.parseObject(result);
        log.info("新增轨迹服务结果：{}", resultJson);
        return result;
    }

    @Override
    public String addTerminal(TerminalReq terminalReq) {
        //构造请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("key", GeoConstant.GEO_KEY);
        paramsMap.put("sid", GeoConstant.GEO_SERVICE_ID);
        paramsMap.put("name", terminalReq.getName());
        paramsMap.put("desc", terminalReq.getDesc());
        paramsMap.put("props", terminalReq.getProps());
        //发送请求
        String result = HttpSendService.sendPost(paramsMap, GeoConstant.GEO_TERMINAL_ADD);
        JSONObject resultJson = JSON.parseObject(result);
        log.info("创建终端结果：{}", resultJson);
        return result;
    }

    @Override
    public String listTerminal(TerminalReq terminalReq) {
        //构造请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("key", GeoConstant.GEO_KEY);
        paramsMap.put("sid", GeoConstant.GEO_SERVICE_ID);
        paramsMap.put("tid", terminalReq.getTid());
        paramsMap.put("name", terminalReq.getName());
        paramsMap.put("page", terminalReq.getPage().toString());
        //发送请求
        String result = HttpSendService.sendRequest(paramsMap, GeoConstant.GEO_TERMINAL_LIST);
        JSONObject resultJson = JSON.parseObject(result);
        log.info("查询终端结果：{}", resultJson);
        return result;
    }

    @Override
    public String addTrack(TraceReq traceReq) {
        //构造请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("key", GeoConstant.GEO_KEY);
        paramsMap.put("sid", GeoConstant.GEO_SERVICE_ID);
        paramsMap.put("tid", traceReq.getTid());
        paramsMap.put("trname", traceReq.getTrname());
        //发送请求
        String result = HttpSendService.sendPost(paramsMap, GeoConstant.GEO_TRACE_ADD);
        JSONObject resultJson = JSON.parseObject(result);
        log.info("创建轨迹结果：{}", resultJson);
        return result;
    }

    @Override
    public String uploadTrackPoints(TraceReq traceReq) {
        //构造请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("key", GeoConstant.GEO_KEY);
        paramsMap.put("sid", GeoConstant.GEO_SERVICE_ID);
        paramsMap.put("tid", traceReq.getTid());
        paramsMap.put("trid", traceReq.getTrid());
        paramsMap.put("points", traceReq.getPoints().toString());
        //发送请求
        String result = HttpSendService.sendPost(paramsMap, GeoConstant.GEO_TRACE_POINT_UPLOAD);
        JSONObject resultJson = JSON.parseObject(result);
        log.info("轨迹点上传结果：{}", resultJson);
        return result;
    }
}
