package com.newlandnpt.varyar.api.controller.system;

import com.alibaba.fastjson2.JSON;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.entity.DeviceLocation;
import com.newlandnpt.varyar.common.core.domain.entity.DeviceRoomParameter;
import com.newlandnpt.varyar.common.core.domain.entity.nowLocation;
import com.newlandnpt.varyar.common.core.domain.model.DevicePhoneRequest;
import com.newlandnpt.varyar.common.core.domain.model.DeviceRequest;
import com.newlandnpt.varyar.common.core.domain.vo.ExtraVo;
import com.newlandnpt.varyar.common.core.domain.vo.TrackerTargetVo;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.common.core.domain.entity.DevicePhone;
import com.newlandnpt.varyar.system.service.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.newlandnpt.varyar.common.constant.CacheConstants.TARGET_LOCATION_SWITCH_KEY;

/**
 * 设备管理
 *
 * @author newlandnpt
 */
@RestController
@RequestMapping("/api/device")
public class DeviceController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DeviceController.class);
    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private RedisCache redisCache;
    /**
     * 获取设备列表
     * */
    @PostMapping("/list")
    public TableDataInfo list( @RequestBody @Validated DeviceRequest deviceRequest) {
        startPage();
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
    /**
     * 获取设备列表
     * */
    @PostMapping("/listState")
    public TableDataInfo listState( @RequestBody @Validated DeviceRequest deviceRequest) {
        startPage();
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
    /**
     * 创建设备
     * */
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
            }
        } catch (Exception e){
            log.error(e.getMessage());
            return  error("新增我的设备失败！");
        }
        return ajax;
    }
    /**
     * 修改设备-修改设备基础信息
     * */
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

    /**
     * 删除设备
     * */
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
        }
        return null;
    }

    /**
     * 绑定设备-设置子区域
     * */
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
        if (deviceRequest.getFlag().equals("1")){
            //一个房间只能绑定一个设备
            TDevice cond = new TDevice();
            cond.setRoomId(Long.valueOf(deviceRequest.getRoomId()));
            List<TDevice> devices = iDeviceService.selectDeviceList(cond);
            if (devices!=null && devices.size()>0){
                throw new ServiceException("此房间已绑定设备！");
        }
        }
        TDevice device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(deviceRequest.getDeviceId()));
        if (device==null){
            return error("无法查找到设备信息！");
        }
        if(!device.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return  error("非创建者无权限操作！");
        }
        device.setName(deviceRequest.getDeviceName());
        if (deviceRequest.getLocation()!=null){
            device.setLocation(deviceRequest.getLocation());
        }
        if (device.getType().equals("0")){
            if (deviceRequest.getRoomId()==null){
                return error("房间id不能为空！");
            }

            //雷达波 判断设备位置信息
            AjaxResult item = checkDeviceLocation(deviceRequest);
            if (item!=null){
                return item;
            }
            TDevice.RadarWaveDeviceSettings radarwave = (TDevice.RadarWaveDeviceSettings)device.getParameter();
            if (radarwave == null){
                radarwave = new TDevice.RadarWaveDeviceSettings();
            }
            //设备位置信息
            DeviceLocation location = new DeviceLocation();
            location.setRoomHeight(deviceRequest.getRoomHeight());
            location.setRoomLength(deviceRequest.getRoomLength());
            location.setRoomLeft(deviceRequest.getRoomLeft());
            location.setRoomRight(deviceRequest.getRoomRight());
            DeviceRoomParameter dr = new DeviceRoomParameter();
            radarwave.setDeviceLocation(location);
            //设备参数设置
            dr.setInMonitorFlag(deviceRequest.getInMonitorFlag());
            dr.setOutMonitorFlag(deviceRequest.getOutMonitorFlag());
            dr.setExistFlag(deviceRequest.getExistFlag());
            dr.setFallFlag(deviceRequest.getFallFlag());
            dr.setEntryTime(deviceRequest.getEntryTime());
            dr.setDepartureTime(deviceRequest.getDepartureTime());
            dr.setStartTime(deviceRequest.getStartTime());
            dr.setEndTime(deviceRequest.getEndTime());
            radarwave.setDeviceRoomParameter(dr);
            device.setParameter(radarwave);
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
            log.error(e.getMessage());
           return error("绑定我的设备失败！");
        }
        return success();
    }
    /**
     * 解绑设备
     * */
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
    /**
     * 设置SOS电话
     * */
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

    /**
     * 设置普通电话  创建普通号码  删除普通号码
     * */
    @PostMapping("/setDevicephone")
    public AjaxResult setDevicephone(
            @RequestBody @Validated DevicePhoneRequest devicePhoneRequest) {
        return setSOSDevicephone(devicePhoneRequest);
    }
    /**
     * 创建普通号码
     * */
    @PostMapping("/creDevicePhone")
    public AjaxResult createDevicePhone(
            @RequestBody @Validated DevicePhoneRequest devicePhoneRequest) {
        return setSOSDevicephone(devicePhoneRequest);

    }
    /**
     * 删除普通号码
     * */
    @PostMapping("/remDevicePhone")
    public AjaxResult removeDevicePhone(
            @RequestBody @Validated DevicePhoneRequest devicePhoneRequest) {
        return setSOSDevicephone(devicePhoneRequest);
    }

    /**获取手表设备的电话列表*/
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

    /**
     * 获取家庭设备数量
     * */
    @GetMapping("/getFamilyNum")
    public String getFamilyNum(Long familyId) {
        TDevice tDevice = new TDevice();
        tDevice.setFamilyId(familyId);
        List<TDevice> list = iDeviceService.selectDeviceList(tDevice);
        return String.valueOf(list.size());
    }
    /**
     * 获取家庭设备数量(在线)
     * */
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
    /**
     * 获取设备位置
     * */
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
    /**
     * 获取未绑定设备位置
     * */
    @GetMapping("/getUnDeviceInfo")
    public AjaxResult getUnDeviceInfo(Long deviceId) {
        TDevice cond = new TDevice();
        cond.setMemberId(this.getLoginUser().getMemberId());
        List<TDevice> TDevices = iDeviceService.selectDeviceList(cond);
        return success(TDevices);
    }
    /**
     * 开启获取设备位置(实时)
     * */
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
    /**
     * 关闭获取设备位置(实时)
     * */
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
    /**
     * 获取设备位置(实时)
     * */
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

}
