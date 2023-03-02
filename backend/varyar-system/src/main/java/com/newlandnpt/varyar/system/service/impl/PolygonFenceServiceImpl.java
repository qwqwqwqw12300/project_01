package com.newlandnpt.varyar.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newlandnpt.varyar.common.annotation.Fence;
import com.newlandnpt.varyar.common.constant.Constants;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.system.domain.TDeviceFence;
import com.newlandnpt.varyar.system.domain.req.PolygonReq;
import com.newlandnpt.varyar.system.domain.vo.GeoResultVo;
import com.newlandnpt.varyar.system.mapper.DeviceFenceMapper;
import com.newlandnpt.varyar.system.service.GeoFenceService;
import com.newlandnpt.varyar.system.service.IDeviceFenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 多边形电子围栏
 * @author ljx
 * @date 2023/3/1
 */
@Fence(value = "polygon", name="多边形电子围栏")
@Service
public class PolygonFenceServiceImpl implements IDeviceFenceService {

    private static final Logger log = LoggerFactory.getLogger(PolygonFenceServiceImpl.class);


    @Autowired
    private DeviceFenceMapper deviceFenceMapper;

    @Autowired
    private GeoFenceService geoFenceService;

    /**
     * 查询设备电子围栏
     *
     * @param deviceFenceId 设备电子围栏主键
     * @return 设备电子围栏
     */
    @Override
    public TDeviceFence selectTDeviceFenceByDeviceFenceId(Long deviceFenceId) {
        return deviceFenceMapper.selectTDeviceFenceByDeviceFenceId(deviceFenceId);
    }

    @Override
    public TDeviceFence selectTDeviceFenceByDeviceNo(String deviceNo) {
        return deviceFenceMapper.selectTDeviceFenceByDeviceNo(deviceNo);
    }

    @Override
    public List<TDeviceFence> selectTDeviceFenceByDeviceId(Long deviceId) {
        return deviceFenceMapper.selectTDeviceFenceByDeviceId(deviceId);
    }

    @Override
    public List<TDeviceFence> selectTDeviceFenceList(TDeviceFence tDeviceFence) {
        return deviceFenceMapper.selectTDeviceFenceList(tDeviceFence);
    }

    /**
     * 新增设备电子围栏
     *
     * @param tDeviceFence 设备电子围栏
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTDeviceFence(TDeviceFence tDeviceFence) {
        //获取设备id,存在则修改原记录
        TDeviceFence tDeviceFenceQuery = deviceFenceMapper.selectTDeviceFenceByDeviceFenceId(tDeviceFence.getDeviceFenceId());
        if (tDeviceFenceQuery != null) {
            return updateTDeviceFence(tDeviceFence);
        }
        //调用高德地理围栏api
        try{
            PolygonReq polygonReq = new PolygonReq();
            polygonReq.setName(tDeviceFence.getDeviceNo() + "_fence");
            polygonReq.setPoints(tDeviceFence.getPoints());
            String result = geoFenceService.addPolygonFence(polygonReq);
            JSONObject jsonObject = JSON.parseObject(result);
            GeoResultVo geoResultVo = jsonObject.toJavaObject(GeoResultVo.class);
            if (!geoResultVo.getErrcode().equals(Constants.GEO_RESP_SUCCESS)) {
                throw new Exception();
            }
            Long geoFenceId = Long.parseLong(geoResultVo.getData().get("gfid").toString());
            tDeviceFence.setGeoFenceId(geoFenceId);
        }catch (Exception e){
            log.error("调用高德地图api-新增多边形围栏-响应失败");
            return 0;
        }
        //高德地理围栏api响应成功，将围栏信息插入至数据库
        tDeviceFence.setCreateTime(DateUtils.getNowDate());
        return deviceFenceMapper.insertTDeviceFence(tDeviceFence);
    }

    /**
     * 修改设备电子围栏
     *
     * @param tDeviceFence 设备电子围栏
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTDeviceFence(TDeviceFence tDeviceFence) {
        //调用高德地理围栏api
        try{
            PolygonReq polygonReq = new PolygonReq();
            polygonReq.setGfid(tDeviceFence.getGeoFenceId().toString());
            polygonReq.setName(tDeviceFence.getDeviceNo() + "_fence");
            polygonReq.setPoints(tDeviceFence.getPoints());
            String result = geoFenceService.updatePolygonFence(polygonReq);
            JSONObject jsonObject = JSON.parseObject(result);
            GeoResultVo geoResultVo = jsonObject.toJavaObject(GeoResultVo.class);
            if (!geoResultVo.getErrcode().equals(Constants.GEO_RESP_SUCCESS)) {
                throw new Exception();
            }
        }catch (Exception e) {
            log.error("调用高德地图api-更新多边形围栏-响应失败");
            return 0;
        }
        //高德地理围栏api响应成功，将围栏信息更新至数据库
        tDeviceFence.setUpdateTime(DateUtils.getNowDate());
        return deviceFenceMapper.updateTDeviceFence(tDeviceFence);
    }

    /**
     * 批量删除设备电子围栏
     *
     * @param deviceFenceIds 需要删除的设备电子围栏主键
     * @return 结果
     */
    @Override
    public int deleteTDeviceFenceByDeviceFenceIds(Long[] deviceFenceIds) {
        return deviceFenceMapper.deleteTDeviceFenceByDeviceFenceIds(deviceFenceIds);
    }

    /**
     * 删除设备电子围栏信息
     *
     * @param deviceFenceId 设备电子围栏主键
     * @return 结果
     */
    @Override
    public int deleteTDeviceFenceByDeviceFenceId(Long deviceFenceId) {
        return deviceFenceMapper.deleteTDeviceFenceByDeviceFenceId(deviceFenceId);
    }
}
