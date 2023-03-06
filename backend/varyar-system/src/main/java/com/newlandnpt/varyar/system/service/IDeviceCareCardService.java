package com.newlandnpt.varyar.system.service;

import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.entity.LocationJob;
import com.newlandnpt.varyar.common.utils.tcp.req.*;
import com.newlandnpt.varyar.system.domain.TDeviceFence;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

public interface IDeviceCareCardService {
    public AjaxResult batchInsertOrUpdateAddressBook(@RequestBody BatchAddOrUpdateAddressBookReq req);

    public AjaxResult getAddressBook(String deviceNo);

    public AjaxResult deleteAddressBook(@RequestBody @Validated DeleteAddressBookReq req);

    public AjaxResult setAddressBook(@RequestBody SetIncomingCallReq req);

    public AjaxResult setFamilyNumber(@RequestBody SetDeviceButtonReq req);

    public AjaxResult getFamilyNumber(GetDeviceButtonReq req);

    public AjaxResult setDeviceFence(@RequestBody @Validated TDeviceFence tDeviceFence);

    public AjaxResult setLocationGuard(@RequestBody @Validated LocationJob locationJob);

}
