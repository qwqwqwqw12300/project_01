package com.newlandnpt.varyar.tcp.service;

import com.newlandnpt.varyar.tcp.dispose.protect.RequestCallReq;
import com.newlandnpt.varyar.tcp.dispose.protect.RequestCallResponse;

/**
 * 设备安全防护
 * @author ljx
 * @date 2023/2/28
 */
public interface IDeviceProtectService {
    RequestCallResponse requestCallResponse(RequestCallReq req);
}
