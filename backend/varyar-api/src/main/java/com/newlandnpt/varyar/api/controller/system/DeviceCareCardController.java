package com.newlandnpt.varyar.api.controller.system;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.newlandnpt.varyar.cloudBase.utils.HttpClientUtil;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.entity.ClassTimePeriod;
import com.newlandnpt.varyar.common.core.domain.entity.DeviceIncomingCall;
import com.newlandnpt.varyar.common.core.domain.entity.DevicePhone;
import com.newlandnpt.varyar.common.core.domain.entity.LocationJob;
import com.newlandnpt.varyar.common.utils.tcp.req.*;
import com.newlandnpt.varyar.system.domain.LocationGuard;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.domain.TDeviceFence;
import com.newlandnpt.varyar.system.service.IDeviceService;
import com.newlandnpt.varyar.system.service.impl.CommonDeviceFenceServiceImpl;
import com.newlandnpt.varyar.system.service.impl.CommonLocationGuardServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 设备电子牵挂卡 控制层
 * @author ljx
 * @date 2023/2/28
 */
@Api(tags = "设备电子牵挂卡")
@RestController
@RequestMapping("/api/careCard")
public class DeviceCareCardController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(DeviceCareCardController.class);

    @Autowired
    private IDeviceService iDeviceService;
    @Autowired
    private CommonDeviceFenceServiceImpl commonDeviceFenceService;
    @Autowired
    private CommonLocationGuardServiceImpl commonLocationGuardService;
    /**
     * 牵挂卡URL
     */
    private static final String DEVICE_CARE_CARD_URL = "http://127.0.0.1:8079/tcp/device/phone";

    /**
     * 设备类型
     */
    private static final String DEVICE_TYPE = "1";

    /**
     * 请求成功状态
     */
    private static final String REQUEST_SUCCESS_STATUS = "0";

    /**
     * 请求状态字段
     */
    private static final String REQUEST_STATUS ="status";


    @ApiOperation("开启远程监听")
    @PostMapping("/remoteMonitor")
    public AjaxResult remoteMonitor(@RequestBody RequestCallReq req){
        try{
            // 拼接url
            String url = DEVICE_CARE_CARD_URL + "/protect";
            // http请求
//            return httpRequest(url,req);
            return success();
        }catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("插入通讯录")
    @PostMapping("/insertAddressBook")
    public AjaxResult insertAddressBook(@RequestBody @Validated AddOrUpdateAddressBookReq req){
        // 入库
        TDevice device = iDeviceService.selectByDeviceNo(req.getDeviceNo());
        if(Objects.isNull(device)){
            return error("数据库中不存在此数据。");
        }
        if(DEVICE_TYPE.equals(device.getType())) {
            // 获取设备参数
            TDevice.WatchSettings object = getParameter(device);
            // 获取通讯录
            List<DeviceIncomingCall> addressBook = object.addressBookList;
            // 如果通讯录为空
            if (Objects.isNull(addressBook)) {
                addressBook = new ArrayList<>();
            }
            // 限制通讯录长度
            if(addressBook.size()>=20){
               return error("通讯录最多添加20个白名单用户。");
            }
            if(addressBook.stream().anyMatch(address -> address.getPhoneNumber().equals(req.getPhoneName()))){
                return error("通讯录中已存在相同号码。");
            }
            DeviceIncomingCall deviceIncomingCall = new DeviceIncomingCall();
            BeanUtils.copyProperties(req,deviceIncomingCall);
            deviceIncomingCall.setAddressBookId(IdUtil.simpleUUID());
            // 更新通讯录
            addressBook.add(deviceIncomingCall);
            object.setAddressBookList(addressBook);
            device.setParameter(object);
            iDeviceService.updateDevice(device);
        } else {
            return error("该设备不是牵挂卡或设备不存在");
        }
        // 生成请求
        SetIncomingCallReq setIncomingCallReq = new SetIncomingCallReq();
        List<SetIncomingCallReq.IncomingCallPhone> addPhones = new ArrayList<>();
        addPhones.add(new SetIncomingCallReq.IncomingCallPhone(req.getPhoneNumber(),req.getTimePeriods()));
        setIncomingCallReq.setAddPhones(addPhones);
        setIncomingCallReq.setDeviceNo(req.getDeviceNo());
        return setAddressBook(setIncomingCallReq);
    }

    @ApiOperation("更新通讯录")
    @PutMapping("/updateAddressBook")
    public AjaxResult updateAddressBook(@RequestBody @Validated AddOrUpdateAddressBookReq req){
        // 入库
        TDevice device = iDeviceService.selectByDeviceNo(req.getDeviceNo());
        if(Objects.isNull(device)){
            return error("数据库中不存在此数据。");
        }
        if(DEVICE_TYPE.equals(device.getType())) {
            // 获取设备参数
            TDevice.WatchSettings object = getParameter(device);
            // 获取通讯录
            List<DeviceIncomingCall> addressBook = object.addressBookList;
            // 如果通讯录为空
            if (Objects.isNull(addressBook)) {
                addressBook = new ArrayList<>();
            }
            // 限制通讯录长度
            if(addressBook.size()>20){
                return error("通讯录最多添加20个白名单用户。");
            }
            if(addressBook.stream().anyMatch(address -> address.getPhoneNumber().equals(req.getPhoneName()))){
                return error("通讯录中已存在相同号码。");
            }
            DeviceIncomingCall deviceIncomingCall = new DeviceIncomingCall();
            BeanUtils.copyProperties(req,deviceIncomingCall);
            // 流操作获取旧数据
            DeviceIncomingCall oldDeviceIncomingCall = addressBook.stream().filter(address -> address.getAddressBookId().equals(req.getAddressBookId())).findAny().orElse(null);
            int index = addressBook.indexOf(oldDeviceIncomingCall);
            // 更新通讯录
            if(Objects.isNull(oldDeviceIncomingCall)){
                addressBook.add(deviceIncomingCall);
            }else if(!oldDeviceIncomingCall.getPhoneNumber().equals(deviceIncomingCall.getPhoneNumber())) {
                SetIncomingCallReq setIncomingCallReq = new SetIncomingCallReq();
                List<String> deleteNumbers = new ArrayList<>();
                deleteNumbers.add(req.getPhoneNumber());
                setIncomingCallReq.setDeleteNumbers(deleteNumbers);
                setIncomingCallReq.setDeviceNo(req.getDeviceNo());
                setAddressBook(setIncomingCallReq);
                addressBook.add(index,deviceIncomingCall);
                addressBook.remove(oldDeviceIncomingCall);
            }else{
                addressBook.add(index,deviceIncomingCall);
                addressBook.remove(oldDeviceIncomingCall);
            }
            // 更新通讯录
            object.setAddressBookList(addressBook);
            device.setParameter(object);
            iDeviceService.updateDevice(device);
        } else {
            return error("该设备不是牵挂卡或设备不存在");
        }
        // 生成请求
        SetIncomingCallReq setIncomingCallReq = new SetIncomingCallReq();
        List<SetIncomingCallReq.IncomingCallPhone> addPhones = new ArrayList<>();
        addPhones.add(new SetIncomingCallReq.IncomingCallPhone(req.getPhoneNumber(),req.getTimePeriods()));
        setIncomingCallReq.setAddPhones(addPhones);
        setIncomingCallReq.setDeviceNo(req.getDeviceNo());
        return setAddressBook(setIncomingCallReq);
    }

    @ApiOperation("删除通讯录")
    @DeleteMapping("/deleteAddressBook")
    public AjaxResult deleteAddressBook(@RequestBody @Validated DeleteAddressBookReq req){
        // 入库
        TDevice device = iDeviceService.selectByDeviceNo(req.getDeviceNo());
        if(Objects.isNull(device)){
            return error("数据库中不存在此数据。");
        }
        if(DEVICE_TYPE.equals(device.getType())){
            // 获取设备参数
            TDevice.WatchSettings object = getParameter(device);
            // 获取通讯录
            List<DeviceIncomingCall> addressBook = object.addressBookList;
            // 流操作
            DeviceIncomingCall oldDeviceIncomingCall = addressBook.stream().filter(address -> address.getAddressBookId().equals(req.getAddressBookId())).findAny().orElse(null);
            if(Objects.isNull(oldDeviceIncomingCall)){
                return error("记录不存在");
            }else {
                addressBook.remove(oldDeviceIncomingCall);
                // 更新通讯录
                object.setAddressBookList(addressBook);
                device.setParameter(object);
                iDeviceService.updateDevice(device);
            }
        } else {
            return error("该设备不是牵挂卡或设备不存在");
        }
        SetIncomingCallReq setIncomingCallReq = new SetIncomingCallReq();
        List<String> deleteNumbers = new ArrayList<>();
        deleteNumbers.add(req.getPhoneNumber());
        setIncomingCallReq.setDeviceNo(req.getDeviceNo());
        setIncomingCallReq.setDeleteNumbers(deleteNumbers);
        return setAddressBook(setIncomingCallReq);
    }

    @ApiOperation("设置通讯录")
    @PostMapping("/setAddressBook")
    public AjaxResult setAddressBook(@RequestBody SetIncomingCallReq req){
        try{
            // 拼接url
            String url = DEVICE_CARE_CARD_URL + "/incoming/call";
            // http请求
//            return httpRequest(url,req);
            return success();
        }catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
    }


    @ApiOperation("设置亲情号码")
    @PostMapping("/setFamilyNumber")
    public AjaxResult setFamilyNumber(@RequestBody SetDeviceButtonReq req) {
        try{
            // 拼接url
            String url = DEVICE_CARE_CARD_URL + "/setButton";
            // 入庫
            TDevice device = iDeviceService.selectByDeviceNo(req.getDeviceNo());
            if(DEVICE_TYPE.equals(device.getType())){
                // 获取设备参数
                TDevice.WatchSettings object = getParameter(device);
                // 获取亲情号码
                Map<String, DevicePhone> familyNumber =  new HashMap<>(16);
                // 设置亲情号码
                req.getButtonFroms().forEach(
                        item->{
                            DevicePhone devicePhone = new DevicePhone(item.getButton(),item.getPhoneName(),item.getPhone());
                            familyNumber.put(item.getButton(),devicePhone);
                        }
                );
                object.setMapSet(familyNumber);
                device.setParameter(object);
                iDeviceService.updateDevice(device);
            }
            // http请求
//            return httpRequest(url,req);
            return success();
        }catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("获取亲情号码")
    @GetMapping("/getFamilyNumber")
    public AjaxResult getFamilyNumber(GetDeviceButtonReq req){
        Map<String, DevicePhone> familyNumber = new HashMap<>(16);
        List<DevicePhone> familyNumberList = new ArrayList<>();
        try {
            TDevice device = iDeviceService.selectByDeviceNo(req.getDeviceNo());
            if (Objects.isNull(device)) {
                return error("数据库中不存在此数据。");
            }
            if (DEVICE_TYPE.equals(device.getType())) {
                // 获取设备参数
                TDevice.WatchSettings object = getParameter(device);
                if (Objects.isNull(object)) {
                    device.setParameter(new TDevice.DeviceParameter());
                } else {
                    familyNumber = object.getMapSet();
                }
                for(int i=0;i<=3;i++){
                    String number = String.valueOf(i);
                    if(Objects.isNull(familyNumber.get(number))){
                        familyNumber.put(number,new DevicePhone(number));
                    }
                    familyNumberList.add(familyNumber.get(number));
                }
            }
        }catch (Exception e){
            return error(e.getMessage());
        }
        return success(familyNumberList);
    }

    @ApiOperation("设置电子围栏")
    @PostMapping("/setDeviceFence")
    public AjaxResult setDeviceFence(@RequestBody @Validated TDeviceFence tDeviceFence) {
        try{
            // 入库
            commonDeviceFenceService.insertTDeviceFence(tDeviceFence);
        }catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success();
    }

    @ApiOperation("设置位置守护")
    @PostMapping("/setLocationGuard")
    public AjaxResult setLocationGuard(@RequestBody @Validated LocationJob locationJob) {
        try{
            //获取设备id,存在则修改原记录
            TDevice device = iDeviceService.selectByDeviceNo(locationJob.getDeviceNo());
            if (Objects.isNull(device)) {
                return AjaxResult.error("数据库中不存在此设备。");
            }
            if(DEVICE_TYPE.equals(device.getType())){
                // 获取设备参数
                TDevice.WatchSettings object = getParameter(device);
                // 获取位置守护信息
                List<LocationJob> locationJobs = object.getLocationJobs();
                // 如果位置守护信息为空
                if(Objects.isNull(locationJobs)){
                    locationJobs = new ArrayList<>();
                }
                // 如果任务的uuid不为空
                if( !Objects.isNull(locationJob.getUuid())){
                    LocationJob oldLocationJob = locationJobs.stream().filter(job -> job.getUuid().equals(locationJob.getUuid())).findAny().orElse(null);
                   if(!Objects.isNull(oldLocationJob)) {
                       // 被任务的index
                       int index = locationJobs.indexOf(oldLocationJob);
                       // 新建地点列表
                       List<LocationJob.place> places = oldLocationJob.getPlaces();
                       // 待删除的地点下标
                       List<LocationJob.place> delPlaceIndexs = new ArrayList<>();
                       // 已被更新过的地点下标
                       List<LocationJob.place> updatedPlaceIndexs = new ArrayList<>();
                       // 本次更新需要新建的地点
                       List<LocationJob.place> collect = locationJob.getPlaces().stream().filter(place -> Objects.isNull(place.getGeoFenceId())).collect(Collectors.toList());
                       locationJob.getPlaces().removeAll(collect);
                       // 删除与更新
                       for(int i=0;i<places.size();i++){
                           // 寻找高德围栏ID相同的地点
                           LocationJob.place dataPlace = places.get(i);
                           LocationJob.place reqPlace = locationJob.getPlaces().stream().filter(place -> {
                               return place.getGeoFenceId().equals(dataPlace.getGeoFenceId());
                           }).findAny().orElse(null);
                           if(Objects.isNull(reqPlace)){
                               // 如果没有相同高德ID地点，则需要删除
                               delPlaceIndexs.add(dataPlace);
                               // TODO:调用高德删除API

                           } else {
                               // 拷贝更新属性
                               oldLocationJob.getPlaces().set(i,reqPlace);
                               // 更新过的地址可以做标注
                               updatedPlaceIndexs.add(reqPlace);
                           }
                       }
                       // 删除应被删除的地址
                       delPlaceIndexs.forEach(del->oldLocationJob.getPlaces().remove(del));
                       // 删除已被更新过的地址
                       updatedPlaceIndexs.forEach(updated->locationJob.getPlaces().remove(updated));
                       // 更新数据
                       locationJobs.remove(index);
                       // 新建地点
                       oldLocationJob.getPlaces().addAll(collect);
                       // 调用高德ID地点
                       if(index!=locationJobs.size()) {
                           locationJobs.set(index, oldLocationJob);
                       }else{
                           locationJobs.add(oldLocationJob);
                       }

                   }
                }else{
                     // 给新建任务一个uuid
                    locationJob.setUuid(IdUtil.simpleUUID());
                    locationJobs.add(locationJob);
                }
                // 调用高德的插入API
                locationJob.getPlaces().forEach(place -> {

                });
                object.setLocationJobs(locationJobs);
                device.setParameter(object);
                iDeviceService.updateDevice(device);
            }
            // 调用高德API
            // commonDeviceFenceService.insertTDeviceFence()
        }catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success();
    }

    @ApiOperation("设置时段禁用")
    @PostMapping("/setPeriodDisable")
    public AjaxResult setPeriodDisable(SetClassModelReq req) {
        try{
            // 拼接url
            String url = DEVICE_CARE_CARD_URL + "/setClassModel";
            // 入库
            TDevice device = iDeviceService.selectByDeviceNo(req.getDeviceNo());
//            if(DEVICE_TYPE.equals(device.getType())){
//                // 获取设备参数
//                TDevice.WatchSettings object = getParameter(device);
//                // 获取禁用时段
//                List<ClassTimePeriod> classTimePeriods = object.getClassTimePeriods();
//                // 如果通讯录为空
//                if(Objects.isNull(classTimePeriods)){
//                    classTimePeriods = new ArrayList<>();
//                }
//
//                // 删除应被删除的号码
//                req.getDeleteNumbers().forEach(
//                        finalAddressBook::remove
//                );
//                // 新增号码
//                req.getAddPhones().forEach(
//                        item->{
//                            DeviceIncomingCall deviceIncomingCall = new DeviceIncomingCall(item.getPhone(),item.getTimePeriods(),req.getPeriod());
//                            finalAddressBook.put(item.getPhone(), deviceIncomingCall);
//                        }
//                );
//
//                // 更新通讯录
//                object.setAddressBookMap(finalAddressBook);
//                device.setParameter(object);
//                iDeviceService.updateDevice(device);
//            }
            // http请求发送
//          return  httpRequest(url,req);
            return success();
        }catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
//        return AjaxResult.success();
    }

    @ApiOperation("重启设备")
    @PostMapping("/rebootDevice")
    public AjaxResult rebootDevice(@RequestBody SetOperateTerminalReq req){
        try{
            // 拼接url
            String url = DEVICE_CARE_CARD_URL + "/setOperateTerminal";
            // http请求
//            return httpRequest(url,req);
            return success();
        }catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * http请求
     * @param url
     * @param req
     * @return
     */
   protected AjaxResult httpRequest(String url, Object req){
       JSONObject httpMsg = JSONObject.parseObject(HttpClientUtil.sendPost(url,req,""));
       if(REQUEST_SUCCESS_STATUS.equals(httpMsg.get(REQUEST_STATUS))){
           return success(httpMsg);
       }else{
           return error(httpMsg.toJSONString());
       }
   }

    /**
     * 获取设备参数
     * @param device
     * @return
     */
   protected TDevice.WatchSettings getParameter(TDevice device){
       TDevice.WatchSettings paramete = (TDevice.WatchSettings) device.getParameter();
       if(Objects.isNull(paramete)){
           paramete = new TDevice.WatchSettings();
       }
       return paramete;
   }
}
