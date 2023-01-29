package com.newlandnpt.varyar.system.service;

/**
 * 设备事件服务
 * @author lisd
 * @date 2023/1/6
 **/
public interface DeviceEventService {

    /**
     * 设备状态事件
     */
    void deviceStateIssue(String deviceNo);

    /**
     * 设备断网事件
     */
    void deviceDisconnectionIssue();

    /**
     * 设备网络恢复事件
     */
    void deviceReconnectionIssue();

    /**
     * 进出房间事件
     * @param deviceNo
     */
    void deviceAccessIssue(String deviceNo);

    /**
     * 设备超出地理围栏事件
     */
    void deviceLeaveLocationIssue(String deviceNo);
}
