package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.RadarRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.system.domain.TFamily;
import com.newlandnpt.varyar.system.domain.TMemberFamily;
import com.newlandnpt.varyar.system.domain.TRoom;
import com.newlandnpt.varyar.system.domain.TRoomZone;
import com.newlandnpt.varyar.system.service.IFamilyService;
import com.newlandnpt.varyar.system.service.IMemberFamilyService;
import com.newlandnpt.varyar.system.service.IRoomService;
import com.newlandnpt.varyar.system.service.IRoomZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @Autowired
    private IRoomService iRoomService;

    @Autowired
    private IFamilyService iFamilyService;

    @Autowired
    private IMemberFamilyService iMemberFamilyService;
    /**
     * 设置雷达波设备 (创建和修改)
     * */
    @PostMapping("/setRadarDevice")
    public AjaxResult setRadarDevice(
            @RequestBody @Validated RadarRequest radarRequest) {
        if (radarRequest.getName()==null || radarRequest.getName().equals("")){
            return error("房间名称不能为空！");
        }
        if (radarRequest.getZoneType()==null||radarRequest.getZoneType().equals("")){
            return error("区域类型不能为空！");
        }
        AjaxResult ajax = AjaxResult.success();
        TRoomZone tRoomZone = new TRoomZone();
        if (radarRequest.getRoomZoneId()!=null && !radarRequest.getRoomZoneId().equals("")){
             tRoomZone = iRoomZoneService.selectTRoomZoneByRoomZoneId(Long.valueOf(radarRequest.getRoomZoneId()));
        }
        tRoomZone.setZoneType(radarRequest.getZoneType());
        tRoomZone.setName(radarRequest.getName());
        tRoomZone.setFallFlag(radarRequest.getFallFlag());
        tRoomZone.setDeviceId(Long.valueOf(radarRequest.getDeviceId()));
        tRoomZone.setRoomId(Long.valueOf(radarRequest.getRoomId()));
        tRoomZone.setExistFlag(radarRequest.getExistFlag());
        tRoomZone.setEntryTime(radarRequest.getEntryTime()/1000);
        tRoomZone.setDepartureTime(radarRequest.getDepartureTime()/1000);
        tRoomZone.setFallFlag(radarRequest.getFallFlag());
        tRoomZone.setX1(radarRequest.getX1());
        tRoomZone.setX2(radarRequest.getX2());
        tRoomZone.setY1(radarRequest.getY1());
        tRoomZone.setY2(radarRequest.getY2());
        tRoomZone.setZ1(radarRequest.getZ1());
        tRoomZone.setZ2(radarRequest.getZ2());
        tRoomZone.setInMonitorFlag(radarRequest.getInMonitorFlag());
        tRoomZone.setOutMonitorFlag(radarRequest.getOutMonitorFlag());
        tRoomZone.setStartTime(radarRequest.getStartTime());
        tRoomZone.setEndTime(radarRequest.getEndTime());
        try {
            if (tRoomZone.getRoomZoneId()==null||tRoomZone.getRoomZoneId().equals("")){
                iRoomZoneService.insertTRoomZone(tRoomZone);
            }else {
                iRoomZoneService.updateTRoomZone(tRoomZone);
            }
        } catch (Exception e){
            ajax = AjaxResult.error("设置雷达波设备失败！");
            return ajax;
        }
        return ajax;
    }
    /**
     * 删除雷达波设备
     * */
    @PostMapping("/remRadarDevice")
    public AjaxResult removeRadarDevice(@RequestBody @Validated TRoomZone radarRequest) {
        if (radarRequest.getRoomZoneId()==null || radarRequest.getRoomZoneId().equals("")){
            return error("房间子区域Id不能为空！");
        }
        TRoomZone tRoomZone = iRoomZoneService.selectTRoomZoneByRoomZoneId(Long.valueOf(radarRequest.getRoomZoneId()));
        if (tRoomZone==null){
            return error("房间子区域数据为空！");
        }
        TRoom tRoom = iRoomService.selectTRoomByRoomId(tRoomZone.getRoomId());
        TFamily tFamily = iFamilyService.selectTFamilyByFamilyId(tRoom.getFamilyId());
        List<TMemberFamily> tMemberFamilys = iMemberFamilyService.selectTMemberFamilyByMemberFamilyId(tFamily.getFamilyId(),this.getLoginUser().getMemberId());
        if(tMemberFamilys.size()>0){
            iRoomZoneService.deleteTRoomZoneByRoomZoneId(Long.valueOf(radarRequest.getRoomZoneId()));
        }else {
            return error("无权删除房间子区域！");
        }
        return success();
    }
    /**
     * 获取雷达波设备子区域列表
     * */
    @GetMapping("/getRoomZoon")
    public TableDataInfo getRoomZoon(String deviceId ) {
        if (deviceId==null || deviceId.equals("")){
            throw new ServiceException("设备Id不能为空！");
        }
        return getDataTable(iRoomZoneService.selectTRoomZoneByDeviceId(Long.valueOf(deviceId)));
    }
}
