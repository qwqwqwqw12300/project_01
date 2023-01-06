package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.DevicePhoneRequest;
import com.newlandnpt.varyar.common.core.domain.model.DeviceRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.system.domain.Device;
import com.newlandnpt.varyar.common.core.domain.entity.DeviceParameter;
import com.newlandnpt.varyar.common.core.domain.entity.DevicePhone;
import com.newlandnpt.varyar.system.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    /**
     * 获取设备列表
     * */
    @PostMapping("/list")
    public TableDataInfo list(
            @RequestBody @Validated DeviceRequest deviceRequest) {
        startPage();
        Long memberId = getLoginUser().getMemberId();
        Map<String ,Long> map =new HashMap<String ,Long>();
        map.put("memberId",memberId);
        map.put("familyId",Long.valueOf(deviceRequest.getFamilyId()));
        map.put("roomId",Long.valueOf(deviceRequest.getRoomId()));
        List<Device> list = iDeviceService.selectDeviceByMemberId(map);
        return getDataTable(list);
    }
    /**
     * 获取设备列表
     * */
    @PostMapping("/listState")
    public TableDataInfo listState(
            @RequestBody @Validated DeviceRequest deviceRequest) {
        startPage();
        Long memberId = getLoginUser().getMemberId();
        Map<String ,Long> map =new HashMap<String ,Long>();
        map.put("memberId",memberId);
        map.put("familyId",Long.valueOf(deviceRequest.getFamilyId()));
        map.put("roomId",Long.valueOf(deviceRequest.getRoomId()));
        List<Device> list = iDeviceService.selectDeviceByMemberId(map);
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
        Device device = new Device();
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
        device.setCreateBy(getLoginUser().getUsername());
        device.setCreateTime(DateUtils.getNowDate());
        try {
            iDeviceService.insertDevice(device);
        } catch (Exception e){
            ajax = AjaxResult.error("新增我的设备失败！");
            return ajax;
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
            ajax = AjaxResult.error("设备id不能为空！");
            return ajax;
        }
        Device device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(deviceRequest.getDeviceId()));
        if (device==null){
            ajax = AjaxResult.error("无法查找到设备信息！");
            return ajax;
        }
        device.setName(deviceRequest.getDeviceName());
        device.setNo(deviceRequest.getDeviceNo());
        device.setType(deviceRequest.getDeviceType());
        device.setLocation(deviceRequest.getLocation());
        try {
            iDeviceService.updateDevice(device);
        } catch (Exception e){
            ajax = AjaxResult.error("修改我的设备失败！");
            return ajax;
        }
        return ajax;
    }
    private AjaxResult checkInfo(DeviceRequest deviceRequest,AjaxResult ajax){
        if (deviceRequest.getDeviceName().equals("")|| deviceRequest.getDeviceName()==null){
            ajax = AjaxResult.error("设备名称不能为空！");
            return ajax;
        }
        if (deviceRequest.getDeviceType().equals("")|| deviceRequest.getDeviceType()==null){
            ajax = AjaxResult.error("设备类型不能为空！");
            return ajax;
        }
        if (deviceRequest.getDeviceNo().equals("")|| deviceRequest.getDeviceNo()==null){
            ajax = AjaxResult.error("设备编号不能为空！");
            return ajax;
        }
        if (deviceRequest.getLocation().equals("")|| deviceRequest.getLocation()==null){
            ajax = AjaxResult.error("设备位置不能为空！");
            return ajax;
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
            ajax = AjaxResult.error("设备id不能为空！");
            return ajax;
        }
        if (deviceRequest.getFamilyId().equals("")|| deviceRequest.getFamilyId()==null){
            ajax = AjaxResult.error("家庭id不能为空！");
            return ajax;
        }
        if (deviceRequest.getRoomId().equals("")|| deviceRequest.getRoomId()==null){
            ajax = AjaxResult.error("房间id不能为空！");
            return ajax;
        }
        Device device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(deviceRequest.getDeviceId()));
        if (device==null){
            ajax = AjaxResult.error("无法查找到设备信息！");
            return ajax;
        }
        device.setFamilyId(Long.valueOf(deviceRequest.getFamilyId()));
        device.setRoomId(Long.valueOf(deviceRequest.getRoomId()));
        try {
            iDeviceService.updateDevice(device);
        } catch (Exception e){
            ajax = AjaxResult.error("修改我的设备失败！");
            return ajax;
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
        //查找设备信息
        Device device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(devicePhoneRequest.getDeviceId()));
        if (device == null){
            ajax = AjaxResult.error("设备信息不存在！");
            return ajax;
        }
        List<DevicePhone> list = devicePhoneRequest.getList();
        //校验是否有sos电话信息
        Boolean sosflag = false;
        for(DevicePhone item :list){
            if (item.getType().equals("0")){
                checkPhoneInfo(item,ajax);
                sosflag =true;
            }
        }
        if (!sosflag){
            ajax = AjaxResult.error("设备信息sos电话信息不存在！");
            return ajax;
        }
        DeviceParameter dpt = new DeviceParameter();
        dpt.setList(list);
        device.setParameter(dpt);
        try {
            iDeviceService.updateDevice(device);
        } catch (Exception e){
            ajax = AjaxResult.error("设置SOS电话失败！");
            return ajax;
        }
        return ajax;
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
            ajax = AjaxResult.error("设备名称不能为空！");
            return ajax;
        }
        if (devicePhone.getPhone().length()>20){
            ajax = AjaxResult.error("联系人名称20个字符！");
            return ajax;
        }
        return null;
    }
    /**
     * 设置普通电话  创建普通号码  删除普通号码
     * */
    @PostMapping("/setDevicephone")
    public AjaxResult setDevicephone(
            @RequestBody @Validated DevicePhoneRequest devicePhoneRequest) {
        AjaxResult ajax = AjaxResult.success();
        //查找设备信息
        Device device = iDeviceService.selectDeviceByDeviceId(Long.valueOf(devicePhoneRequest.getDeviceId()));
        if (device == null){
            ajax = AjaxResult.error("设备信息不存在！");
            return ajax;
        }
        List<DevicePhone> list = devicePhoneRequest.getList();
        for(DevicePhone item :list){
            if (item.getType().equals("P")){
                checkPhoneInfo(item,ajax);
            }
        }
        DeviceParameter dpt = new DeviceParameter();
        dpt.setList(list);
        device.setParameter(dpt);
        try {
            iDeviceService.updateDevice(device);
        } catch (Exception e){
            ajax = AjaxResult.error("设置SOS电话失败！");
            return ajax;
        }
        return ajax;
    }
    /**
     * 创建普通号码
     * */
    @PostMapping("/creDevicePhone")
    public AjaxResult createDevicePhone(
            @RequestBody @Validated DevicePhoneRequest devicePhoneRequest) {
        AjaxResult ajax = AjaxResult.success();
        return ajax;

    }
    /**
     * 删除普通号码
     * */
    @PostMapping("/remDevicePhone")
    public AjaxResult removeDevicePhone(
            @RequestBody @Validated DevicePhoneRequest devicePhoneRequest) {
        AjaxResult ajax = AjaxResult.success();
        return ajax;
    }

}
