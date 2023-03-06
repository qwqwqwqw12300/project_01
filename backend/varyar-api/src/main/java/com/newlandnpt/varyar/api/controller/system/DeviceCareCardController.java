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
import com.newlandnpt.varyar.system.service.IDeviceCareCardService;
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
    @Autowired
    private IDeviceCareCardService iDeviceCareCardService;
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
            return httpRequest(url,req);
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
        addPhones.add(new SetIncomingCallReq.IncomingCallPhone(req.getPhoneNumber()));
        setIncomingCallReq.setAddPhones(addPhones);
        setIncomingCallReq.setDeviceNo(req.getDeviceNo());
        return setAddressBook(setIncomingCallReq);
    }

    @ApiOperation("更新通讯录")
    @PostMapping("/updateAddressBook")
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
        addPhones.add(new SetIncomingCallReq.IncomingCallPhone(req.getPhoneNumber()));
        setIncomingCallReq.setAddPhones(addPhones);
        setIncomingCallReq.setDeviceNo(req.getDeviceNo());
        return setAddressBook(setIncomingCallReq);
    }

    @ApiOperation("批量新增或修改通讯录")
    @PostMapping("/addOrUpdateAddressBook")
    public AjaxResult batchInsertOrUpdateAddressBook(@RequestBody @Validated BatchAddOrUpdateAddressBookReq req){
        try{
            return iDeviceCareCardService.batchInsertOrUpdateAddressBook(req);
        }catch (Exception e){
            return error(e.getMessage());
        }
    }

    @ApiOperation("获取通讯录")
    @GetMapping("/getAddressBook")
    public AjaxResult getAddressBook(String deviceNo){
        return iDeviceCareCardService.getAddressBook(deviceNo);
    }

    @ApiOperation("删除通讯录")
    @PostMapping("/deleteAddressBook")
    public AjaxResult deleteAddressBook(@RequestBody @Validated DeleteAddressBookReq req){
        return iDeviceCareCardService.deleteAddressBook(req);
    }

    @ApiOperation("设置通讯录")
    @PostMapping("/setAddressBook")
    public AjaxResult setAddressBook(@RequestBody SetIncomingCallReq req){
        try{
            // 拼接url
            String url = DEVICE_CARE_CARD_URL + "/incoming/call";
            // http请求
            return httpRequest(url,req);
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
            iDeviceCareCardService.setFamilyNumber(req);
            // http请求
            return httpRequest(url,req);
        }catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("获取亲情号码")
    @GetMapping("/getFamilyNumber")
    public AjaxResult getFamilyNumber(GetDeviceButtonReq req){
        return iDeviceCareCardService.getFamilyNumber(req);
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
          iDeviceCareCardService.setLocationGuard(locationJob);
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
          return  httpRequest(url,req);
        }catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("重启设备")
    @PostMapping("/rebootDevice")
    public AjaxResult rebootDevice(@RequestBody SetOperateTerminalReq req){
        try{
            // 拼接url
            String url = DEVICE_CARE_CARD_URL + "/setOperateTerminal";
            // http请求
            return httpRequest(url,req);
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
