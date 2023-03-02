package com.newlandnpt.varyar.api.controller.business.rocketmq;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newlandnpt.varyar.cloudBase.domain.Fall;
import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.utils.tcp.domain.LocationInfoResponseMqMsgEntity;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.GeoFenceService;
import com.newlandnpt.varyar.system.service.IDeviceService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.newlandnpt.varyar.framework.datasource.DynamicDataSourceContextHolder.log;

/**
 * 电子牵挂卡 位置上报事件监听
 * @author lin.ju
 * @date 2023/2/28
 */
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic.location}", consumerGroup = "${rocketmq.group.location}")
public class LocationInfoListener implements RocketMQListener<LocationInfoResponseMqMsgEntity> {

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private GeoFenceService geoFenceService;
    @Autowired
    private RedisCache redisCache;

    @Override
    public void onMessage(LocationInfoResponseMqMsgEntity entity) {
        log.debug("监听到[电子牵挂卡 位置上报事件], 报文对象：{} " , JSON.toJSONString(entity));

        TDevice device = deviceService.selectByDeviceNo(entity.getDeviceNo());

        if(device == null){
            log.warn(">>>>> 设备号：【{}】,的电子牵挂卡设备不存在，忽略位置上报事件",entity.getDeviceNo());
            return;
        }
        String tId = redisCache.getCacheObject(CacheConstants.GAODE_TID+device.getNo());

        if(tId == null){
            log.warn(">>>>> 设备号：【{}】,的电子牵挂卡设备的高德地图 tId查找不到，忽略位置上报事件");
            return;
        }
        // todo 查询轨迹创建轨迹和上传轨迹信息

    }

}
