package com.newlandnpt.varyar.system.service.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.constant.DeviceConstants;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.domain.TEvent;
import com.newlandnpt.varyar.system.mapper.TDeviceMapper;
import com.newlandnpt.varyar.system.mapper.TEventMapper;
import com.newlandnpt.varyar.system.service.DeviceDisconnectionService;

/**
 * @author lisd
 * @date 2023/1/6
 **/
@Service
//@Slf4j
public class DeviceDisconnectionServiceImpl implements DeviceDisconnectionService {

	private static final Logger log = LoggerFactory.getLogger(DeviceDisconnectionServiceImpl.class);
	
    @Autowired
    private TDeviceMapper deviceMapper;

    @Autowired
    private TEventMapper tEventMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void deviceDisconnectionIssue(String deviceNo) {
        TDevice param = new TDevice();
        param.setNo(deviceNo);
        param.setStatus(DeviceConstants.STATUS_ACTIVATED);
        List<TDevice> devices = deviceMapper.selectTDeviceList(param);
        if ((devices.size() == 0)) {
            log.error("设备列表为空");
            return;
        }
        for (TDevice device : devices) {
            String cacheKey = getCacheKey(device.getNo());
            if (!redisCache.hasKey(cacheKey)) {
                //todoL Redis缓存设备结构
                triggerNetEvent(device);
            }
        }
    }


    /**
     * 新增断网事件
     *
     * @param device 终端信息
     */
    private void triggerNetEvent(TDevice device) {
        TEvent event = new TEvent();
        event.setNo(UUID.randomUUID().toString());
        event.setLevel("严重");
        event.setContent("设备" + device.getNo() + " 疑似断网，请及时处理！");
        //todoL event表其他值
        tEventMapper.insertTEvent(event);
    }

    /**
     * 设置cache key
     *
     * @param key 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String key) {
        return CacheConstants.DEVICE_KEY + key;
    }

}
