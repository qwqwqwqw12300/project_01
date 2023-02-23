package com.newlandnpt.varyar.tcp.service;

import com.newlandnpt.varyar.tcp.dispose.incoming.call.SetIncomingCallReq;
import com.newlandnpt.varyar.tcp.dispose.incoming.call.SetIncomingCallResponse;

/**
 * 设备电话服务接口
 * @author lin.ju
 * @date 2023/2/23
 */
public interface IDevicePhoneService {


    /**
     * 设置呼入号码
     * @param req
     * @return
     */
    SetIncomingCallResponse setIncomingCall(SetIncomingCallReq req);

}
