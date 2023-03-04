package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.entity.LeaveBedWarnParameter;
import com.newlandnpt.varyar.common.core.domain.model.*;
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
     * 设置雷达波屏蔽区域 (创建和修改)
     * */
    @ApiOperation("设置雷达波屏蔽区域 (创建和修改)")
    @PostMapping("/setRadarWareShadowZone")
    public AjaxResult setRadarWareShadowZone(
            @RequestBody @Validated RadarWareShadowZoneRequest radarWareShadowZoneRequest) {
        if (radarWareShadowZoneRequest.getShadowZoneName()==null || radarWareShadowZoneRequest.getShadowZoneName().equals("")){
            return error("屏蔽区域名称不能为空！");
        }
//
        AjaxResult ajax = AjaxResult.success();
        TRoomZone tRoomZone= null;
        Long deviceId = Long.valueOf(radarWareShadowZoneRequest.getDeviceId());
        TDevice.DeviceParameter parameter = iDeviceService.loadSettings(deviceId);
        if(!(parameter instanceof TDevice.RadarWaveDeviceSettings)){
            return error("设备不是雷达波设备！");
        }
        TDevice device = iDeviceService.selectDeviceByDeviceId(deviceId);
        if (device==null){
            return error("无法查找到设备信息！");
        }
        if(!device.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return  error("非创建者无权限操作！");
        }
        TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)parameter;

        if (radarWareShadowZoneRequest.getRoomZoneId()!=null && !radarWareShadowZoneRequest.getRoomZoneId().equals("")
                && CollectionUtils.isNotEmpty(radarWaveDeviceSettings.getRoomZones())){

            tRoomZone = radarWaveDeviceSettings.getRoomZones().stream()
                    .filter(p->radarWareShadowZoneRequest.getRoomZoneId().equals(p.getRoomZoneId()+"") )
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

        tRoomZone.setName(radarWareShadowZoneRequest.getShadowZoneName());
        tRoomZone.setDeviceId(Long.valueOf(radarWareShadowZoneRequest.getDeviceId()));
        tRoomZone.setRoomId(Long.valueOf(radarWareShadowZoneRequest.getRoomId()));
        tRoomZone.setX1(radarWareShadowZoneRequest.getX1());
        tRoomZone.setX2(radarWareShadowZoneRequest.getX2());
        tRoomZone.setY1(radarWareShadowZoneRequest.getY1());
        tRoomZone.setY2(radarWareShadowZoneRequest.getY2());
        //子区域类型：0:普通区域 1：屏蔽区域 2：床
        tRoomZone.setZoneType("1");
        //雷达波为顶挂时设置
        if("1".equals(radarWaveDeviceSettings.getInstallPosition()))
        {
            tRoomZone.setZ1(radarWareShadowZoneRequest.getZ1());
            tRoomZone.setZ2(radarWareShadowZoneRequest.getZ2());
        }else{
            tRoomZone.setZ1(new BigDecimal(0));
            tRoomZone.setZ2(new BigDecimal(1.5));
        }
        try {
            iDeviceService.setSettings(deviceId,radarWaveDeviceSettings);
        } catch (Exception e){
            log.error(">>>>>>",e);
            ajax = AjaxResult.error("设置雷达波屏蔽区域失败！");
            return ajax;
        }

        return ajax;
    }

    /**
     * 设置雷达波床区域 (创建和修改)
     * */
    @ApiOperation("设置雷达波床区域 (创建和修改)")
    @PostMapping("/setRadarWareBedZone")
    public AjaxResult setRadarWareBedZone(
            @RequestBody @Validated RadarWareBedZoneRequest radarWareBedZoneRequest) {
        if (radarWareBedZoneRequest.getBedName()==null || radarWareBedZoneRequest.getBedName().equals("")){
            return error("房间名称不能为空！");
        }
        AjaxResult ajax = AjaxResult.success();
        TRoomZone tRoomZone= null;
        Long deviceId = Long.valueOf(radarWareBedZoneRequest.getDeviceId());
        TDevice.DeviceParameter parameter = iDeviceService.loadSettings(deviceId);
        if(!(parameter instanceof TDevice.RadarWaveDeviceSettings)){
            return error("设备不是雷达波设备！");
        }
        TDevice device = iDeviceService.selectDeviceByDeviceId(deviceId);
        if (device==null){
            return error("无法查找到设备信息！");
        }
        if(!device.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return  error("非创建者无权限操作！");
        }

        TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)parameter;

        if (radarWareBedZoneRequest.getRoomZoneId()!=null && !radarWareBedZoneRequest.getRoomZoneId().equals("")
                && CollectionUtils.isNotEmpty(radarWaveDeviceSettings.getRoomZones())){

            tRoomZone = radarWaveDeviceSettings.getRoomZones().stream()
                    .filter(p->radarWareBedZoneRequest.getRoomZoneId().equals(p.getRoomZoneId()+"") )
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
        //子区域类型：0:普通区域 1：屏蔽区域 2：床
        tRoomZone.setZoneType("2");
        tRoomZone.setName(radarWareBedZoneRequest.getBedName());
        tRoomZone.setDeviceId(Long.valueOf(radarWareBedZoneRequest.getDeviceId()));
        tRoomZone.setRoomId(Long.valueOf(radarWareBedZoneRequest.getRoomId()));
        tRoomZone.setX1(radarWareBedZoneRequest.getX1());
        tRoomZone.setX2(radarWareBedZoneRequest.getX2());
        tRoomZone.setY1(radarWareBedZoneRequest.getY1());
        tRoomZone.setY2(radarWareBedZoneRequest.getY2());
        //雷达波为顶挂时设置
        if("1".equals(radarWaveDeviceSettings.getInstallPosition()))
        {
            tRoomZone.setZ1(radarWareBedZoneRequest.getZ1());
            tRoomZone.setZ2(radarWareBedZoneRequest.getZ2());
        }else{
            tRoomZone.setZ1(new BigDecimal(0));
            tRoomZone.setZ2(new BigDecimal(1.5));
        }
        tRoomZone.setLeaveBedWarnParameter(radarWareBedZoneRequest.getLeaveBedWarnParameter());
        try {
            iDeviceService.setSettings(deviceId,radarWaveDeviceSettings);
        } catch (Exception e){
            log.error(">>>>>>",e);
            ajax = AjaxResult.error("设置雷达波床区域失败！");
            return ajax;
        }
        return ajax;
    }

    @ApiOperation("设置雷达波离床时间规则")
    @PostMapping("/setRadarWaveLeaveBedRulesDate")
    public AjaxResult setRadarWaveLeaveBedRulesDate(
            @RequestBody @Validated RadarWaveLeaveBedRulesRequest radarWaveLeaveBedRulesRequest) {

        if (radarWaveLeaveBedRulesRequest.getDeviceId()==null|| radarWaveLeaveBedRulesRequest.getDeviceId().equals("")){
            return error("设备id不能为空！");
        }
        if (radarWaveLeaveBedRulesRequest.getRoomZoneId()==null|| radarWaveLeaveBedRulesRequest.getRoomZoneId().equals("")){
            return error("子区域id不能为空！");
        }
        TDevice device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(radarWaveLeaveBedRulesRequest.getDeviceId()));
        if (device==null){
            return error("无法查找到设备信息！");
        }
        if(!device.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return  error("非创建者无权限操作！");
        }
        if(radarWaveLeaveBedRulesRequest.getStartTime()==null  || radarWaveLeaveBedRulesRequest.getEndTime()==null)
        {
            return  error("开始结束时间不能为空！");
        }
        if ("0".equals(radarWaveLeaveBedRulesRequest.getDateType())) {
            if (radarWaveLeaveBedRulesRequest.getStartDate() == null || radarWaveLeaveBedRulesRequest.getEndDate() == null) {
                return error("开始结束日期不能为空！");
            }
            if (radarWaveLeaveBedRulesRequest.getStartDate().getTime()  > radarWaveLeaveBedRulesRequest.getEndDate().getTime() ) {
                return error("开始不能大于结束日期！");
            }
        }else {
            if (radarWaveLeaveBedRulesRequest.getWeek() == null || radarWaveLeaveBedRulesRequest.getWeek().length==0) {
                return error("星期不能为空！");
            }
        }

        Long deviceId = Long.valueOf(radarWaveLeaveBedRulesRequest.getDeviceId());

        TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)device.getParameter();
        TRoomZone troomZone = radarWaveDeviceSettings.getRoomZones().stream()
                .filter(p-> radarWaveLeaveBedRulesRequest.getRoomZoneId().equals(p.getRoomZoneId().longValue()+""))
                .findAny().orElse(null);

        if(radarWaveDeviceSettings.getRoomZones() == null){
            radarWaveDeviceSettings.setRoomZones(new ArrayList<>());
        }

        if(troomZone == null){
            troomZone = new TRoomZone();
            radarWaveDeviceSettings.getRoomZones().add(troomZone);
        }
//        LeaveBedWarnParameter.SetRuleDate  setRuleDate= null;
        if(radarWaveDeviceSettings.getRoomZones()!=null) {
//            setRuleDate = new LeaveBedWarnParameter.SetRuleDate();
            troomZone.getLeaveBedWarnParameter().getSetRuleDate().setDateType(radarWaveLeaveBedRulesRequest.getDateType());
            if("1".equals(radarWaveLeaveBedRulesRequest.getDateType()))
            {
                troomZone.getLeaveBedWarnParameter().getSetRuleDate().setWeek(radarWaveLeaveBedRulesRequest.getWeek());
            }   else
            {
                troomZone.getLeaveBedWarnParameter().getSetRuleDate().setEndDate(radarWaveLeaveBedRulesRequest.getEndDate());
                troomZone.getLeaveBedWarnParameter().getSetRuleDate().setStartDate(radarWaveLeaveBedRulesRequest.getStartDate());
            }
            troomZone.getLeaveBedWarnParameter().getSetRuleDate().setStartTime(radarWaveLeaveBedRulesRequest.getStartTime());
            troomZone.getLeaveBedWarnParameter().getSetRuleDate().setEndTime(radarWaveLeaveBedRulesRequest.getEndTime());
        }
//        troomZone.getLeaveBedWarnParameter().setSetRuleDate(setRuleDate);

        try {
            //设置时间规则
            if (device.getType().equals("0")){
                int i = iDeviceService.setSettings(deviceId,radarWaveDeviceSettings);
                if (i==0){
                    return error("设置离床时间规则失败！");
                }
            }

        } catch (Exception e){
            log.error("设置离床时间规则失败！",e);
            return error("设置离床时间规则失败！");
        }
        //保存查询返回
        return success();
    }




    @ApiOperation("删除离床时间规则")
    @PostMapping("/delRadarWaveLeaveBedRulesDate")
    public AjaxResult delRadarWaveLeaveBedRulesDate(
            @RequestBody @Validated RadarWaveLeaveBedRulesRequest  radarWaveLeaveBedRulesRequest) {
        AjaxResult ajax = AjaxResult.success();
        if (radarWaveLeaveBedRulesRequest.getDeviceId()==null||radarWaveLeaveBedRulesRequest.getDeviceId().equals("")){
            return error("设备id不能为空！");
        }
        if (radarWaveLeaveBedRulesRequest.getRoomZoneId()==null||radarWaveLeaveBedRulesRequest.getRoomZoneId().equals("")){
            return error("房间子区域id不能为空！");
        }
        Long deviceId = Long.valueOf(radarWaveLeaveBedRulesRequest.getDeviceId());
        //根据设备id获取参数信息
        TDevice.DeviceParameter parameter = iDeviceService.loadSettings(deviceId);
        if(!(parameter instanceof TDevice.RadarWaveDeviceSettings)){
            return error("设备不是雷达波设备！");
        }

        TDevice device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(radarWaveLeaveBedRulesRequest.getDeviceId()));
        if (device==null){
            return error("无法查找到设备信息！");
        }
        if(!device.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return  error("非创建者无权限操作！");
        }

        //雷达波参数
        TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)parameter;
        TRoomZone troomZone = radarWaveDeviceSettings.getRoomZones().stream()
                .filter(p-> radarWaveLeaveBedRulesRequest.getRoomZoneId().equals(""+p.getRoomZoneId().longValue()))
                .findAny().orElse(null);


        troomZone.getLeaveBedWarnParameter().setSetRuleDate(null);

        try {
                //设置时间规则
            // if (device.getType().equals("0")) {
                    int i = iDeviceService.setSettings(deviceId, radarWaveDeviceSettings);
                    if (i == 0) {
                        return error("置离床时间规则失败！");
                    }
            } catch (Exception e) {
                log.error("删除离床时间规则失败！", e);
                return error("删除离床时间规则失败！");
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
