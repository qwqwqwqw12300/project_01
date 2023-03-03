package com.newlandnpt.varyar.system.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newlandnpt.varyar.common.annotation.LocationGuardType;
import com.newlandnpt.varyar.common.constant.Constants;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.system.domain.LocationGuard;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.domain.req.CircleReq;
import com.newlandnpt.varyar.system.domain.req.DelLocationGuardReq;
import com.newlandnpt.varyar.system.domain.vo.GeoResultVo;
import com.newlandnpt.varyar.system.service.GeoFenceService;
import com.newlandnpt.varyar.system.service.IDeviceService;
import com.newlandnpt.varyar.system.service.ILocationGuardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 圆形位置守护
 * @author ljx
 * @date 2022/3/2
 */
@LocationGuardType(value = "circle",name = "圆形位置守护")
@Service
public class CircleLocationGuardServiceImpl implements ILocationGuardService {

    private static final Logger log = LoggerFactory.getLogger(CircleLocationGuardServiceImpl.class);

    @Autowired
    private GeoFenceService geoFenceService;

    @Autowired
    private IDeviceService iDeviceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LocationGuard insertLocationGuard(LocationGuard locationGuard) {
        //调用高德地理围栏api
        try {
            CircleReq circleReq = new CircleReq();
            circleReq.setName("LocationGuard_"+IdUtil.simpleUUID());
            circleReq.setCenter(locationGuard.getLongitude() + "," + locationGuard.getLatitude());
            circleReq.setRadius(locationGuard.getRadius());
            String result = geoFenceService.addCircleFence(circleReq);
            JSONObject jsonObject = JSON.parseObject(result);
            GeoResultVo geoResultVo = jsonObject.toJavaObject(GeoResultVo.class);
            if (!geoResultVo.getErrcode().equals(Constants.GEO_RESP_SUCCESS)) {
                throw new Exception();
            }
            Long geoFenceId = Long.parseLong(geoResultVo.getData().get("gfid").toString());
            locationGuard.setGeoFenceId(geoFenceId);
        } catch (Exception e) {
            log.error("调用高德地图api-新增圆形围栏-响应失败");
        }
        //高德地理围栏api响应成功，将围栏信息插入至数据库
        return locationGuard;
    }

    @Override
    public Integer updateLocationGuard(LocationGuard locationGuard) {
        //调用高德地理围栏api
        try {
            CircleReq circleReq = new CircleReq();
            circleReq.setGfid(locationGuard.getGeoFenceId().toString());
            circleReq.setName("LocationGuard_"+IdUtil.simpleUUID());
            circleReq.setCenter(locationGuard.getLongitude() + "," + locationGuard.getLatitude());
            circleReq.setRadius(locationGuard.getRadius());
            String result = geoFenceService.updateCircleFence(circleReq);
            JSONObject jsonObject = JSON.parseObject(result);
            GeoResultVo geoResultVo = jsonObject.toJavaObject(GeoResultVo.class);
            if (!geoResultVo.getErrcode().equals(Constants.GEO_RESP_SUCCESS)) {
                throw new Exception();
            }
        } catch (Exception e) {
            log.error("调用高德地图api-更新圆形围栏-响应失败");
            return 0;
        }
        return 1;
    }

}
