package com.newlandnpt.varyar.tcp.service.impl;

import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.tcp.dispose.ChannelMessageDisposer;
import com.newlandnpt.varyar.tcp.dispose.protect.RequestCallReq;
import com.newlandnpt.varyar.tcp.dispose.protect.RequestCallResponse;
import com.newlandnpt.varyar.tcp.service.IDeviceProtectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备安全防护实现
 * @author ljx
 * @date 2023/2/28
 */
@Service
public class DeviceProtectServiceImpl implements IDeviceProtectService {
    @Autowired
    private ChannelMessageDisposer channelMessageDisposer;

    @Override
    public RequestCallResponse requestCallResponse(RequestCallReq req) {
        return channelMessageDisposer.dispose(ApiTypes.REQUEST_CALL, req, RequestCallResponse::new);
    }
}
