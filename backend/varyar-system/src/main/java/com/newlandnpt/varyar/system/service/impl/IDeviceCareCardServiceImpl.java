package com.newlandnpt.varyar.system.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.entity.ClassTimePeriod;
import com.newlandnpt.varyar.common.core.domain.entity.DeviceIncomingCall;
import com.newlandnpt.varyar.common.core.domain.entity.DevicePhone;
import com.newlandnpt.varyar.common.core.domain.entity.LocationJob;
import com.newlandnpt.varyar.common.enums.BusinessStatus;
import com.newlandnpt.varyar.common.enums.ExCodeEnum;
import com.newlandnpt.varyar.common.exception.business.BusinessException;
import com.newlandnpt.varyar.common.utils.HttpClientUtil;
import com.newlandnpt.varyar.common.utils.tcp.req.*;
import com.newlandnpt.varyar.system.domain.LocationGuard;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.domain.TDeviceFence;
import com.newlandnpt.varyar.system.domain.req.DelLocationGuardReq;
import com.newlandnpt.varyar.system.domain.req.DeleteFenceReq;
import com.newlandnpt.varyar.system.service.IDeviceCareCardService;
import com.newlandnpt.varyar.system.service.IDeviceService;
import io.swagger.models.auth.In;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IDeviceCareCardServiceImpl implements IDeviceCareCardService {

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


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult batchInsertOrUpdateAddressBook(BatchAddOrUpdateAddressBookReq req) {
            // 入库
            TDevice device = iDeviceService.selectByDeviceNo(req.getDeviceNo());
            if(Objects.isNull(device)){
                throw new BusinessException(ExCodeEnum.DB_NOT_DEVICE);
            }
            if(DEVICE_TYPE.equals(device.getType())){
                try {
                    // 获取设备参数
                    TDevice.WatchSettings object = getParameter(device);
                    // 获取通讯录
                    List<DeviceIncomingCall> addressBook = object.addressBookList;
                    // 如果通讯录为空
                    if (Objects.isNull(addressBook)) {
                        addressBook = new ArrayList<>();
                    }
                    // 需修改的通讯录
                    List<DeviceIncomingCall> waitUpdatedAddress = req.getAddressBooks().stream().filter(address -> !Objects.isNull(address.getAddressBookId())).collect(Collectors.toList());
                    // 需新增的通讯录
                    List<DeviceIncomingCall> waitInsertAddress = req.getAddressBooks().stream().filter(address -> Objects.isNull(address.getAddressBookId())).collect(Collectors.toList());
                    // 限制通讯录长度
                    if (waitInsertAddress.size() + addressBook.size() > 20) {
                        throw new BusinessException(ExCodeEnum.ADDRESS_NUMBER_LIMIT);
                    }
                    // 修改通讯录
                    List<DeviceIncomingCall> finalAddressBook = addressBook;
                    waitUpdatedAddress.forEach(
                            updateAddress -> {
                                // 流操作获取旧数据
                                DeviceIncomingCall oldDeviceIncomingCall =
                                        finalAddressBook.stream().filter(address -> address.getAddressBookId().equals(updateAddress.getAddressBookId())).findAny().orElse(null);
                                int index = finalAddressBook.indexOf(oldDeviceIncomingCall);
                                // 更新通讯录
                                if (Objects.isNull(oldDeviceIncomingCall)) {
                                    finalAddressBook.add(updateAddress);
                                } else if (!oldDeviceIncomingCall.getPhoneNumber().equals(updateAddress.getPhoneNumber())) {
                                    SetIncomingCallReq setIncomingCallReq = new SetIncomingCallReq();
                                    List<String> deleteNumbers = new ArrayList<>();
                                    deleteNumbers.add(updateAddress.getPhoneNumber());
                                    setIncomingCallReq.setDeleteNumbers(deleteNumbers);
                                    setIncomingCallReq.setDeviceNo(req.getDeviceNo());
                                    setAddressBook(setIncomingCallReq);
                                    finalAddressBook.add(index, updateAddress);
                                    finalAddressBook.remove(oldDeviceIncomingCall);
                                } else {
                                    finalAddressBook.add(index, updateAddress);
                                    finalAddressBook.remove(oldDeviceIncomingCall);
                                }
                            }
                    );
                    // 给新增通讯录设置UUID
                    waitInsertAddress.forEach(address -> address.setAddressBookId(IdUtil.simpleUUID()));
                    // 新增通讯录
                    finalAddressBook.addAll(waitInsertAddress);
                    // 更新通讯录
                    object.setAddressBookList(addressBook);
                    device.setParameter(object);
                    iDeviceService.updateDevice(device);
                    // 生成请求
                    SetIncomingCallReq setIncomingCallReq = new SetIncomingCallReq();
                    List<SetIncomingCallReq.IncomingCallPhone> addPhones = new ArrayList<>();
                    waitInsertAddress.forEach(phone -> {
                        addPhones.add(new SetIncomingCallReq.IncomingCallPhone(phone.getPhoneNumber()));
                    });
                    setIncomingCallReq.setAddPhones(addPhones);
                    setIncomingCallReq.setDeviceNo(req.getDeviceNo());
                    return setAddressBook(setIncomingCallReq);
                }catch (Exception e){
                    throw new BusinessException(ExCodeEnum.ADDRESS_SET_FAIL);
                }
            }else{
                throw new BusinessException(ExCodeEnum.DEVICE_NOT_CARE_CARD);
            }
    }

    @Override
    public AjaxResult getAddressBook(String deviceNo) {
        try{
            TDevice device = iDeviceService.selectByDeviceNo(deviceNo);
            if(Objects.isNull(device)){
                throw new BusinessException(ExCodeEnum.DB_NOT_DEVICE);
            }
            if(DEVICE_TYPE.equals(device.getType())){
                // 获取设备参数
                TDevice.WatchSettings object = getParameter(device);
                // 获取通讯录
                List<DeviceIncomingCall> addressBook = object.addressBookList;
                // 如果通讯录为空
                if (Objects.isNull(addressBook)) {
                    addressBook = new ArrayList<>();
                }
                return AjaxResult.success(addressBook);
            }
            else {
                throw new BusinessException(ExCodeEnum.DEVICE_NOT_CARE_CARD);
            }
        }catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deleteAddressBook(DeleteAddressBookReq req) {
        // 入库
        TDevice device = iDeviceService.selectByDeviceNo(req.getDeviceNo());
        if(Objects.isNull(device)){
            throw new BusinessException(ExCodeEnum.DB_NOT_DEVICE);
        }
        if(DEVICE_TYPE.equals(device.getType())){
            // 获取设备参数
            TDevice.WatchSettings object = getParameter(device);
            // 获取通讯录
            List<DeviceIncomingCall> addressBook = object.addressBookList;
            // 流操作
            DeviceIncomingCall oldDeviceIncomingCall = addressBook.stream().filter(address -> address.getAddressBookId().equals(req.getAddressBookId())).findAny().orElse(null);
            if(Objects.isNull(oldDeviceIncomingCall)){
                return AjaxResult.error("记录不存在");
            }else {
                addressBook.remove(oldDeviceIncomingCall);
                // 更新通讯录
                object.setAddressBookList(addressBook);
                device.setParameter(object);
                iDeviceService.updateDevice(device);
            }
        } else {
            throw new BusinessException(ExCodeEnum.DEVICE_NOT_CARE_CARD);
        }
        SetIncomingCallReq setIncomingCallReq = new SetIncomingCallReq();
        List<String> deleteNumbers = new ArrayList<>();
        deleteNumbers.add(req.getPhoneNumber());
        setIncomingCallReq.setDeviceNo(req.getDeviceNo());
        setIncomingCallReq.setDeleteNumbers(deleteNumbers);
        return setAddressBook(setIncomingCallReq);
    }

    @Override
    public AjaxResult setAddressBook(SetIncomingCallReq req) {
            // 拼接url
            String url = DEVICE_CARE_CARD_URL + "/incoming/call";
            // http请求
            return AjaxResult.success(httpRequest(url,req));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult setFamilyNumber(SetDeviceButtonReq req) {
        try{
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
            }else {
                throw new BusinessException(ExCodeEnum.DEVICE_NOT_CARE_CARD);
            }
        }catch (Exception e){
            throw new BusinessException(ExCodeEnum.FAMILY_NUMBER_SET_FAIL);
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult getFamilyNumber(GetDeviceButtonReq req) {
        Map<String, DevicePhone> familyNumber = new HashMap<>(16);
        List<DevicePhone> familyNumberList = new ArrayList<>();
        try {
            TDevice device = iDeviceService.selectByDeviceNo(req.getDeviceNo());
            if (Objects.isNull(device)) {
                throw new BusinessException(ExCodeEnum.DB_NOT_DEVICE);
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
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success(familyNumberList);
    }

    @Override
    public AjaxResult setDeviceFence(TDeviceFence tDeviceFence) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult setLocationGuard(LocationJob locationJob) throws Exception {
        //获取设备id,存在则修改原记录
        TDevice device = iDeviceService.selectByDeviceNo(locationJob.getDeviceNo());
        if (Objects.isNull(device)) {
            throw new BusinessException(ExCodeEnum.DB_NOT_DEVICE);
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
                    // 已被持久化的地点列表
                    List<LocationJob.place> places = oldLocationJob.getPlaces();
                    // 待删除的地点下标
                    List<LocationJob.place> delPlaceIndexs = new ArrayList<>();
                    ArrayList<String> delList = new ArrayList<>();
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
                            delList.add(String.valueOf(dataPlace.getGeoFenceId()));
                        } else {
                            // 拷贝更新属性
                            oldLocationJob.getPlaces().set(i,reqPlace);
                            // 调用高德更新API
                            LocationGuard locationGuard = new LocationGuard();
                            BeanUtils.copyProperties(reqPlace,locationGuard);
                            commonLocationGuardService.updateLocationGuard(locationGuard);
                            // 更新过的地址可以做标注
                            updatedPlaceIndexs.add(reqPlace);
                        }
                    }
                    // 删除应被删除的地址
                    delPlaceIndexs.forEach(del->oldLocationJob.getPlaces().remove(del));
                    // 调用高德删除API
                    DelLocationGuardReq delLocationGuardReq = new DelLocationGuardReq();
                    delLocationGuardReq.setGeoLocationGuardId(delList);
                    commonLocationGuardService.deleteLocationGuard(delLocationGuardReq);
                    // 删除已被更新过的地址
                    updatedPlaceIndexs.forEach(updated->locationJob.getPlaces().remove(updated));
                    // 更新数据
                    locationJobs.remove(index);
                    // 新建地点
                    oldLocationJob.getPlaces().addAll(collect);
                    // 统一调用高德的插入API
                    collect.forEach(place -> {
                        LocationGuard locationGuard = new LocationGuard();
                        BeanUtils.copyProperties(place, locationGuard);
                        place.setGeoFenceId(commonLocationGuardService.insertLocationGuard(locationGuard).getGeoFenceId());
                    });
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
                // 统一调用高德的插入API
                locationJob.getPlaces().forEach(place -> {
                    LocationGuard locationGuard = new LocationGuard();
                    BeanUtils.copyProperties(place, locationGuard);
                    place.setGeoFenceId(commonLocationGuardService.insertLocationGuard(locationGuard).getGeoFenceId());
                });
                locationJobs.add(locationJob);
            }
            object.setLocationJobs(locationJobs);
            device.setParameter(object);
            iDeviceService.updateDevice(device);
        }
        else {
            throw new BusinessException(ExCodeEnum.DEVICE_NOT_CARE_CARD);
        }
        return AjaxResult.success();
    }


    @Override
    public AjaxResult getLocationGuard(String deviceNo, String uuid) {
        //获取设备id,存在则修改原记录
        TDevice device = iDeviceService.selectByDeviceNo(deviceNo);
        if (Objects.isNull(device)) {
            throw new BusinessException(ExCodeEnum.DB_NOT_DEVICE);
        }
        if(DEVICE_TYPE.equals(device.getType())){
            // 获取设备参数
            TDevice.WatchSettings object = getParameter(device);
            // 获取位置守护信息
            List<LocationJob> locationJobs = object.getLocationJobs();
            LocationJob locationJob = locationJobs.stream().filter(job -> job.getUuid().equals(uuid)).findFirst().orElse(new LocationJob());
            return AjaxResult.success(locationJob);
        }else{
            throw new BusinessException(ExCodeEnum.DEVICE_NOT_CARE_CARD);
        }
    }

    @Override
    public AjaxResult getLocationGuardList(String deviceNo) {
        //获取设备id,存在则修改原记录
        TDevice device = iDeviceService.selectByDeviceNo(deviceNo);
        if (Objects.isNull(device)) {
            throw new BusinessException(ExCodeEnum.DB_NOT_DEVICE);
        }
        if(DEVICE_TYPE.equals(device.getType())){
            // 获取设备参数
            TDevice.WatchSettings object = getParameter(device);
            // 获取位置守护信息
            List<LocationJob> locationJobs = object.getLocationJobs();
            if(Objects.isNull(locationJobs)){
                locationJobs = new ArrayList<LocationJob>();
            }
            return AjaxResult.success(locationJobs);
        }else{
            throw new BusinessException(ExCodeEnum.DEVICE_NOT_CARE_CARD);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deleteLocationGuard(DeleteLocationGuardReq req) {
        //获取设备id,存在则修改原记录
        TDevice device = iDeviceService.selectByDeviceNo(req.getDeviceNo());
        if (Objects.isNull(device)) {
            throw new BusinessException(ExCodeEnum.DB_NOT_DEVICE);
        }
        if(DEVICE_TYPE.equals(device.getType())) {
            // 获取设备参数
            TDevice.WatchSettings object = getParameter(device);
            // 获取位置守护信息
            List<LocationJob> locationJobs = object.getLocationJobs();
            // 如果位置守护信息为空
            if(Objects.isNull(locationJobs)){
                return AjaxResult.success();
            }
            req.getUuidList().forEach(uuid->{
                LocationJob oldLocationJob = locationJobs.stream().filter(job -> job.getUuid().equals(uuid)).findAny().orElse(null);
                if(!Objects.isNull(oldLocationJob)){
                    try {
                        List<LocationJob.place> places = oldLocationJob.getPlaces();
                        ArrayList<String> delList = new ArrayList<>();
                        for (int i = 0; i < places.size(); i++) {
                            delList.add(String.valueOf(places.get(i).getGeoFenceId()));
                        }
                        // 调用高德删除API
                        DelLocationGuardReq delLocationGuardReq = new DelLocationGuardReq();
                        delLocationGuardReq.setGeoLocationGuardId(delList);
                        commonLocationGuardService.deleteLocationGuard(delLocationGuardReq);
                        // 更新数据
                        locationJobs.remove(oldLocationJob);
                        object.setLocationJobs(locationJobs);
                        device.setParameter(object);
                        iDeviceService.updateDevice(device);
                    }catch (Exception e){
                        throw new BusinessException(ExCodeEnum.LOCATION_GUARD_DELETE_FAIL);
                    }
                }
            });
        }else{
            throw new BusinessException(ExCodeEnum.DEVICE_NOT_CARE_CARD);
        }
        return AjaxResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult setPeriodDisable(ClassTimePeriod req) {
        // 拼接url
        String url = DEVICE_CARE_CARD_URL + "/setClassModel";
        //获取设备id,存在则修改原记录
        TDevice device = iDeviceService.selectByDeviceNo(req.getDeviceNo());
        if (Objects.isNull(device)) {
            throw new BusinessException(ExCodeEnum.DB_NOT_DEVICE);
        }
        if(DEVICE_TYPE.equals(device.getType())){
            // 获取设备参数
            TDevice.WatchSettings object = getParameter(device);
            // 获取时段禁用信息
            List<ClassTimePeriod> classTimePeriods = object.getClassTimePeriods();
            // 如果时段禁用信息为空
            if(Objects.isNull(classTimePeriods)){
                classTimePeriods = new ArrayList<>();
            }
            // 如果请求的uuid不为空,则更新数据库
            if( !Objects.isNull(req.getUuid())){
                // 已被持久化的禁用时段
                ClassTimePeriod oldClassTimePeriod = classTimePeriods.stream().filter(period -> period.getUuid().equals(req.getUuid())).findAny().orElse(null);
                // 判断需要更新的数据是否存在
                if(!Objects.isNull(oldClassTimePeriod)){
                    // 待更新数据的下标
                    int index = classTimePeriods.indexOf(oldClassTimePeriod);
                    // 更新数据
                    classTimePeriods.remove(oldClassTimePeriod);
                    if(index!=classTimePeriods.size()) {
                        classTimePeriods.set(index, req);
                    }else{
                        classTimePeriods.add(req);
                    }
                }
            }
            else{
                if(classTimePeriods.size()>=10){
                    throw new BusinessException(ExCodeEnum.EXCEED_PERIOD_DISABLE_NUMBER_LIMIT);
                }
                // 给新的禁用时段一个uuid
                req.setUuid(IdUtil.simpleUUID());
                classTimePeriods.add(req);
            }
            // 平台下发数据并更新数据库
            try{
                List<SetClassModelReq.timePeriod> timePeriods = new ArrayList<>();
                for(int i=1;i<=classTimePeriods.size();i++){
                    ClassTimePeriod dataClassTimePeriod = classTimePeriods.get(i - 1);
                    String time = i +"="+dataClassTimePeriod.getBeginTime()+"-"+dataClassTimePeriod.getEndTime();
                    String period = dataClassTimePeriod.getPeriod();
                    timePeriods.add(new SetClassModelReq.timePeriod(time,period));
                }
                object.setClassTimePeriods(classTimePeriods);
                device.setParameter(object);
                iDeviceService.updateDevice(device);
                SetClassModelReq setClassModelReq = new SetClassModelReq(timePeriods);
                setClassModelReq.setDeviceNo(req.getDeviceNo());
                return AjaxResult.success(httpRequest(url,setClassModelReq));
            }catch (BusinessException bs){
                throw new BusinessException(bs.getMsg());
            }
            catch (Exception e){
                throw new BusinessException(ExCodeEnum.SET_PERIOD_DISABLE_FAIL);
            }
        }else {
            throw new BusinessException(ExCodeEnum.DEVICE_NOT_CARE_CARD);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deletePeriodDisable(DeletePeriodDisabledReq req) {
        // 拼接url
        String url = DEVICE_CARE_CARD_URL + "/setClassModel";
        TDevice device = iDeviceService.selectByDeviceNo(req.getDeviceNo());
        if(Objects.isNull(device)){
             throw new BusinessException(ExCodeEnum.DB_NOT_DEVICE);
        }
        if(DEVICE_TYPE.equals(device.getType())){
            // 获取设备参数
            TDevice.WatchSettings object = getParameter(device);
            // 获取禁用时间
            List<ClassTimePeriod> classTimePeriods = object.getClassTimePeriods();
            // 循环进行删除操作
            for (String uuid : req.getUuidList()) {
                // 流操作
                ClassTimePeriod oldClassTimePeriod = classTimePeriods.stream().filter(period -> period.getUuid().equals(uuid)).findAny().orElse(null);
                if(Objects.isNull(oldClassTimePeriod)){
                    return AjaxResult.error("记录不存在");
                }else {
                    classTimePeriods.remove(oldClassTimePeriod);
                }
            }
            // 平台下发数据并更新数据库
            try{
                List<SetClassModelReq.timePeriod> timePeriods = new ArrayList<>();
                for(int i=1;i<=classTimePeriods.size();i++){
                    ClassTimePeriod dataClassTimePeriod = classTimePeriods.get(i - 1);
                    String time = i +"="+dataClassTimePeriod.getBeginTime()+"-"+dataClassTimePeriod.getEndTime();
                    String period = dataClassTimePeriod.getPeriod();
                    timePeriods.add(new SetClassModelReq.timePeriod(time,period));
                }
                object.setClassTimePeriods(classTimePeriods);
                device.setParameter(object);
                iDeviceService.updateDevice(device);
                SetClassModelReq setClassModelReq = new SetClassModelReq(timePeriods);
                setClassModelReq.setDeviceNo(req.getDeviceNo());
                return AjaxResult.success(httpRequest(url,setClassModelReq));
            }catch (BusinessException bs){
                throw new BusinessException(bs.getMsg());
            }
            catch (Exception e){
                throw new BusinessException(ExCodeEnum.SET_PERIOD_DISABLE_FAIL);
            }
        }else{
            throw new BusinessException(ExCodeEnum.DEVICE_NOT_CARE_CARD);
        }
    }

    @Override
    public AjaxResult getPeriodDisable(String deviceNo, String uuid) {
        try{
            TDevice device = iDeviceService.selectByDeviceNo(deviceNo);
            if (Objects.isNull(device)) {
                throw new BusinessException(ExCodeEnum.DB_NOT_DEVICE);
            }
            if(DEVICE_TYPE.equals(device.getType())){
                // 获取设备参数
                TDevice.WatchSettings object = getParameter(device);
                // 获取禁用时段
                List<ClassTimePeriod> classTimePeriods = object.getClassTimePeriods();
                if(CollectionUtils.isEmpty(classTimePeriods)){
                    classTimePeriods = new ArrayList<>();
                }
                ClassTimePeriod classTimePeriod = classTimePeriods.stream().filter(job -> job.getUuid().equals(uuid)).findFirst().orElse(new ClassTimePeriod());
                return AjaxResult.success(classTimePeriod);
            }else{
                throw new BusinessException(ExCodeEnum.DEVICE_NOT_CARE_CARD);
            }
        }catch (Exception e){
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public AjaxResult getPeriodDisableList(String deviceNo) {
        try{
            TDevice device = iDeviceService.selectByDeviceNo(deviceNo);
            if(Objects.isNull(device)){
                throw new BusinessException(ExCodeEnum.DB_NOT_DEVICE);
            }
            if(DEVICE_TYPE.equals(device.getType())){
                // 获取设备参数
                TDevice.WatchSettings object = getParameter(device);
                // 获取禁用时段
                List<ClassTimePeriod> classTimePeriods = object.getClassTimePeriods();
                // 如果通讯录为空
                if (Objects.isNull(classTimePeriods)) {
                    classTimePeriods = new ArrayList<>();
                }
                return AjaxResult.success(classTimePeriods);
            }
            else {
                throw new BusinessException(ExCodeEnum.DEVICE_NOT_CARE_CARD);
            }
        }catch (Exception e){
            throw new BusinessException(e.getMessage());
        }
    }

    protected JSONObject httpRequest(String url, Object req){
        JSONObject httpMsg = new JSONObject();
        try {
            httpMsg = JSONObject.parseObject(HttpClientUtil.sendPost(url, req, ""));
        }catch (Exception e){
            throw new BusinessException(e.getMessage());
        }
        if(REQUEST_SUCCESS_STATUS.equals(httpMsg.get(REQUEST_STATUS))){
            return httpMsg;
        }else{
            throw new BusinessException(ExCodeEnum.TCP_SET_FAIL);
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
