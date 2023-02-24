package com.newlandnpt.varyar.tcp.service.impl;

import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.tcp.dispose.ChannelMessageDisposer;
import com.newlandnpt.varyar.tcp.dispose.button.SetDeviceButtonReq;
import com.newlandnpt.varyar.tcp.dispose.button.SetDeviceButtonResponse;
import com.newlandnpt.varyar.tcp.service.IDeviceButtonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备按键设置服务实现类
 * @author chenxw1
 * @date 2023/2/23
 */
@Service
public class DeviceButtonServiceImpl implements IDeviceButtonService {

    @Autowired
    private ChannelMessageDisposer channelMessageDisposer;

    /**
     * 设置按键号码
     * @param req
     * @return
     */
    @Override
    public SetDeviceButtonResponse setDeviceButtonResponse(SetDeviceButtonReq req) {
        return channelMessageDisposer.dispose(ApiTypes.SET_NORMAL_BUTTON, req, SetDeviceButtonResponse::new);
    }


}
