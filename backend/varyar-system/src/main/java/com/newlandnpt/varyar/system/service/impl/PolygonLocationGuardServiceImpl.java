package com.newlandnpt.varyar.system.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newlandnpt.varyar.common.annotation.LocationGuardType;
import com.newlandnpt.varyar.common.constant.Constants;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.system.domain.LocationGuard;
import com.newlandnpt.varyar.system.domain.req.DelLocationGuardReq;
import com.newlandnpt.varyar.system.domain.req.PolygonReq;
import com.newlandnpt.varyar.system.domain.vo.GeoResultVo;
import com.newlandnpt.varyar.system.service.GeoFenceService;
import com.newlandnpt.varyar.system.service.ILocationGuardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 多边形位置守护
 * @author ljx
 * @date 2023/3/2
 */
@LocationGuardType(value = "polygon",name = "多边形位置守护")
@Service
public class PolygonLocationGuardServiceImpl implements ILocationGuardService {

    private static final Logger log = LoggerFactory.getLogger(CircleLocationGuardServiceImpl.class);

    @Autowired
    private GeoFenceService geoFenceService;

    @Override
    public LocationGuard insertLocationGuard(LocationGuard locationGuard) {
        //调用高德地理围栏api
        try{
            PolygonReq polygonReq = new PolygonReq();
            polygonReq.setName("locationGuard_"+ IdUtil.simpleUUID());
            polygonReq.setPoints(locationGuard.getPoints());
            String result = geoFenceService.addPolygonFence(polygonReq);
            JSONObject jsonObject = JSON.parseObject(result);
            GeoResultVo geoResultVo = jsonObject.toJavaObject(GeoResultVo.class);
            if (!geoResultVo.getErrcode().equals(Constants.GEO_RESP_SUCCESS)) {
                throw new Exception();
            }
            Long geoFenceId = Long.parseLong(geoResultVo.getData().get("gfid").toString());
            locationGuard.setGeoFenceId(geoFenceId);
        }catch (Exception e){
            log.error("调用高德地图api-新增多边形围栏-响应失败");
        }
        //高德地理围栏api响应成功，将围栏信息插入至数据库
        locationGuard.setCreateTime(DateUtils.getNowDate());
        return locationGuard;
    }

    @Override
    public AjaxResult updateLocationGuard(LocationGuard locationGuard) {
        return null;
    }

    @Override
    public Integer deleteLocationGuard(DelLocationGuardReq delReq) {
        //调用高德地理围栏api
        try{

        }catch (Exception e){
            log.error("调用高德地图api-新增多边形围栏-响应失败");
        }
        //高德地理围栏api响应成功，将围栏信息插入至数据库
        return null;
    }
}
