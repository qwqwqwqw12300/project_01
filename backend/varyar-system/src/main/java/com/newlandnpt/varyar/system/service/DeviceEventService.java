package com.newlandnpt.varyar.system.service;

import com.newlandnpt.varyar.system.domain.TDevice;

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
     * @param areaName
     * @param type
     * @param delayTime
     */
    void deviceAccessIssue(String deviceNo, String areaName, String type, long delayTime);

    /**
     * 跌倒事件
     * @param deviceNo
     */
    void deviceFallIssue(String deviceNo);

    /**
     * 设备超出地理围栏事件
     */
    void deviceLeaveLocationIssue(String deviceNo);

    /**
     * 设备24小时无人预警
     * @param deviceNo
     * @param device
     */
    void device24HoursExistsIssue(String deviceNo, TDevice device);
}
