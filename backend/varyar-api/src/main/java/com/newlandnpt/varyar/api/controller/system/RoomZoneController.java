package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.RadarRequest;
import com.newlandnpt.varyar.system.domain.TRoomZone;
import com.newlandnpt.varyar.system.service.IRoomZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;

/**
 * 房间区域空间管理
 *
 * @author newlandnpt
 */
@RestController
@RequestMapping("/api/roomZone")
public class RoomZoneController extends BaseController {

    @Autowired
    private IRoomZoneService iRoomZoneService;

    /**
     * 设置雷达波设备
     * */
    @PostMapping("/setRadarDevice")
    public AjaxResult setRadarDevice(
            @RequestBody @Validated RadarRequest radarRequest) {
        AjaxResult ajax = AjaxResult.success();
        TRoomZone tRoomZone = new TRoomZone();
        tRoomZone.setName(radarRequest.getZoneName());
        tRoomZone.setRoomId(Long.valueOf(radarRequest.getRoomId()));
        tRoomZone.setExistFlag(radarRequest.getZoneType());
        tRoomZone.setEntryTime(radarRequest.getEntryTime());
        tRoomZone.setDepartureTime(radarRequest.getDepartureTime());
        tRoomZone.setFallFlag(radarRequest.getFallFlag());
        tRoomZone.setX1(radarRequest.getX1());
        tRoomZone.setX2(radarRequest.getX2());
        tRoomZone.setY1(radarRequest.getY1());
        tRoomZone.setY1(radarRequest.getY1());
        tRoomZone.setZ1(radarRequest.getZ1());
        tRoomZone.setZ1(radarRequest.getZ1());
        try {
            iRoomZoneService.insertTRoomZone(tRoomZone);
        } catch (Exception e){
            ajax = AjaxResult.error("设置雷达波设备失败！");
            return ajax;
        }
        return ajax;
    }
}
