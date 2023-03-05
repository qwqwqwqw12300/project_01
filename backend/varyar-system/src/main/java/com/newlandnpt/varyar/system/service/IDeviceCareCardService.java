package com.newlandnpt.varyar.system.service;

import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.entity.ClassTimePeriod;
import com.newlandnpt.varyar.common.core.domain.entity.LocationJob;
import com.newlandnpt.varyar.common.utils.tcp.req.*;
import com.newlandnpt.varyar.system.domain.TDeviceFence;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

public interface IDeviceCareCardService {

    /**
     * 批量插入或更新通讯录
     * @param req
     * @return
     */
    public AjaxResult batchInsertOrUpdateAddressBook(@RequestBody BatchAddOrUpdateAddressBookReq req);

    /**
     * 获取设备通讯录
     * @param deviceNo
     * @return
     */
    public AjaxResult getAddressBook(String deviceNo);

    /**
     * 删除通讯录
     * @param req
     * @return
     */
    public AjaxResult deleteAddressBook(@RequestBody @Validated DeleteAddressBookReq req);

    /**
     * 设置通讯录
     * @param req
     * @return
     */
    public AjaxResult setAddressBook(@RequestBody SetIncomingCallReq req);

    /**
     * 设置亲情号
     * @param req
     * @return
     */
    public AjaxResult setFamilyNumber(@RequestBody SetDeviceButtonReq req);

    /**
     * 获取亲情号
     * @param req
     * @return
     */
    public AjaxResult getFamilyNumber(GetDeviceButtonReq req);


    public AjaxResult setDeviceFence(@RequestBody @Validated TDeviceFence tDeviceFence);

    /**
     * 设置位置守护
     * @param locationJob
     * @return
     */
    public AjaxResult setLocationGuard(@RequestBody @Validated LocationJob locationJob) throws Exception;

    /**
     * 获取位置守护信息
     * @param deviceNo
     * @param uuid
     * @return
     */
    public AjaxResult getLocationGuard(String deviceNo, String uuid);

    /**
     * 获取位置守护列表
     * @param deviceNo
     * @return
     */
    public AjaxResult getLocationGuardList(String deviceNo);

    /**
     * 删除位置守护
     * @return
     */
    public AjaxResult deleteLocationGuard(@RequestBody DeleteLocationGuardReq req);

    /**
     * 设置时段禁用
     * @param req
     * @return
     */
    public AjaxResult setPeriodDisable(@RequestBody ClassTimePeriod req);

    /**
     * 删除时段禁用
     * @param req
     * @return
     */
    public AjaxResult deletePeriodDisable(@RequestBody DeletePeriodDisabledReq req);

    /**
     * 获取时段禁用
     * @param deviceNo
     * @param uuid
     * @return
     */
    public AjaxResult getPeriodDisable(String deviceNo, String uuid);

    /**
     * 获取时段禁用列表
     * @param deviceNo
     * @return
     */
    public AjaxResult getPeriodDisableList(String deviceNo);
}
