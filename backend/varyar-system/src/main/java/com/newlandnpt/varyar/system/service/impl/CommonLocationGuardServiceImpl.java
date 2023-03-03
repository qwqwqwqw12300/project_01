package com.newlandnpt.varyar.system.service.impl;


import com.newlandnpt.varyar.common.annotation.LocationGuardType;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.system.domain.LocationGuard;
import com.newlandnpt.varyar.system.domain.req.DelLocationGuardReq;
import com.newlandnpt.varyar.system.domain.req.DeleteFenceReq;
import com.newlandnpt.varyar.system.service.GeoFenceService;
import com.newlandnpt.varyar.system.service.ILocationGuardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通用位置守护 Service 业务层
 * @author ljx
 * @date 2023/3/2
 */
@Service
public class CommonLocationGuardServiceImpl implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(CommonLocationGuardServiceImpl.class);

    @Autowired
    private GeoFenceService geoFenceService;

    private static Map<String, ILocationGuardService> locationGuardMap = null;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(LocationGuardType.class);
        locationGuardMap = new HashMap<>(16);
        beansWithAnnotation.forEach((key, value) -> {
            String bizType = AopUtils.getTargetClass(value).getAnnotation(LocationGuardType.class).value();
            locationGuardMap.put(bizType, (ILocationGuardService) value);
        });
    }

    public LocationGuard insertLocationGuard(LocationGuard locationGuard){
        return locationGuardMap.get(locationGuard.getGuardType()).insertLocationGuard(locationGuard);
    }

    public Integer updateLocationGuard(LocationGuard locationGuard){
        return locationGuardMap.get(locationGuard.getGuardType()).updateLocationGuard(locationGuard);
    }

    public Integer deleteLocationGuard(DelLocationGuardReq delReq){
        try{
            if (delReq.getGeoLocationGuardId().size() > 90){
                return 0;
            }
            if(delReq.getGeoLocationGuardId().size()>0) {
                String delFence = String.join(",", delReq.getGeoLocationGuardId());
                DeleteFenceReq deleteFenceReq = new DeleteFenceReq();
                deleteFenceReq.setGfid(delFence);
                geoFenceService.deleteFence(deleteFenceReq);
            }
        }catch (Exception e){
            return 0;
        }
        return 1;
    }
}
