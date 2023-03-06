package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.annotation.Fence;
import com.newlandnpt.varyar.common.enums.ExCodeEnum;
import com.newlandnpt.varyar.common.exception.business.BusinessException;
import com.newlandnpt.varyar.system.domain.TDeviceFence;
import com.newlandnpt.varyar.system.domain.req.DeleteFenceReq;
import com.newlandnpt.varyar.system.service.GeoFenceService;
import com.newlandnpt.varyar.system.service.IDeviceFenceService;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 通用的电子围栏 业务层
 * @author ljx
 * @date 2023/3/1
 */
@Service
public class CommonDeviceFenceServiceImpl implements ApplicationListener<ContextRefreshedEvent> {
    private static Map<String, IDeviceFenceService> deviceFenceMap = null;

    @Autowired
    private GeoFenceService geoFenceService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Fence.class);
        deviceFenceMap = new HashMap<>();
        beansWithAnnotation.forEach((key, value) -> {
            String bizType = AopUtils.getTargetClass(value).getAnnotation(Fence.class).value();
            deviceFenceMap.put(bizType, (IDeviceFenceService) value);
        });
    }

    /**
     * 插入电子围栏
     * @param tDeviceFence
     * @return
     */
    public int insertTDeviceFence(TDeviceFence tDeviceFence){
        return deviceFenceMap.get(tDeviceFence.getFenceType()).insertTDeviceFence(tDeviceFence);
    }

    /**
     * 更新电子围栏
     * @param tDeviceFence
     * @return
     */
    public int updateTDeviceFence(TDeviceFence tDeviceFence){
        return deviceFenceMap.get(tDeviceFence.getFenceType()).updateTDeviceFence(tDeviceFence);
    }

    public TDeviceFence selectTDeviceFenceByDeviceFenceId(Long deviceFenceId){
        return deviceFenceMap.get("circle").selectTDeviceFenceByDeviceFenceId(deviceFenceId);
    }

    public TDeviceFence selectTDeviceFenceByDeviceNo(String deviceNo){
        return deviceFenceMap.get("circle").selectTDeviceFenceByDeviceNo(deviceNo);
    }

    public List<TDeviceFence> selectTDeviceFenceByDeviceId(Long deviceId){
        return deviceFenceMap.get("circle"). selectTDeviceFenceByDeviceId(deviceId);
    }

    public List<TDeviceFence> selectTDeviceFenceList(TDeviceFence tDeviceFence){
        return deviceFenceMap.get(tDeviceFence.getFenceType()).selectTDeviceFenceList(tDeviceFence);
    }

    public int deleteTDeviceFenceByDeviceFenceIds(Long[] deviceFenceIds){
        return deviceFenceMap.get("circle").deleteTDeviceFenceByDeviceFenceIds(deviceFenceIds);
    }

    public int deleteTDeviceFence(TDeviceFence tDeviceFence){
        try{
                String delFence = String.valueOf(tDeviceFence.getGeoFenceId());
                DeleteFenceReq deleteFenceReq = new DeleteFenceReq();
                deleteFenceReq.setGfid(delFence);
                geoFenceService.deleteFence(deleteFenceReq);
        }catch (Exception e){
            throw new BusinessException(ExCodeEnum.FENCE_DELETE_FAIL);
        }
        return deviceFenceMap.get("circle").deleteTDeviceFenceByDeviceFenceId(tDeviceFence.getDeviceFenceId());
    }

    public int deleteTDeviceFenceByDeviceFenceId(Long deviceFenceId){
        return deviceFenceMap.get("circle").deleteTDeviceFenceByDeviceFenceId(deviceFenceId);
    }
}
