package com.newlandnpt.varyar.system.job;

import com.newlandnpt.varyar.system.service.DeviceEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lisd
 * @date 2023/1/16
 **/
@Component("deviceNetTask")
public class DeviceNetTask {

    @Autowired
    private DeviceEventService disconnectionService;

    public void checkDisconnection()
    {
        disconnectionService.deviceDisconnectionIssue();
        System.out.println("执行设备断网检查定时任务");
    }

    public void checkReconnection()
    {
        disconnectionService.deviceReconnectionIssue();
        System.out.println("执行设备网络恢复检查定时任务");
    }
}
