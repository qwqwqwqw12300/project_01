package com.newlandnpt.varyar.api.controller.business.rocketmq;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.utils.tcp.domain.DeviceLoginMqMsgEntity;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.domain.req.TerminalAddReq;
import com.newlandnpt.varyar.system.service.DeviceEventService;
import com.newlandnpt.varyar.system.service.GeoFenceService;
import com.newlandnpt.varyar.system.service.IDeviceService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.newlandnpt.varyar.common.constant.DeviceConstants.STATUS_NOT_ACTIVE;
import static com.newlandnpt.varyar.framework.datasource.DynamicDataSourceContextHolder.log;

/**
 * 设备登录事件 监听
 * @author lin.ju
 * @date 2023/2/28
 **/
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic.deviceLogin}", consumerGroup = "${rocketmq.group.deviceLogin}")
public class DeviceLoginListener implements RocketMQListener<DeviceLoginMqMsgEntity> {

    private static final Logger log = LoggerFactory.getLogger(DeviceLoginListener.class);

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private GeoFenceService geoFenceService;

    @Override
    public void onMessage(DeviceLoginMqMsgEntity entity) {
        log.debug("监听到[设备登录事件], 报文对象：{} " , JSON.toJSONString(entity));

        TDevice device = deviceService.selectByDeviceNo(entity.getDeviceNo());

        if(device == null){
            log.warn(">>>>> 设备号：【{}】,的电子牵挂卡设备不存在，忽略设备登录事件",entity.getDeviceNo());
            return;
        }

        String tId = geoFenceService.findTerminalIdByName(device.getType()+"_"+device.getNo());
        if(tId == null){
            TerminalAddReq terminalAddReq = new TerminalAddReq();
            terminalAddReq.setName(device.getType()+"_"+device.getNo());
            tId = geoFenceService.addTerminal(terminalAddReq);
        }
        redisCache.setCacheObject(CacheConstants.GAODE_TID+device.getNo(),tId);
    }
}
