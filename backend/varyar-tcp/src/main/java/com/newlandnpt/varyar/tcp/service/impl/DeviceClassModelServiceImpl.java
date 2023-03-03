package com.newlandnpt.varyar.tcp.service.impl;

import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.tcp.dispose.ChannelMessageDisposer;
import com.newlandnpt.varyar.tcp.dispose.protect.RequestCallResponse;
import com.newlandnpt.varyar.tcp.dispose.setClassModel.SetClassModelReq;
import com.newlandnpt.varyar.tcp.dispose.setClassModel.SetClassModelResponse;
import com.newlandnpt.varyar.tcp.service.IDeviceClassModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceClassModelServiceImpl implements IDeviceClassModelService {

    @Autowired
    private ChannelMessageDisposer channelMessageDisposer;

    @Override
    public SetClassModelResponse setClassModel(SetClassModelReq req) {
        return channelMessageDisposer.dispose(ApiTypes.SET_CLASS_MODEL, req, SetClassModelResponse::new);
    }
}
