package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.annotation.Fence;
import com.newlandnpt.varyar.common.annotation.LocationGuardType;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.system.domain.LocationGuard;
import com.newlandnpt.varyar.system.domain.req.DelLocationGuardReq;
import com.newlandnpt.varyar.system.service.IDeviceFenceService;
import com.newlandnpt.varyar.system.service.ILocationGuardService;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用位置守护 Service 业务层
 * @author ljx
 * @date 2023/3/2
 */
@Service
public class CommonLocationGuardServiceImpl implements ApplicationListener<ContextRefreshedEvent> {
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

    public AjaxResult updateLocationGuard(LocationGuard locationGuard){
        return locationGuardMap.get(locationGuard.getGuardType()).updateLocationGuard(locationGuard);
    }

    public AjaxResult deleteLocationGuard(DelLocationGuardReq delReq){
        return null;
    }
}
