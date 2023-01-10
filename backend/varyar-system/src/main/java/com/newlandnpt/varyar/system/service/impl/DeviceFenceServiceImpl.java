package com.newlandnpt.varyar.system.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newlandnpt.varyar.common.constant.Constants;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.system.domain.TDeviceFence;
import com.newlandnpt.varyar.system.domain.req.CircleReq;
import com.newlandnpt.varyar.system.domain.vo.GeoResultVo;
import com.newlandnpt.varyar.system.mapper.DeviceFenceMapper;
import com.newlandnpt.varyar.system.service.GeoFenceService;
import com.newlandnpt.varyar.system.service.IDeviceFenceService;

/**
 * 设备电子围栏Service业务层处理
 *
 * @author ruoyi
 * @date 2023-01-06
 */
@Service
//@Slf4j
public class DeviceFenceServiceImpl implements IDeviceFenceService {
	
	private static final Logger log = LoggerFactory.getLogger(DeviceFenceServiceImpl.class);
	
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

    /**
     * 查询设备电子围栏列表
     *
     * @param tDeviceFence 设备电子围栏
     * @return 设备电子围栏
     */
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
        try {
            CircleReq circleReq = new CircleReq();
            circleReq.setName(tDeviceFence.getDeviceNo() + "_fence");
            circleReq.setCenter(tDeviceFence.getLongitude() + "," + tDeviceFence.getLatitude());
            circleReq.setRadius(tDeviceFence.getRadius());
            String result = geoFenceService.addCircleFence(circleReq);
            JSONObject jsonObject = JSON.parseObject(result);
            GeoResultVo geoResultVo = jsonObject.toJavaObject(GeoResultVo.class);
            if (!geoResultVo.getErrcode().equals(Constants.GEO_RESP_SUCCESS)) {
                throw new Exception();
            }
            Long geoFenceId = Long.parseLong(geoResultVo.getData().get("gfid").toString());
            tDeviceFence.setGeoFenceId(geoFenceId);
        } catch (Exception e) {
            log.error("调用高德地图api-新增圆形围栏-响应失败");
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
        try {
            CircleReq circleReq = new CircleReq();
            circleReq.setGfid(tDeviceFence.getGeoFenceId().toString());
            circleReq.setName(tDeviceFence.getDeviceNo() + "_fence");
            circleReq.setCenter(tDeviceFence.getLongitude() + "," + tDeviceFence.getLatitude());
            circleReq.setRadius(tDeviceFence.getRadius());
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
