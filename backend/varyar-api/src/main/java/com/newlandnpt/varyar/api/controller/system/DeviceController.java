package com.newlandnpt.varyar.api.controller.system;

import com.alibaba.fastjson2.JSON;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.entity.*;
import com.newlandnpt.varyar.common.core.domain.model.*;
import com.newlandnpt.varyar.common.core.domain.vo.ExtraVo;
import com.newlandnpt.varyar.common.core.domain.vo.TrackerTargetVo;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.common.utils.uuid.IdUtils;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.IDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

import static com.newlandnpt.varyar.common.constant.CacheConstants.TARGET_LOCATION_SWITCH_KEY;

/**
 * 设备管理
 *
 * @author newlandnpt
 */
@Api(tags = "设备")
@RestController
@RequestMapping("/api/device")
public class DeviceController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DeviceController.class);
    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private RedisCache redisCache;


    @ApiOperation("根据家庭id和房间id获取设备列表")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody @Validated DeviceRequest deviceRequest) {
//        startPage(10000);
        startPage(deviceRequest.getPageNum(),deviceRequest.getPageSize());
        TDevice cond = new TDevice();
        cond.setFamilyId(Long.valueOf(deviceRequest.getFamilyId()));
        cond.setRoomId(Long.valueOf(deviceRequest.getRoomId()));
        List<TDevice> list = new ArrayList<TDevice>();
        try {
            list = iDeviceService.selectDeviceList(cond);
            if(list.size()>0){
                for (TDevice item:list){
                    if (item.getParameter()==null){
                        item.setParameter(new TDevice.DeviceParameter());
                    }
                }
                list = iDeviceService.loadingDeviceStauts(list);
            }
        } catch (Exception e){
            log.error(e.getMessage());
            throw new ServiceException("查询我的设备失败！");
        }
        return getDataTable(list);
    }

    @ApiOperation("根据家庭id和房间id获取当前会员的设备列表")
    @PostMapping("/listState")
    public TableDataInfo listState( @RequestBody @Validated DeviceRequest deviceRequest) {
//        startPage(1000);
        startPage(deviceRequest.getPageNum(),deviceRequest.getPageSize());
        Map map = new HashMap();
        map.put("memberId",this.getLoginUser().getMemberId());
        map.put("familyId",deviceRequest.getFamilyId());
        map.put("roomId",deviceRequest.getRoomId());
        List<TDevice> list = new ArrayList<TDevice>();
        try {
            list = iDeviceService.selectDeviceByMemberId(map);
            if(list.size()>0){
                for (TDevice item:list){
                    if (item.getParameter()==null){
                        item.setParameter(new TDevice.DeviceParameter());
                    }
                }
                list = iDeviceService.loadingDeviceStauts(list);
            }
        } catch (Exception e){
            log.error(e.getMessage());
            throw new ServiceException("查询我的设备失败！");
        }
        return getDataTable(list);
    }

    @ApiOperation("创建设备")
    @PostMapping("/creDevice")
    public AjaxResult createDevice(
            @RequestBody @Validated DeviceRequest deviceRequest){
        AjaxResult ajax = AjaxResult.success();
        ajax  = checkInfo(deviceRequest,ajax);

        if(ajax!= null){
           return ajax;
        }
        TDevice device = new TDevice();
        ajax = AjaxResult.success(device);
        if (deviceRequest.getDeviceName()==null||deviceRequest.getDeviceName().equals("")){
            deviceRequest.setDeviceName(this.getUsername()+"的设备");
        }
        device.setName(deviceRequest.getDeviceName());
        device.setNo(deviceRequest.getDeviceNo());
        device.setType(deviceRequest.getDeviceType());
        device.setLocation(deviceRequest.getLocation());
        device.setStatus("0");
        device.setDistributeFlag("1");
        device.setOrgId(Long.valueOf("101"));
        device.setOrgName("个人机构");
        device.setDelFlag("0");
        device.setMemberId(getLoginUser().getMemberId());
        device.setCreateTime(DateUtils.getNowDate());

        try {
            TDevice cond = new TDevice();
            cond.setNo(deviceRequest.getDeviceNo());
            cond.setDelFlag("0");
            List<TDevice> devices = iDeviceService.selectDeviceList(cond);
            if (devices.size()>0){
                TDevice item = devices.get(0);
                if (item.getMemberId()!=null && !item.getMemberId().toString().equals("")){
                    return error("此设备已存在！");
                }else{
                    item.setMemberId(this.getLoginUser().getMemberId());
                    item.setName(deviceRequest.getDeviceName());
                    item.setLocation(deviceRequest.getLocation());
                    iDeviceService.updateDevice(item);
                }
            }else{
                iDeviceService.insertDevice(device);
                log.debug(">>>>> 设备创建成功，返回报文：{}",JSON.toJSONString(ajax));
            }


        } catch (Exception e){
            log.error(e.getMessage());
            return  error("新增我的设备失败！");
        }


        return ajax;
    }

    @ApiOperation("修改设备-修改设备基础信息")
    @PostMapping("/editDevice")
    public AjaxResult editDevice(
            @RequestBody @Validated DeviceRequest deviceRequest){
        AjaxResult ajax = AjaxResult.success();
        if (deviceRequest.getDeviceId()==null||deviceRequest.getDeviceId().equals("")){
            return  error("设备id不能为空！");
        }
        if (deviceRequest.getDeviceName()==null||deviceRequest.getDeviceName().equals("")){
            return error("设备名称不能为空！");
        }
        TDevice device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(deviceRequest.getDeviceId()));
        if (device==null){
            return  error("无法查找到设备信息！");
        }
        if(!device.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return  error("非创建者无权限修改！");
        }
        device.setName(deviceRequest.getDeviceName());
        if (deviceRequest.getLocation()!=null){
            device.setLocation(deviceRequest.getLocation());
        }
        try {
            iDeviceService.updateDevice(device);
        } catch (Exception e){
            log.error(e.getMessage());
            return  error("修改我的设备失败！");
        }
        return ajax;
    }

    @ApiOperation("删除设备")
    @PostMapping("/remDevice")
    public AjaxResult removeDevice(
            @RequestBody @Validated DeviceRequest deviceRequest){
        AjaxResult ajax = AjaxResult.success();
        if (deviceRequest.getDeviceId()==null || deviceRequest.getDeviceId().equals("")){
            return  error("设备id不能为空！");
        }
        TDevice device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(deviceRequest.getDeviceId()));
        if (device==null){
            return  error("无法查找到设备信息！");
        }
        if(!device.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return  error("非创建者无权限删除！");
        }
        try {
            iDeviceService.deleteAndSaveDevice(Long.valueOf(deviceRequest.getDeviceId()));
        } catch (Exception e){
            log.error(e.getMessage());
            return  error("修改我的设备失败！");
        }
        return ajax;
    }

    private AjaxResult checkInfo(DeviceRequest deviceRequest,AjaxResult ajax){
        if (deviceRequest.getDeviceType()==null||deviceRequest.getDeviceType().equals("")){
            ajax = AjaxResult.error("设备类型不能为空！");
            return ajax;
        }
        if (deviceRequest.getDeviceNo()==null||deviceRequest.getDeviceNo().equals("")){
            ajax = AjaxResult.error("设备编号不能为空！");
            return ajax;
        }
        return null;
    }

    private AjaxResult checkDeviceLocation(DeviceRequest deviceRequest){
        if (deviceRequest.getRoomLeft()==null||deviceRequest.getRoomLeft().equals("")){
            return error("左边长度不能为空！");
        }
        if (deviceRequest.getRoomRight()==null||deviceRequest.getRoomRight().equals("")){
            return error("右边长度不能为空！");
        }
        if((deviceRequest.getRoomLeft().add(deviceRequest.getRoomRight())).compareTo(new BigDecimal(4))==1){
            return error("左右侧距离之和必须小于等于4！");
        }
        //顶装时: 前后距离及左右距离和大于0.3小于4
        if ("1".equals(deviceRequest.getInstallPosition())){
            if((deviceRequest.getRoomLeft().add(deviceRequest.getRoomRight())).compareTo(new BigDecimal(4))==1 ||
                    (deviceRequest.getRoomLeft().add(deviceRequest.getRoomRight())).compareTo(new BigDecimal(0.3))==-1
            ){
                return error("左右侧距离之和必须大于等于0.3、小于等于4！");
            }
            if((deviceRequest.getRoomBehind().add(deviceRequest.getRoomFront())).compareTo(new BigDecimal(4))==1 ||
                    (deviceRequest.getRoomBehind().add(deviceRequest.getRoomFront())).compareTo(new BigDecimal(0.3))==-1
            ){
                return error("前后距离之和必须大于等于0.3、小于等于4！");
            }
            if(deviceRequest.getRoomHeight().compareTo(new BigDecimal(4))==1||deviceRequest.getRoomHeight().compareTo(new BigDecimal(0))==-1)
            {
                return error("设备高度必须介于0到4米之间！");
            }
        }


        /*if (deviceRequest.getRoomLeft()==null||deviceRequest.getRoomLeft().equals("")){
            return error("左边长度不能为空！");
        }
        if (deviceRequest.getRoomRight()==null||deviceRequest.getRoomRight().equals("")){
            return error("右边长度不能为空！");
        }
        if (deviceRequest.getRoomLength()==null||deviceRequest.getRoomLength().equals("")){
            return error("长度信息不能为空！");
        }
        if (deviceRequest.getRoomLength()==null||deviceRequest.getRoomLength().equals("")){
            return error("高度信息不能为空！");
        }
        if (deviceRequest.getInMonitorFlag()==null||deviceRequest.getInMonitorFlag().equals("")){
            return error("进入标识不能为空！");
        }
        if (deviceRequest.getOutMonitorFlag()==null||deviceRequest.getOutMonitorFlag().equals("")){
            return error("离开标识不能为空！");
        }
        if (deviceRequest.getEntryTime()==null||deviceRequest.getEntryTime().equals("")){
            return error("进入时间不能为空！");
        }
        if (deviceRequest.getDepartureTime()==null ||deviceRequest.getDepartureTime().equals("")){
            return error("离开时间不能为空！");
        }
        if ( deviceRequest.getExistFlag()==null||deviceRequest.getExistFlag().equals("")){
            return error("区域类型不能为空！");
        }
        if ( deviceRequest.getFallFlag()==null||deviceRequest.getFallFlag().equals("")){
            return error("迭代监测不能为空！");
        }
        if (deviceRequest.getStartTime()==null){
            return error("开始时间不能为空！");
        }
        if (deviceRequest.getEndTime()==null){
            return error("结束时间不能为空！");
        }*/

        return null;
    }


    @ApiOperation("雷达波设置页面")
    @PostMapping("/setRadarWaveDevice")
    public AjaxResult setRadarWaveDevice(
            @RequestBody @Validated RadarWaveDeviceRequest radarWaveDeviceRequest) {
        AjaxResult ajax = AjaxResult.success();
        if (radarWaveDeviceRequest.getDeviceId()==null||radarWaveDeviceRequest.getDeviceId().equals("")){
            return error("设备id不能为空！");
        }
        TDevice device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(radarWaveDeviceRequest.getDeviceId()));
        if (device==null){
            return error("无法查找到设备信息！");
        }
        if(!device.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return  error("非创建者无权限操作！");
        }
        device.setName(radarWaveDeviceRequest.getDeviceName());
        if (device.getType().equals("0")){
            //雷达波 判断设备位置信息
            AjaxResult item = checkRadarWaveDeviceLocation(radarWaveDeviceRequest);
            if (item!=null){
                return item;
            }

            //雷达波参数
//            TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)parameter;
            TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)device.getParameter();
            if (radarWaveDeviceSettings == null){
                radarWaveDeviceSettings = new TDevice.RadarWaveDeviceSettings();
            }
            DeviceLocationTop locationTop = new DeviceLocationTop();
            DeviceLocationWall locationWall = new DeviceLocationWall();
            //设备安装位置
            radarWaveDeviceSettings.setInstallPosition(radarWaveDeviceRequest.getInstallPosition());
            if("1".equals(radarWaveDeviceRequest.getInstallPosition())) {
                locationTop.setRoomHeight(radarWaveDeviceRequest.getDeviceLocationTop().getRoomHeight());
                locationTop.setRoomFront(radarWaveDeviceRequest.getDeviceLocationTop().getRoomFront());
                locationTop.setRoomBehind(radarWaveDeviceRequest.getDeviceLocationTop().getRoomBehind());
                locationTop.setRoomLeft(radarWaveDeviceRequest.getDeviceLocationTop().getRoomLeft());
                locationTop.setRoomRight(radarWaveDeviceRequest.getDeviceLocationTop().getRoomRight());
                radarWaveDeviceSettings.setDeviceLocationTop(locationTop);
            }else {
                locationWall.setRoomLength(radarWaveDeviceRequest.getDeviceLocationWall().getRoomLength());
                locationWall.setRoomLeft(radarWaveDeviceRequest.getDeviceLocationWall().getRoomLeft());
                locationWall.setRoomRight(radarWaveDeviceRequest.getDeviceLocationWall().getRoomRight());
                radarWaveDeviceSettings.setDeviceLocationWall(locationWall);
            }
            //预警参数设置
            radarWaveDeviceSettings.getDeviceWarnParameter().setFallWarn("1");
            radarWaveDeviceSettings.getDeviceWarnParameter().setNobodyWarn(radarWaveDeviceRequest
                    .getDeviceWarnParameter().getNobodyWarn());

            device.setParameter(radarWaveDeviceSettings);
        }
        try {
            //设置雷达波设备
            int i = iDeviceService.setSettings(Long.valueOf(radarWaveDeviceRequest.getDeviceId()),device.getParameter());
            if (i==0){
                    return error("设置配置失败！");
            }

        } catch (Exception e){
            log.error("设置配置失败！",e);
            return error("设置配置失败！");
        }


        return  ajax;
    }

    private AjaxResult checkRadarWaveDeviceLocation(RadarWaveDeviceRequest radarWaveDeviceRequest){
        //顶装时: 前后距离及左右距离和大于0.3小于4
        if ("1".equals(radarWaveDeviceRequest.getInstallPosition())){
            if (radarWaveDeviceRequest.getDeviceLocationTop().getRoomLeft()==null||radarWaveDeviceRequest.getDeviceLocationTop().getRoomLeft().equals("")){
                return error("左边长度不能为空！");
            }
            if (radarWaveDeviceRequest.getDeviceLocationTop().getRoomRight()==null||radarWaveDeviceRequest.getDeviceLocationTop().getRoomRight().equals("")){
                return error("右边长度不能为空！");
            }

            if (radarWaveDeviceRequest.getDeviceLocationTop().getRoomFront()==null||radarWaveDeviceRequest.getDeviceLocationTop().getRoomFront().equals("")){
                return error("前距离不能为空！");
            }
            if (radarWaveDeviceRequest.getDeviceLocationTop().getRoomBehind()==null||radarWaveDeviceRequest.getDeviceLocationTop().getRoomBehind().equals("")){
                return error("后距离不能为空！");
            }
            if (radarWaveDeviceRequest.getDeviceLocationTop().getRoomHeight()==null||radarWaveDeviceRequest.getDeviceLocationTop().getRoomHeight().equals("")){
                return error("高度不能为空！");
            }

            if((radarWaveDeviceRequest.getDeviceLocationTop().getRoomLeft().add(radarWaveDeviceRequest.getDeviceLocationTop().getRoomRight())).compareTo(new BigDecimal(4))==1 ||
                    (radarWaveDeviceRequest.getDeviceLocationTop().getRoomLeft().add(radarWaveDeviceRequest.getDeviceLocationTop().getRoomRight())).compareTo(new BigDecimal(0.3))==-1
            ){
                return error("左右侧距离之和必须大于等于0.3、小于等于4！");
            }
            if((radarWaveDeviceRequest.getDeviceLocationTop().getRoomBehind().add(radarWaveDeviceRequest.getDeviceLocationTop().getRoomFront())).compareTo(new BigDecimal(4))==1 ||
                    (radarWaveDeviceRequest.getDeviceLocationTop().getRoomBehind().add(radarWaveDeviceRequest.getDeviceLocationTop().getRoomFront())).compareTo(new BigDecimal(0.3))==-1
            ){
                return error("前后距离之和必须大于等于0.3、小于等于4！");
            }
            if(radarWaveDeviceRequest.getDeviceLocationTop().getRoomHeight().compareTo(new BigDecimal(4))==1||radarWaveDeviceRequest.getDeviceLocationTop().getRoomHeight().compareTo(new BigDecimal(0))==-1)
            {
                return error("设备高度必须介于0到4米之间！");
            }
        }else{
            if (radarWaveDeviceRequest.getDeviceLocationWall().getRoomLeft()==null||radarWaveDeviceRequest.getDeviceLocationWall().getRoomLeft().equals("")){
                return error("左边长度不能为空！");
            }
            if (radarWaveDeviceRequest.getDeviceLocationWall().getRoomRight()==null||radarWaveDeviceRequest.getDeviceLocationWall().getRoomRight().equals("")){
                return error("右边长度不能为空！");
            }
            if (radarWaveDeviceRequest.getDeviceLocationWall().getRoomLength()==null||radarWaveDeviceRequest.getDeviceLocationWall().getRoomLength().equals("")){
                return error("前距离长度不能为空！");

            }
            if((radarWaveDeviceRequest.getDeviceLocationWall().getRoomLeft().add(radarWaveDeviceRequest.getDeviceLocationWall().getRoomRight())).compareTo(new BigDecimal(4))==1){
                return error("左右侧距离之和必须小于等于4！");
            }
            if(radarWaveDeviceRequest.getDeviceLocationWall().getRoomLength().compareTo(new BigDecimal(4))==1||radarWaveDeviceRequest.getDeviceLocationWall().getRoomLength().compareTo(new BigDecimal(0))==-1)
            {
                return error("前距离长度必须介于0到4米之间！");
            }
        }
        return null;
    }

    @ApiOperation("绑定设备-设置子区域")
    @PostMapping("/setDevice")
    public AjaxResult setDevice(
            @RequestBody @Validated DeviceRequest deviceRequest) {
        if (deviceRequest.getDeviceId()==null||deviceRequest.getDeviceId().equals("")){
            return error("设备id不能为空！");
        }
        if (deviceRequest.getFamilyId()==null){
            return error("家庭id不能为空！");
        }
        if (deviceRequest.getFlag()==null||deviceRequest.getFlag().equals("")){
            return error("操作标识不能为空！");
        }
        TDevice device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(deviceRequest.getDeviceId()));
        if (deviceRequest.getFlag().equals("1")) {
            //一个房间只能绑定一个设备
            TDevice cond = new TDevice();
            cond.setRoomId(Long.valueOf(deviceRequest.getRoomId()));
            List<TDevice> devices = iDeviceService.selectDeviceList(cond);
            if (devices != null && devices.size() > 0) {
                throw new ServiceException("此房间已绑定设备！");
            }
            device.setParameter(new TDevice.RadarWaveDeviceSettings());
        }
        if (device==null){
            return error("无法查找到设备信息！");
        }
        if(!device.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return  error("非创建者无权限操作！");
        }
//        device.setName(deviceRequest.getDeviceName());
        if (deviceRequest.getLocation()!=null){
            device.setLocation(deviceRequest.getLocation());
        }
        if (device.getType().equals("0")){
            if (deviceRequest.getRoomId()==null){
                return error("房间id不能为空！");
            }

        }
        if (!deviceRequest.getFamilyId().equals("")){
            device.setFamilyId(Long.valueOf(deviceRequest.getFamilyId()));
        }else{
            device.setFamilyId(Long.valueOf("0"));
        }
        if (!deviceRequest.getRoomId().equals("")){
            device.setRoomId(Long.valueOf(deviceRequest.getRoomId()));
        }else{
            device.setRoomId(Long.valueOf("0"));
        }
        try {
            //绑定雷达波设备
            if (device.getType().equals("0")){
                int i = iDeviceService.setDevice(device);
                if (i==0){
                    return error("绑定我的设备失败！");
                }
            }else //绑定监护设备
                {
                int i = iDeviceService.updateDevice(device);
                if (i==0){
                    return error("绑定我的设备失败！");
                }
            }

        } catch (Exception e){
            log.error("绑定我的设备失败！",e);
           return error("绑定我的设备失败！");
        }
        return success();
    }

    @ApiOperation("解绑设备")
    @PostMapping("/relDevice")
    public AjaxResult relieveDevice(
            @RequestBody @Validated DeviceRequest deviceRequest) {
        if (deviceRequest.getFlag()==null||deviceRequest.getFlag().equals("")){
            return error("操作标识不能为空！");
        }
        if (!deviceRequest.getFlag().equals("3")){
            return error("操作标识错误解绑设备失败！");
        }
        if (deviceRequest.getDeviceId()==null||deviceRequest.getDeviceId().equals("")){
            return error("设备id不能为空！");
        }
        TDevice device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(deviceRequest.getDeviceId()));
        if (device==null){
            return error("无法查找到设备信息！");
        }
        if(!device.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return  error("非创建者无权限操作！");
        }
        device.setFamilyId(null);
        device.setRoomId(null);
        try {
            int i = iDeviceService.relievTDevice(device);
            if (i==0){
                return error("解绑我的设备失败！");
            }
        } catch (Exception e){
            log.error(e.getMessage());
            return error("解绑我的设备失败！");
        }
        return success();
    }

    @ApiOperation("设置SOS电话")
    @PostMapping("/setSOSDevicePhone")
    public AjaxResult setSOSDevicephone(
            @RequestBody @Validated DevicePhoneRequest devicePhoneRequest) {
        AjaxResult ajax = AjaxResult.success();
        if (devicePhoneRequest.getList() == null){
            return error("电话列表不能为空！");
        }
        if (devicePhoneRequest.getMapSet() == null){
            devicePhoneRequest.setMapSet(new HashMap<>());
        }
        //查找设备信息
        TDevice device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(devicePhoneRequest.getDeviceId()));
        if (device == null){
            return error("设备信息不存在！");
        }
        if(!device.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return  error("非创建者无权限操作！");
        }
        if(!device.getType().equals("1")){
            return error("该设备不是监控设备！");
        }
        TDevice.WatchSettings parameter = (TDevice.WatchSettings)device.getParameter();
        if(parameter==null){
            parameter = new TDevice.WatchSettings();
        }
        parameter.setList(devicePhoneRequest.getList());
        parameter.setMapSet(devicePhoneRequest.getMapSet());
        device.setParameter(parameter);
        try {
            iDeviceService.updateDevice(device);
        } catch (Exception e){
           log.error(e.getMessage());
           return error("设置电话失败！");
        }
        return ajax;
    }

    @ApiOperation("设置普通电话")
    @PostMapping("/setDevicephone")
    public AjaxResult setDevicephone(
            @RequestBody @Validated DevicePhoneRequest devicePhoneRequest) {
        return setSOSDevicephone(devicePhoneRequest);
    }

    @ApiOperation("创建普通号码")
    @PostMapping("/creDevicePhone")
    public AjaxResult createDevicePhone(
            @RequestBody @Validated DevicePhoneRequest devicePhoneRequest) {
        return setSOSDevicephone(devicePhoneRequest);

    }

    @ApiOperation("删除普通号码")
    @PostMapping("/remDevicePhone")
    public AjaxResult removeDevicePhone(
            @RequestBody @Validated DevicePhoneRequest devicePhoneRequest) {
        return setSOSDevicephone(devicePhoneRequest);
    }


    @ApiOperation("获取设备的电话列表")
    @PostMapping("/getDevicePhone")
    public AjaxResult getDevicePhone(
            @RequestBody @Validated DevicePhoneRequest devicePhoneRequest) {
        AjaxResult ajax = AjaxResult.success();
        //查找设备信息
        TDevice device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(devicePhoneRequest.getDeviceId()));
        if (device == null){
            throw new ServiceException("设备信息不存在!");
        }
        if(!device.getType().equals("1")){
            throw new ServiceException("该设备不是监控设备!");
        }
        if(!device.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            throw new ServiceException("无权限获取本设备通讯录!");
        }
        List<DevicePhone> list = new ArrayList<DevicePhone>();
        if(device.getParameter()==null){
            return AjaxResult.success(null);
        }
        return AjaxResult.success(device.getParameter());
    }

    @ApiOperation("获取家庭设备数量")
    @ApiImplicitParam(name = "familyId", value = "家庭ID", required = true, dataType = "int", paramType = "query", dataTypeClass = Integer.class)
    @GetMapping("/getFamilyNum")
    public String getFamilyNum(Long familyId) {
        TDevice tDevice = new TDevice();
        tDevice.setFamilyId(familyId);
        List<TDevice> list = iDeviceService.selectDeviceList(tDevice);
        return String.valueOf(list.size());
    }

    @ApiOperation("获取家庭设备数量(在线)")
    @ApiImplicitParam(name = "familyId", value = "家庭ID", required = true, dataType = "int", paramType = "query", dataTypeClass = Integer.class)
    @GetMapping("/getFNumOnline")
    public String getFamilyNumOnline(Long familyId) {
        TDevice tDevice = new TDevice();
        tDevice.setFamilyId(familyId);
        List<TDevice> list = iDeviceService.selectDeviceList(tDevice);
        //设备在线信息
        int number = 0;
        for (int i = 0; i <list.size() ; i++) {
            TDevice item = list.get(i);
            if(item.getOnlineFlag()!=null && item.getOnlineFlag().equals("1")){
                number = number++;
            }
        }
        return String.valueOf(number);
    }

    @ApiOperation("获取设备位置")
    @ApiImplicitParam(name = "deviceId", value = "设备ID", required = true, dataType = "int", paramType = "query", dataTypeClass = Integer.class)
    @GetMapping("/getDeviceNow")
    public AjaxResult getDeviceNow(Long deviceId) {
        TDevice tDevice = iDeviceService.selectDeviceByDeviceId(deviceId);
        if (!tDevice.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return error("非创建者无权获取设备信息！");
        }
        if(!tDevice.getType().equals("1")){
            return error("设备非监控设备！");
        }
        if(tDevice!=null){
            //经 度
            String longitude ="213123213992.2132132";
            //纬度
            String latitude ="10002932931.2123213";
            //地址
            String address = "88888888888";
            Map<String,String > map = new HashMap<String,String>();
            map.put("longitude",longitude);
            map.put("latitude",latitude);
            map.put("address",address);
            String nowLocation = JSON.toJSONString(map);
            tDevice.setNowLoacation(nowLocation);
        }else{
            return AjaxResult.error("获取设备信息失败！");
        }
        return AjaxResult.success(tDevice);
    }

    @ApiOperation("获取当前用户未绑定设备信息")
    @GetMapping("/getUnDeviceInfo")
    public AjaxResult getUnDeviceInfo() {
        TDevice cond = new TDevice();
        cond.setMemberId(this.getLoginUser().getMemberId());
        List<TDevice> TDevices = iDeviceService.selectDeviceList(cond);
        return success(TDevices);
    }

    @ApiOperation("开启获取设备位置(实时)")
    @ApiImplicitParam(name = "deviceId", value = "设备ID", required = true, dataType = "string", paramType = "query", dataTypeClass = String.class)
    @GetMapping("/startNowInfo")
    public AjaxResult startNowInfo(String deviceId) {
        TDevice tDevice = iDeviceService.selectDeviceByDeviceId(Long.valueOf(deviceId));
        if (!tDevice.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return error("非创建者无权获取设备信息！");
        }
        if(tDevice!=null){
            redisCache.setCacheObject(TARGET_LOCATION_SWITCH_KEY+tDevice.getNo(), true);
        }
        return success();
    }

    @ApiOperation("关闭获取设备位置(实时)")
    @ApiImplicitParam(name = "deviceId", value = "设备ID", required = true, dataType = "string", paramType = "query", dataTypeClass = String.class)
    @GetMapping("/endNowInfo")
    public AjaxResult getUnDeviceInfo(String deviceId) {
        TDevice tDevice = iDeviceService.selectDeviceByDeviceId(Long.valueOf(deviceId));
        if (!tDevice.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return error("非创建者无权获取设备信息！");
        }
        if(tDevice!=null){
            redisCache.setCacheObject(TARGET_LOCATION_SWITCH_KEY+tDevice.getNo(), false);
        }
        return success();
    }

    @ApiOperation("获取设备位置(实时)")
    @ApiImplicitParam(name = "deviceId", value = "设备ID", required = true, dataType = "string", paramType = "query", dataTypeClass = String.class)
    @GetMapping("/getNowInfo")
    public AjaxResult getNowInfo(String deviceId) {
        TDevice tDevice = iDeviceService.selectDeviceByDeviceId(Long.valueOf(deviceId));
        if (!tDevice.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return error("非创建者无权获取设备信息！");
        }
        List<TrackerTargetVo> tvo = iDeviceService.getRealLocationMonitorByDeviceNo(tDevice.getNo());
        List<ExtraVo> evo = iDeviceService.getRealLocationExtraByDeviceNo(tDevice.getNo());
        List<nowLocation> locations = new ArrayList<nowLocation>();
        if (tvo!=null&&tvo.size()>0){
            for (TrackerTargetVo item:tvo){
                nowLocation i = new nowLocation();
                i.setId(item.getId());
                i.setxPosCm(item.getxPosCm());
                i.setyPosCm(item.getyPosCm());
                i.setzPosCm(item.getzPosCm());
                i.setState("0");
                if (evo!=null && evo.size()>0){
                    for (ExtraVo it :evo){
                        if (i.getxPosCm().equals(String.valueOf(it.getxCm()))&&
                                i.getyPosCm().equals(String.valueOf(it.getyCm()))&&
                                i.getzPosCm().equals(String.valueOf(it.getzCm()))){
                            i.setId(String.valueOf(it.getTargetId()));
                            i.setxPosCm(String.valueOf(it.getxCm()));
                            i.setyPosCm(String.valueOf(it.getyCm()));
                            i.setzPosCm(String.valueOf(it.getzCm()));
                            i.setState("1");
                            locations.add(i);
                        }else{
                            nowLocation iNew = new nowLocation();
                            iNew.setId(String.valueOf(it.getTargetId()));
                            iNew.setxPosCm(String.valueOf(it.getxCm()));
                            iNew.setyPosCm(String.valueOf(it.getyCm()));
                            iNew.setzPosCm(String.valueOf(it.getzCm()));
                            iNew.setState("1");
                            locations.add(iNew);
                            locations.add(i);
                        }
                    }
                }else{
                    locations.add(i);
                }
            }
        }
        return success(locations);
    }
    private AjaxResult checkPhoneInfo(DevicePhone devicePhone ,AjaxResult ajax){
        if (devicePhone.getPhoneName()==null||devicePhone.getPhoneName().equals("")){
            ajax = AjaxResult.error("联系人名称不能为空！");
            return ajax;
        }
        if (devicePhone.getPhone().length()>20){
            ajax = AjaxResult.error("联系人名称20个字符！");
            return ajax;

        }
        if (devicePhone.getPhone()==null||devicePhone.getPhone().equals("")){
            ajax = AjaxResult.error("联系人电话不能为空！");
            return ajax;
        }
        if (devicePhone.getPhone().length()>11){
            ajax = AjaxResult.error("联系人电话长度11个字符！");
            return ajax;
        }

        if (devicePhone.getPhone().length()>20){
            ajax = AjaxResult.error("联系人名称20个字符！");
            return ajax;
        }
        return null;
    }

    @ApiOperation("获取人员监控状态")
    @ApiImplicitParam(name = "deviceId", value = "设备ID", required = true, dataType = "int", paramType = "query", dataTypeClass = Integer.class)
    @GetMapping("/getPeopleStatus")
    public AjaxResult getPeopleStatus(Long deviceId) {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("peopleStatus", "0");
        return ajax;
    }

    @ApiOperation("设置预警规则")
    @PostMapping("/setRadarWaveNobodyWarn")
    public AjaxResult setRadarWaveNobodyWarn(
            @RequestBody @Validated DeviceWarnRequest deviceWarnRequest) {
        if (deviceWarnRequest.getDeviceId()==null||deviceWarnRequest.getDeviceId().equals("")){
            return error("设备id不能为空！");
        }
        TDevice device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(deviceWarnRequest.getDeviceId()));
        if (device==null){
            return error("无法查找到设备信息！");
        }
        if(!device.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return  error("非创建者无权限操作！");
        }
        if(deviceWarnRequest.getStartTime()==null  || deviceWarnRequest.getEndTime()==null)
        {
            return  error("开始结束时间不能为空！");
        }

        if ("0".equals(deviceWarnRequest.getDateType())) {
            if (deviceWarnRequest.getStartDate() == null || deviceWarnRequest.getEndDate() == null) {
                return error("开始结束日期不能为空！");
            }
            if (deviceWarnRequest.getStartDate().getTime()  > deviceWarnRequest.getEndDate().getTime() ) {
                return error("开始不能大于结束日期！");
            }
        }else {
            if (deviceWarnRequest.getWeek() == null || deviceWarnRequest.getWeek().length==0) {
                return error("星期不能为空！");
            }
        }


        Long deviceId = Long.valueOf(deviceWarnRequest.getDeviceId());
        TDevice.RadarWaveDeviceSettings radarwave = (TDevice.RadarWaveDeviceSettings)device.getParameter();
        if(radarwave.getDeviceWarnParameter()!=null) {
            //        DeviceWarnParameter deviceWarnParameter=new DeviceWarnParameter();
            radarwave.getDeviceWarnParameter().setFallWarn(deviceWarnRequest.getFallWarn());
            radarwave.getDeviceWarnParameter().getNobodyWarn().setNoBodyContinue(deviceWarnRequest.getNoBodyContinue());
            radarwave.getDeviceWarnParameter().getNobodyWarn().setNoBody(deviceWarnRequest.getNoBody());
            DeviceWarnParameter.WarnRule warnRule = null;
            //校验是否已有规则，已有取出规则否则为空
            if (deviceWarnRequest.getRuleNo() != null && !deviceWarnRequest.getRuleNo().equals("")
                    && CollectionUtils.isNotEmpty(radarwave.getDeviceWarnParameter().getNobodyWarn().getWarnRules())) {

                warnRule = radarwave.getDeviceWarnParameter().getNobodyWarn().getWarnRules().stream()
                        .filter(p -> deviceWarnRequest.getRuleNo().equals(p.getRuleNo() + ""))
                        .findAny()
                        .orElse(null);
            }

            //规则为空的话 初始化规则列表
            if (radarwave.getDeviceWarnParameter().getNobodyWarn().getWarnRules() == null) {
                radarwave.getDeviceWarnParameter().getNobodyWarn().setWarnRules(new ArrayList<>());
            }
            //
            if (warnRule == null) {
                warnRule = new DeviceWarnParameter.WarnRule();
                warnRule.setWarnRuleName(deviceWarnRequest.getWarnRuleName());
                warnRule.setRuleSwitch(deviceWarnRequest.getRuleSwitch());
                warnRule.setDateType(deviceWarnRequest.getDateType());
                //规则编号用于规则列表删改操作
                warnRule.setRuleNo(IdUtils.fastSimpleUUID());
                if("1".equals(deviceWarnRequest.getDateType()))
                {
                    warnRule.setWeek(deviceWarnRequest.getWeek());

                }   else
                {
                    warnRule.setEndDate(deviceWarnRequest.getEndDate());
                    warnRule.setStartDate(deviceWarnRequest.getStartDate());
                }
                warnRule.setStartTime(deviceWarnRequest.getStartTime());
                warnRule.setEndTime(deviceWarnRequest.getEndTime());
                radarwave.getDeviceWarnParameter().getNobodyWarn().getWarnRules().add(warnRule);
            }else{
                warnRule.setWarnRuleName(deviceWarnRequest.getWarnRuleName());
//                warnRule.setRuleSwitch(deviceWarnRequest.getRuleSwitch());
                warnRule.setDateType(deviceWarnRequest.getDateType());
                //规则编号用于规则列表删改操作
                warnRule.setRuleNo(IdUtils.fastSimpleUUID());
                if("1".equals(deviceWarnRequest.getDateType()))
                {
                    warnRule.setWeek(deviceWarnRequest.getWeek());

                }   else
                {
                    warnRule.setEndDate(deviceWarnRequest.getEndDate());
                    warnRule.setStartDate(deviceWarnRequest.getStartDate());
                }
                warnRule.setStartTime(deviceWarnRequest.getStartTime());
                warnRule.setEndTime(deviceWarnRequest.getEndTime());
            }

        }
        try {
            //设置雷达波设备策略规则
            if (device.getType().equals("0")){
                int i = iDeviceService.setSettings(deviceId,radarwave);
                if (i==0){
                    return error("设置预警策略失败！");
                }
            }

        } catch (Exception e){
            log.error("设置预警策略失败！",e);
            return error("设置预警策略失败！");
        }
        //查询
        return success(radarwave.getDeviceWarnParameter().getNobodyWarn().getWarnRules());
    }

    @ApiOperation("设置预警规则")
    @PostMapping("/setLeaveBedWarn")
    public AjaxResult setLeaveBedWarn(
            @RequestBody @Validated DeviceWarnRequest deviceWarnRequest) {
        if (deviceWarnRequest.getDeviceId()==null||deviceWarnRequest.getDeviceId().equals("")){
            return error("设备id不能为空！");
        }
        TDevice device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(deviceWarnRequest.getDeviceId()));
        if (device==null){
            return error("无法查找到设备信息！");
        }
        if(!device.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return  error("非创建者无权限操作！");
        }
        TDevice.RadarWaveDeviceSettings radarwave = (TDevice.RadarWaveDeviceSettings)device.getParameter();


        LeaveBedWarnParameter leaveBedWarnParameter =new LeaveBedWarnParameter();
        LeaveBedWarnParameter.Bed bed = new LeaveBedWarnParameter.Bed();
        bed.setName(deviceWarnRequest.getBedName());
        //设置编号
        bed.setBedNo(IdUtils.fastSimpleUUID());
        bed.setDateType(deviceWarnRequest.getDateType());
        bed.setLeaveBedInterval(deviceWarnRequest.getLeaveBedInterval());
        bed.setIntervalTime(deviceWarnRequest.getIntervalTime());
        if("1".equals(deviceWarnRequest.getDateType()))
        {
            bed.setWeek(deviceWarnRequest.getWeek());

        }   else
        {
            bed.setStartDate(deviceWarnRequest.getStartDate());
            bed.setEndDate(deviceWarnRequest.getEndDate());
        }

        bed.setStartTime(deviceWarnRequest.getStartTime());
        bed.setEndTime(deviceWarnRequest.getEndTime());


        List<LeaveBedWarnParameter.Bed> beds = new ArrayList<>();
        beds.add(bed);
        leaveBedWarnParameter.setBeds(beds);
        radarwave.setLeaveBedWarnParameter(leaveBedWarnParameter);

        device.setParameter(radarwave);

        try {
            //绑定雷达波设备
            if (device.getType().equals("0")){
                int i = iDeviceService.setDevice(device);
                if (i==0){
                    return error("设置离床预警策略失败！");
                }
            }

        } catch (Exception e){
            log.error("设置离床预警策略失败！",e);
            return error("设置离床预警策略失败！");
        }
        return success();
    }


    @ApiOperation("删除无人预警规则")
    @PostMapping("/delRadarWaveNobodyWarn")
    public AjaxResult delRadarWaveNobodyWarn(
            @RequestBody @Validated DeviceWarnRequest deviceWarnRequest) {
        AjaxResult ajax = AjaxResult.success();
        if (deviceWarnRequest.getDeviceId()==null||deviceWarnRequest.getDeviceId().equals("")){
            return error("设备id不能为空！");
        }
        if (deviceWarnRequest.getRuleNo()==null||deviceWarnRequest.getRuleNo().equals("")){
            return error("预警规则编号不能为空！");
        }
        Long deviceId = Long.valueOf(deviceWarnRequest.getDeviceId());
        //根据设备id获取参数信息
        TDevice.DeviceParameter parameter = iDeviceService.loadSettings(deviceId);
        if(!(parameter instanceof TDevice.RadarWaveDeviceSettings)){
            return error("设备不是雷达波设备！");
        }


        //雷达波参数
        TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)parameter;
        //校验是否已有规则，已有取出规则否则为空
        DeviceWarnParameter.WarnRule warnRule = null;
        if (deviceWarnRequest.getRuleNo() != null && !deviceWarnRequest.getRuleNo().equals("")
                && CollectionUtils.isNotEmpty(radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getWarnRules())) {

            warnRule = radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getWarnRules().stream()
                    .filter(p -> deviceWarnRequest.getRuleNo().equals(p.getRuleNo() + ""))
                    .findAny()
                    .orElse(null);
        }
       if(warnRule==null){
           return  error("不存在此规则，删除失败！");
       }
        //删除规则
        radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getWarnRules()
                .removeIf(item->item.getRuleNo().equals(deviceWarnRequest.getRuleNo()));

        try {
            //更新雷达波参数
            iDeviceService.setSettings(deviceId,radarWaveDeviceSettings);
        } catch (Exception e){
            log.error(">>>>>>",e);
            ajax = AjaxResult.error("删除预警规则失败");
            return ajax;
        }

        return ajax;
    }

    @ApiOperation("设置预警规则")
    @PostMapping("/delLeaveBedWarn")
    public AjaxResult delLeaveBedWarn(
            @RequestBody @Validated DeviceWarnRequest deviceWarnRequest) {
        AjaxResult ajax = AjaxResult.success();
        if (deviceWarnRequest.getDeviceId()==null||deviceWarnRequest.getDeviceId().equals("")){
            return error("设备id不能为空！");
        }
        if (deviceWarnRequest.getBedNo()==null||deviceWarnRequest.getBedNo().equals("")){
            return error("床编号不能为空！");
        }
        Long deviceId = Long.valueOf(deviceWarnRequest.getDeviceId());
        //根据设备id获取参数信息
        TDevice.DeviceParameter parameter = iDeviceService.loadSettings(deviceId);
        if(!(parameter instanceof TDevice.RadarWaveDeviceSettings)){
            return error("设备不是雷达波设备！");
        }
        //雷达波参数
        TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)parameter;
        DeviceWarnParameter.WarnRule warnRule = null;
        if (deviceWarnRequest.getRuleNo() != null && !deviceWarnRequest.getRuleNo().equals("")
                && CollectionUtils.isNotEmpty(radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getWarnRules())) {

            warnRule = radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getWarnRules().stream()
                    .filter(p -> deviceWarnRequest.getRuleNo().equals(p.getRuleNo() + ""))
                    .findAny()
                    .orElse(null);
        }
        if(warnRule==null){
            return  error("不存在此规则，删除失败！");
        }
        //删除规则
        radarWaveDeviceSettings.getLeaveBedWarnParameter().getBeds()
                .removeIf(item->item.getBedNo().equals(deviceWarnRequest.getBedNo()));

        try {
            //更新雷达波参数
            iDeviceService.setSettings(deviceId,radarWaveDeviceSettings);
        } catch (Exception e){
            log.error(">>>>>>",e);
            ajax = AjaxResult.error("删除离床预警规则失败");
            return ajax;
        }

        return ajax;
    }
}
