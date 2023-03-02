package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.RadarRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.system.domain.*;
import com.newlandnpt.varyar.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 房间区域空间管理
 *
 * @author newlandnpt
 */
@Api(tags = "房间区域空间管理")
@RestController
@RequestMapping("/api/roomZone")
public class RoomZoneController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(RoomZoneController.class);
    @Autowired
    private IRoomZoneService iRoomZoneService;

    @Autowired
    private IRoomService iRoomService;

    @Autowired
    private IFamilyService iFamilyService;

    @Autowired
    private IMemberFamilyService iMemberFamilyService;
    @Autowired
    private IDeviceService iDeviceService;
    /**
     * 设置雷达波设备 (创建和修改)
     * */
    @ApiOperation("设置雷达波设备子区域 (创建和修改)")
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
        TRoomZone tRoomZone= null;
        Long deviceId = Long.valueOf(radarRequest.getDeviceId());
        TDevice.DeviceParameter parameter = iDeviceService.loadSettings(deviceId);
        if(!(parameter instanceof TDevice.RadarWaveDeviceSettings)){
            return error("设备不是雷达波设备！");
        }

        TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)parameter;

        if (radarRequest.getRoomZoneId()!=null && !radarRequest.getRoomZoneId().equals("")
                && CollectionUtils.isNotEmpty(radarWaveDeviceSettings.getRoomZones())){

             tRoomZone = radarWaveDeviceSettings.getRoomZones().stream()
                     .filter(p->radarRequest.getRoomZoneId().equals(p.getRoomZoneId()+"") )
                     .findAny()
                     .orElse(null);
        }

        if(radarWaveDeviceSettings.getRoomZones() == null){
            radarWaveDeviceSettings.setRoomZones(new ArrayList<>());
        }

        if(tRoomZone == null){
            tRoomZone = new TRoomZone();
            radarWaveDeviceSettings.getRoomZones().add(tRoomZone);
        }

        tRoomZone.setZoneType(radarRequest.getZoneType());
        tRoomZone.setName(radarRequest.getName());
        tRoomZone.setFallFlag(radarRequest.getFallFlag());
        tRoomZone.setDeviceId(Long.valueOf(radarRequest.getDeviceId()));
        tRoomZone.setRoomId(Long.valueOf(radarRequest.getRoomId()));
        tRoomZone.setExistFlag(radarRequest.getExistFlag());
        tRoomZone.setEntryTime(radarRequest.getEntryTime());
        tRoomZone.setDepartureTime(radarRequest.getDepartureTime());
        tRoomZone.setFallFlag(radarRequest.getFallFlag());
        tRoomZone.setX1(radarRequest.getX1());
        tRoomZone.setX2(radarRequest.getX2());
        tRoomZone.setY1(radarRequest.getY1());
        tRoomZone.setY2(radarRequest.getY2());
        //雷达波为顶挂时设置
        if("1".equals(radarWaveDeviceSettings.getInstallPosition()))
        {
            tRoomZone.setZ1(radarRequest.getZ1());
            tRoomZone.setZ2(radarRequest.getZ2());
        }else{
            tRoomZone.setZ1(new BigDecimal(1.5));
            tRoomZone.setZ2(new BigDecimal(1.5));
        }

        tRoomZone.setInMonitorFlag(radarRequest.getInMonitorFlag());
        tRoomZone.setOutMonitorFlag(radarRequest.getOutMonitorFlag());
        tRoomZone.setStartTime(radarRequest.getStartTime());
        tRoomZone.setEndTime(radarRequest.getEndTime());
        try {
            iDeviceService.setSettings(deviceId,radarWaveDeviceSettings);
        } catch (Exception e){
            log.error(">>>>>>",e);
            ajax = AjaxResult.error("设置雷达波设备失败！");
            return ajax;
        }
        return ajax;
    }
    /**
     * 删除雷达波设备
     * */
    @ApiOperation("删除雷达波设备子区域")
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
            Long deviceId = tRoomZone.getDeviceId();

            TDevice.DeviceParameter parameter = iDeviceService.loadSettings(deviceId);
            if(!(parameter instanceof TDevice.RadarWaveDeviceSettings)){
                return error("设备不是雷达波设备！");
            }
            TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)parameter;

            if(CollectionUtils.isNotEmpty(radarWaveDeviceSettings.getRoomZones())){
                radarWaveDeviceSettings.setRoomZones(radarWaveDeviceSettings.getRoomZones().stream()
                        .filter(p->radarRequest.getRoomZoneId().longValue()!=p.getRoomZoneId())
                        .collect(Collectors.toList()));
            }
            iDeviceService.setSettings(deviceId,radarWaveDeviceSettings);
        }else {
            return error("无权删除房间子区域！");
        }
        return success();
    }
    /**
     * 获取雷达波设备子区域列表
     * */
    @ApiOperation("获取雷达波设备子区域列表")
    @ApiImplicitParam(name = "deviceId", value = "设备id",required = true, dataType = "String", dataTypeClass = String.class)
    @GetMapping("/getRoomZoon")
    public TableDataInfo getRoomZoon(String deviceId ) {
        if (deviceId==null || deviceId.equals("")){
            throw new ServiceException("设备Id不能为空！");
        }
        return getDataTable(iRoomZoneService.selectTRoomZoneByDeviceId(Long.valueOf(deviceId)));
    }
}
