package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.DevicePhoneRequest;
import com.newlandnpt.varyar.common.core.domain.model.DeviceRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.common.core.domain.entity.DeviceParameter;
import com.newlandnpt.varyar.common.core.domain.entity.DevicePhone;
import com.newlandnpt.varyar.system.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 设备管理
 *
 * @author newlandnpt
 */
@RestController
@RequestMapping("/api/device")
public class DeviceController extends BaseController {

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
        Map map = new HashMap();
        map.put("memberId",this.getLoginUser().getMemberId());
        map.put("familyId",deviceRequest.getFamilyId());
        map.put("roomId",deviceRequest.getRoomId());
        List<TDevice> list = iDeviceService.selectDeviceByMemberId(map);
        return getDataTable(list);
    }
    /**
     * 获取设备列表
     * */
    @GetMapping("/listState")
    public TableDataInfo listState() {
        startPage();
        List<TDevice> list = iDeviceService.selectDeviceByMember(this.getLoginUser().getMemberId());
        return getDataTable(list);
    }
    /**
     * 创建设备
     * */
    @PostMapping("/creDevice")
    public AjaxResult createDevice(
            @RequestBody @Validated DeviceRequest deviceRequest){
        AjaxResult ajax = AjaxResult.success();
        if(checkInfo(deviceRequest,ajax) != null){
           return ajax;
        }
        TDevice device = new TDevice();
        if (deviceRequest.getDeviceName().equals("")|| deviceRequest.getDeviceName()==null){
            deviceRequest.setDeviceId(this.getUsername()+"的设备");
        }
        device.setName(deviceRequest.getDeviceName());
        device.setNo(deviceRequest.getDeviceNo());
        device.setType(deviceRequest.getDeviceType());
        device.setRegisterTime(DateUtils.getNowDate());
        device.setLocation(deviceRequest.getLocation());
        device.setStatus("1");
        device.setOrgId(Long.valueOf("101"));
        device.setOrgName("个人机构");
        device.setDelFlag("0");
        device.setMemberId(getLoginUser().getMemberId());
        device.setCreateTime(DateUtils.getNowDate());
        device.setCreateById(String.valueOf(this.getLoginUser().getMemberId()));
        try {
            iDeviceService.insertDevice(device);
        } catch (Exception e){
            error("新增我的设备失败！");
        }
        return ajax;
    }
    /**
     * 修改设备
     * */
    @PostMapping("/editDevice")
    public AjaxResult editDevice(
            @RequestBody @Validated DeviceRequest deviceRequest){
        AjaxResult ajax = AjaxResult.success();
        if(checkInfo(deviceRequest,ajax) != null){
            return ajax;
        }
        if (deviceRequest.getDeviceId().equals("")|| deviceRequest.getDeviceId()==null){
            error("设备id不能为空！");
        }
        TDevice device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(deviceRequest.getDeviceId()));
        if(device.getCreateBy().equals(this.getLoginUser().getMemberId())){
            error("非创建者无权限修改！");
        }
        if (device==null){
            error("无法查找到设备信息！");
        }
        device.setName(deviceRequest.getDeviceName());
        device.setNo(deviceRequest.getDeviceNo());
        device.setType(deviceRequest.getDeviceType());
        device.setLocation(deviceRequest.getLocation());
        try {
            iDeviceService.updateDevice(device);
        } catch (Exception e){
            error("修改我的设备失败！");
        }
        return ajax;
    }
    private AjaxResult checkInfo(DeviceRequest deviceRequest,AjaxResult ajax){
        if (deviceRequest.getDeviceName().equals("")|| deviceRequest.getDeviceName()==null){
            error("设备名称不能为空！");
        }
        if (deviceRequest.getDeviceType().equals("")|| deviceRequest.getDeviceType()==null){
            error("设备类型不能为空！");
        }
        if (deviceRequest.getDeviceNo().equals("")|| deviceRequest.getDeviceNo()==null){
            error("设备编号不能为空！");
        }
        if (deviceRequest.getLocation().equals("")|| deviceRequest.getLocation()==null){
            error("设备位置不能为空！");
        }
        return null;
    }

    /**
     * 绑定\解绑设备
     * */
    @PostMapping("/setDevice")
    public AjaxResult setDevice(
            @RequestBody @Validated DeviceRequest deviceRequest) {
        AjaxResult ajax = AjaxResult.success();
        if (deviceRequest.getDeviceId().equals("")|| deviceRequest.getDeviceId()==null){
            error("设备id不能为空！");
        }
        if (deviceRequest.getFamilyId().equals("")|| deviceRequest.getFamilyId()==null){
            error("家庭id不能为空！");
        }
        if (deviceRequest.getRoomId().equals("")|| deviceRequest.getRoomId()==null){
            error("房间id不能为空！");
        }
        TDevice device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(deviceRequest.getDeviceId()));
        if (device==null){
            error("无法查找到设备信息！");
        }
        device.setFamilyId(Long.valueOf(deviceRequest.getFamilyId()));
        device.setRoomId(Long.valueOf(deviceRequest.getRoomId()));
        try {
            iDeviceService.updateDevice(device);
        } catch (Exception e){
            error("修改我的设备失败！");
        }
        return ajax;
    }
    /**
     * 设置SOS电话
     * */
    @PostMapping("/setSOSDevicePhone")
    public AjaxResult setSOSDevicephone(
            @RequestBody @Validated DevicePhoneRequest devicePhoneRequest) {
        AjaxResult ajax = AjaxResult.success();
        if (CollectionUtils.isEmpty(devicePhoneRequest.getList())){
            error("电话列表不能为空！");
        }
        //查找设备信息
        TDevice device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(devicePhoneRequest.getDeviceId()));
        if (device == null){
            error("设备信息不存在！");
        }
        if(!device.getType().equals("2")){
            error("该设备不是手表！");
        }
        List<DevicePhone> list = devicePhoneRequest.getList();
        //校验是否有sos电话信息
        Boolean sosflag = false;
        Set<String> types = new HashSet<String>();
        Set<String> type1 = new HashSet<String>();
        Set<String> type2 = new HashSet<String>();
        Set<String> type3 = new HashSet<String>();
        Set<String> type4 = new HashSet<String>();
        for(DevicePhone item :list){
            ajax = checkPhoneInfo(item,ajax);
            if(ajax != null){
                return ajax;
            }else {
                ajax = AjaxResult.success();
            }
            if (item.getType().equals("0")){
                sosflag =true;
                if (!types.add(item.getType())){
                    error("sos电话信息重复！");
                }
            }
            if(item.getType().equals("1")){
                if (!type1.add(item.getType())){
                    error("按钮1电话信息重复！");
                }
            }
            if(item.getType().equals("2")){
                if (!type2.add(item.getType())){
                    error("按钮2电话信息重复！");
                }
            }
            if(item.getType().equals("3")){
                if (!type3.add(item.getType())){
                    error("按钮3电话信息重复！");
                }
            }
            if(item.getType().equals("4")){
                if (!type4.add(item.getType())){
                    error("按钮4电话信息重复！");
                }
            }
        }
        if (!sosflag){
            error("设备信息sos电话信息不存在！");
        }
        DeviceParameter dpt = new DeviceParameter();
        dpt.setList(list);
        device.setParameter(dpt);
        try {
            iDeviceService.updateDevice(device);
        } catch (Exception e){
            error("设置SOS电话失败！");
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
    public TableDataInfo getDevicePhone(
            @RequestBody @Validated DevicePhoneRequest devicePhoneRequest) {
        AjaxResult ajax = AjaxResult.success();
        //查找设备信息
        TDevice device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(devicePhoneRequest.getDeviceId()));
        if (device == null){
            throw new ServiceException("设备信息不存在!");
        }
        if(!device.getType().equals("2")){
            throw new ServiceException("该设备不是手表!");
        }
        if(!device.getMemberId().equals(this.getLoginUser().getMemberId())){
            throw new ServiceException("无权限获取本设备通讯录!");
        }
        List<DevicePhone> list = new ArrayList<DevicePhone>();
        if( device.getParameter()==null || device.getParameter().getList()==null ){
            return getDataTable(list);
        }
        list = device.getParameter().getList();
        return getDataTable(list);
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


    private AjaxResult checkPhoneInfo(DevicePhone devicePhone ,AjaxResult ajax){
        if (devicePhone.getPhoneName().equals("")|| devicePhone.getPhoneName()==null){
            ajax = AjaxResult.error("联系人名称不能为空！");
            return ajax;
        }
        if (devicePhone.getPhone().length()>20){
            ajax = AjaxResult.error("联系人名称20个字符！");
            return ajax;

        }
        if (devicePhone.getPhone().equals("")|| devicePhone.getPhone()==null){
            ajax = AjaxResult.error("联系人电话不能为空！");
            return ajax;
        }
        if (devicePhone.getPhone().length()>11){
            ajax = AjaxResult.error("联系人电话长度11个字符！");
            return ajax;
        }
        if (devicePhone.getType().equals("")|| devicePhone.getType()==null){
            ajax = AjaxResult.error("联系人电话类型不能为空！");
            return ajax;
        }
        if (devicePhone.getPhone().length()>20){
            ajax = AjaxResult.error("联系人名称20个字符！");
            return ajax;
        }
        String type = devicePhone.getType();
        if (!(type.equals("0")||type.equals("1")||type.equals("2")
                ||type.equals("3")||type.equals("4")||type.equals("P"))){
            ajax = AjaxResult.error("联系人电话类型错误！");
            return ajax;
        }
        return null;
    }
}
