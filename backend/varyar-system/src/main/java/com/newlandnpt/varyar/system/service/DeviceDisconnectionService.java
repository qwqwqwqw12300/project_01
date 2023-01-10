package com.newlandnpt.varyar.system.service;

/**
 * 设备断网事件服务
 * @author lisd
 * @date 2023/1/6
 **/
public interface DeviceDisconnectionService {

    /**
     * 设备断网事件
     * @param deviceNo
     */
    void deviceDisconnectionIssue(String deviceNo);
}
