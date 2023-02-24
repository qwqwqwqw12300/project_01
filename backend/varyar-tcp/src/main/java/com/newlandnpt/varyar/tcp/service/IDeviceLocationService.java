package com.newlandnpt.varyar.tcp.service;

import com.newlandnpt.varyar.tcp.dispose.incoming.locationFrequency.SetLocationFrequencyReq;
import com.newlandnpt.varyar.tcp.dispose.incoming.locationFrequency.SetLocationFrequencyResponse;
import com.newlandnpt.varyar.tcp.dispose.incoming.locationMode.SetLocationModeReq;
import com.newlandnpt.varyar.tcp.dispose.incoming.locationMode.SetLocationModeResponse;

/**
 * 设备位置服务
 * @author ljx
 * @date 2023/2/23
 */
public interface IDeviceLocationService {

    /**
     * 设置位置上报频率
     * @param req
     * @return
     */
    SetLocationFrequencyResponse setLocationFrequency(SetLocationFrequencyReq req);

    /**
     * 设置定位模式
     * @param req
     * @return
     */
    SetLocationModeResponse setLocationMode(SetLocationModeReq req);
}
