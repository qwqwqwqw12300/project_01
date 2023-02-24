package com.newlandnpt.varyar.tcp.service.impl;

import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.tcp.dispose.ChannelMessageDisposer;
import com.newlandnpt.varyar.tcp.dispose.incoming.locationFrequency.SetLocationFrequencyReq;
import com.newlandnpt.varyar.tcp.dispose.incoming.locationFrequency.SetLocationFrequencyResponse;
import com.newlandnpt.varyar.tcp.dispose.incoming.locationMode.SetLocationModeReq;
import com.newlandnpt.varyar.tcp.dispose.incoming.locationMode.SetLocationModeResponse;
import com.newlandnpt.varyar.tcp.service.IDeviceLocationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备位置服务实现
 * @author ljx
 * @date 2023/2/23
 */
@Service
public class DeviceLocationServiceImpl implements IDeviceLocationService {
    @Autowired
    private ChannelMessageDisposer channelMessageDisposer;

    @Override
    public SetLocationFrequencyResponse setLocationFrequency(SetLocationFrequencyReq req) {
        return channelMessageDisposer.dispose(ApiTypes.FREQUENCY_LOCATION_SET, req, SetLocationFrequencyResponse::new);
    }

    @Override
    public SetLocationModeResponse setLocationMode(SetLocationModeReq req) {
        return channelMessageDisposer.dispose(ApiTypes.SET_LOCATION_MODE, req, SetLocationModeResponse::new);
    }
}
