package com.newlandnpt.varyar.api.controller.business.rocketmq;

import java.util.UUID;

import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newlandnpt.varyar.common.constant.DeviceConstants;
import com.newlandnpt.varyar.common.constant.GeoConstant;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.system.domain.Device;
import com.newlandnpt.varyar.system.domain.TDeviceFence;
import com.newlandnpt.varyar.system.domain.TEvent;
import com.newlandnpt.varyar.system.domain.req.FenceReq;
import com.newlandnpt.varyar.system.domain.req.LocationMsgReq;
import com.newlandnpt.varyar.system.domain.vo.GeoFenceResultVo;
import com.newlandnpt.varyar.system.service.GeoFenceService;
import com.newlandnpt.varyar.system.service.IDeviceFenceService;
import com.newlandnpt.varyar.system.service.IDeviceService;
import com.newlandnpt.varyar.system.service.ITEventService;

/**
 * 地理围栏事件-消息监听类
 *
 * @author lisd
 * @date 2022/12/24
 **/
@Component
//@Slf4j
//@RocketMQMessageListener(topic = "${rocketmq.topic.location}", consumerGroup = "${rocketmq.group.location}")
public class LocationListener implements RocketMQListener<String> {

	private static final Logger log = LoggerFactory.getLogger(LocationListener.class);
	
    @Autowired
    private GeoFenceService geoFenceService;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private ITEventService eventService;

    @Autowired
    private IDeviceFenceService deviceFenceService;

    @Override
    public void onMessage(String s) {
        log.debug("----" + System.currentTimeMillis() + "----" + " 监听到地理围栏消息： " + s);
        JSONObject jsonObject = JSON.parseObject(s);
        LocationMsgReq msgReq = jsonObject.toJavaObject(LocationMsgReq.class);
        if (StringUtils.isEmpty(msgReq.getDeviceNo()) || StringUtils.isEmpty(msgReq.getLongitude()) || StringUtils.isEmpty(msgReq.getLatitude())) {
            return;
        }
        String deviceNo = msgReq.getDeviceNo();
        String location = msgReq.getLongitude() + "," + msgReq.getLatitude();
        Device device = deviceService.selectByDeviceNo(deviceNo);
        //判断设备记录与状态
        if (device == null || !device.getStatus().equals(DeviceConstants.STATUS_ACTIVATED)) {
            log.info("未查询到相关电子围栏");
            return;
        }
        TDeviceFence deviceFence = deviceFenceService.selectTDeviceFenceByDeviceNo(deviceNo);
        //判断设备电子围栏记录与状态
        if (deviceFence == null || deviceFence.getGeoFenceId() == null) {
            log.info("未查询到相关的已激活设备");
            return;
        }
        String gfid = deviceFence.getGeoFenceId().toString();
        FenceReq fenceReq = new FenceReq();
        fenceReq.setKey(GeoConstant.GEO_KEY);
        fenceReq.setSid(GeoConstant.GEO_SERVICE_ID);
        fenceReq.setLocation(location);
        fenceReq.setGfids(gfid);
        String result = geoFenceService.getFenceLocationStatus(fenceReq);
        try {
            String results = JSON.parseObject(JSON.parseObject(result).get("data").toString()).get("results").toString();
            if (StringUtils.isEmpty(result)) {
                log.info("未查得 指定坐标(" + location + ") 与 电子围栏(gfid=" + gfid + ") 的关系");
                return;
            }
            GeoFenceResultVo vo = JSON.parseArray(results).toList(GeoFenceResultVo.class).get(0);
            int in = vo.getIn();
            if (in == 0) {
                TEvent event = new TEvent();
                event.setNo(UUID.randomUUID().toString());
                event.setLevel("严重");
                event.setContent("设备" + deviceNo + "超出地理围栏范围，请及时处理！");
                //todoL event表其他值
                eventService.insertTEvent(event);
            } else {
                log.info("设备定位在围栏中，状态正常");
            }
        } catch (Exception e) {
            log.error("地理围栏消息处理异常：" + e.getMessage(), e);
        }

    }
}
