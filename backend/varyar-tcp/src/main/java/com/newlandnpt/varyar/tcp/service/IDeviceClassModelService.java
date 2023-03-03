package com.newlandnpt.varyar.tcp.service;

import com.newlandnpt.varyar.tcp.dispose.setClassModel.SetClassModelReq;
import com.newlandnpt.varyar.tcp.dispose.setClassModel.SetClassModelResponse;

public interface IDeviceClassModelService {

    SetClassModelResponse setClassModel(SetClassModelReq req);
}
