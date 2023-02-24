package com.newlandnpt.varyar.tcp.service;

import com.newlandnpt.varyar.tcp.dispose.button.SetDeviceButtonReq;
import com.newlandnpt.varyar.tcp.dispose.button.SetDeviceButtonResponse;

/**
 * 设备设置按键服务接口
 * @author chenxw1
 * @date 2023/2/23
 */
public interface IDeviceButtonService {


    /**
     * 设置按键号码
     * @param req
     * @return
     */
    SetDeviceButtonResponse setDeviceButtonResponse(SetDeviceButtonReq req);

}
