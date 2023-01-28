package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.constant.Constants;
import com.newlandnpt.varyar.common.constant.DeviceConstants;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.system.domain.*;
import com.newlandnpt.varyar.system.domain.vo.StateVo;
import com.newlandnpt.varyar.system.mapper.*;
import com.newlandnpt.varyar.system.service.DeviceEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author lisd
 * @date 2023/1/6
 **/
@Service
public class DeviceEventServiceImpl implements DeviceEventService {

    private static final Logger log = LoggerFactory.getLogger(DeviceEventServiceImpl.class);

    @Autowired
    private StateMapper stateMapper;

    @Autowired
    private TDeviceMapper deviceMapper;
    @Autowired
    private TMemberMapper memberMapper;
    @Autowired
    private TDeviceGroupMapper deviceGroupMapper;

    @Autowired
    private TEventMapper tEventMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 事件级别 严重
     */
    private static final String EVENT_LEVEL_HIGH = "urgent";

    /**
     * 事件级别 正常
     */
    private static final String EVENT_LEVEL_NORMAL = "normal";

    /**
     * 事件操作类型默认值 1-后台人工
     */
    private static final String AUTO_EVENT = "1";

    /**
     * 事件操作标志默认值 0-未处理
     */
    private static final String AUTO_FLAG = "0";

    @Override
    public void deviceStateIssue(String deviceNo) {
        TDevice device = deviceMapper.selectByDeviceNo(deviceNo);
        triggerEvent(EVENT_LEVEL_NORMAL, device, "设备 " + deviceNo + " 激活成功！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deviceDisconnectionIssue() {
        //设置查询时间
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        date.add(Calendar.MINUTE, -5);
        Date beginTime = date.getTime();
        //在此时间之前的记录视为断网设备记录，进行下一步验证
        List<StateVo> stateList = stateMapper.selectByTime(beginTime);
        if (stateList.size() == 0) {
            return;
        }
        List<TDevice> discDevices = new ArrayList<>();
        for (StateVo stateVo : stateList) {
            //Redis验证设备断网情况，无记录则确为断网
            String deviceNo = stateVo.getDeviceId();
            if (!redisCache.hasKey(CacheConstants.DEVICE_STATE_KEY + deviceNo)) {
                TDevice device = deviceMapper.selectByDeviceNo(deviceNo);
                discDevices.add(device);
            }
        }
        if (discDevices.size() > 0) {
            List<String> devIds = new ArrayList<>();
            // Todo 状态一直是断网的情况不重复发断网事件

            for (TDevice device : discDevices) {
                devIds.add(device.getNo());
                triggerEvent(EVENT_LEVEL_HIGH, device, "设备" + device.getNo() + " 疑似断网，请及时处理！");
            }
            //Redis缓存断网设备列表
            List<String> cacheList = redisCache.getCacheList(CacheConstants.DEVICE_DISCONNECTION);
            cacheList.removeAll(devIds);
            List<String> newCacheList = new ArrayList<>();
            newCacheList.addAll(cacheList);
            newCacheList.addAll(devIds);
            //更新Redis
            redisCache.deleteObject(CacheConstants.DEVICE_DISCONNECTION);
            redisCache.setCacheList(CacheConstants.DEVICE_DISCONNECTION, newCacheList);
        }

    }

    @Override
    public void deviceReconnectionIssue() {
        if (!redisCache.hasKey(CacheConstants.DEVICE_DISCONNECTION)) {
            return;
        }
        List<String> discDevices = redisCache.getCacheList(CacheConstants.DEVICE_DISCONNECTION);
        List<String> reconnects = new ArrayList<>();
        for (String devId : discDevices) {
            if (redisCache.hasKey(CacheConstants.DEVICE_STATE_KEY + devId)) {
                reconnects.add(devId);
            }
        }
        if (reconnects.size() > 0) {
            for (String deviceNo : reconnects) {
                TDevice device = deviceMapper.selectByDeviceNo(deviceNo);
                if (device == null) {
                    continue;
                }
                triggerEvent(EVENT_LEVEL_NORMAL, device, "设备" + deviceNo + " 已恢复网络连接");
            }
            discDevices.removeAll(reconnects);
            //更新Redis
            redisCache.deleteObject(CacheConstants.DEVICE_DISCONNECTION);
            if (discDevices.size() == 0) {
                return;
            }
            redisCache.setCacheList(CacheConstants.DEVICE_DISCONNECTION, discDevices);
        }
    }

    @Override
    public void deviceAccessIssue(String deviceNo) {
        TDevice param = new TDevice();
        param.setNo(deviceNo);
        param.setStatus(DeviceConstants.STATUS_ACTIVATED);
        param.setDelFlag(DeviceConstants.DEL_FLAG_NOT_ACTIVE);
        List<TDevice> devices = deviceMapper.selectTDeviceList(param);
        if ((devices.size() == 0)) {
            log.error("未找到设备 " + deviceNo);
            return;
        }
        for (TDevice device : devices) {
            String cacheKey = CacheConstants.T_DEVICE_KEY + device.getNo();
            if (redisCache.hasKey(cacheKey)) {
                TDevice redisDev = redisCache.getCacheObject(cacheKey);
                if (redisDev.getStatus().equals(DeviceConstants.STATUS_ACTIVATED)) {
                    //todoL Redis缓存设备结构
                    triggerEvent(EVENT_LEVEL_HIGH, device, "设备 " + device.getNo() + " 监控到房间内【有/无人】超过" + Constants.ACCESS_ISSUE_DELAY_THRITY + "分钟，请及时处理！");
                }
            }
        }
    }


    /**
     * 新增事件
     *
     * @param device 终端信息
     */
    private void triggerEvent(String level, TDevice device, String content) {
        TEvent event = new TEvent();
        event.setNo(UUID.randomUUID().toString());
        event.setDeviceId(device.getDeviceId());
        event.setDevicegroupId(device.getDevicegroupId());
        event.setFamilyId(device.getFamilyId());
        event.setDeviceNo(device.getNo());
        event.setOrgId(device.getOrgId());
        event.setOrgName(device.getOrgName());
        event.setMemberId(device.getMemberId());
        event.setMemberPhone(device.getMemberPhone());
        event.setLevel(level);
        event.setContent(content);
        event.setOperateType(AUTO_EVENT);
        event.setOperateFlag(AUTO_FLAG);
        // 根据设备查找运营人员信息
        if (device.getMemberId() != null) {
            // 会员设备查找会员的运营人员
            TMember member = memberMapper.selectMemberByMemberId(device.getMemberId());
            if(member!=null){
                log.info(">>>>> 设备{}所属会员id:{}查不到会员信息，事件忽略运营人员信息录入", device.getNo(), device.getMemberId());
            }else{
                event.setUserId(member.getUserId());
                event.setUserName(member.getUserName());
            }
        } else if (device.getDevicegroupId() != null) {
            // 机构设备查找设备对应设备组
            TDeviceGroup deviceGroup = deviceGroupMapper.selectDeviceGroupByDeviceGroupId(device.getDevicegroupId());
            if (deviceGroup != null) {
                event.setUserId(deviceGroup.getUserId());
                event.setUserName(deviceGroup.getUserName());
            } else {
                log.info(">>>>> 设备{}所属设备组id:{}查不到设备组信息，事件忽略运营人员信息录入", device.getNo(), device.getDevicegroupId());
            }
        } else {
            log.info(">>>>> 设备{}会员id和设备组id都为空，事件忽略运营人员信息录入", device.getNo());
        }
        event.setCreateTime(new Date());
        tEventMapper.insertTEvent(event);
        log.info("新增事件成功：" + content);
        // todo 会员设备事件同时触发发消息
    }


}
