package com.newlandnpt.varyar.tcp.service.impl;

import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.tcp.dispose.ChannelMessageDisposer;
import com.newlandnpt.varyar.tcp.dispose.incoming.call.SetIncomingCallReq;
import com.newlandnpt.varyar.tcp.dispose.incoming.call.SetIncomingCallResponse;
import com.newlandnpt.varyar.tcp.service.IDevicePhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备电话服务实现类
 * @author lin.ju
 * @date 2023/2/23
 */
@Service
public class DevicePhoneServiceImpl implements IDevicePhoneService {

    @Autowired
    private ChannelMessageDisposer channelMessageDisposer;

    @Override
    public SetIncomingCallResponse setIncomingCall(SetIncomingCallReq req) {
        return channelMessageDisposer.dispose(ApiTypes.SET_INCOMING_CALL, req, SetIncomingCallResponse::new);
    }
}
