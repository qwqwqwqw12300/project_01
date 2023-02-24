package com.newlandnpt.varyar.tcp.gateway.controller;

import com.newlandnpt.varyar.tcp.dispose.button.SetDeviceButtonReq;
import com.newlandnpt.varyar.tcp.dispose.button.SetDeviceButtonResponse;
import com.newlandnpt.varyar.tcp.dispose.incoming.call.SetIncomingCallReq;
import com.newlandnpt.varyar.tcp.dispose.incoming.call.SetIncomingCallResponse;
import com.newlandnpt.varyar.tcp.dispose.serverInfo.SetServerInfoReq;
import com.newlandnpt.varyar.tcp.dispose.serverInfo.SetServerInfoResponse;
import com.newlandnpt.varyar.tcp.dispose.terminal.SetOperateTerminalReq;
import com.newlandnpt.varyar.tcp.dispose.terminal.SetOperateTerminalResponse;
import com.newlandnpt.varyar.tcp.service.IDeviceButtonService;
import com.newlandnpt.varyar.tcp.dispose.incoming.locationFrequency.SetLocationFrequencyReq;
import com.newlandnpt.varyar.tcp.dispose.incoming.locationFrequency.SetLocationFrequencyResponse;
import com.newlandnpt.varyar.tcp.dispose.incoming.locationMode.SetLocationModeReq;
import com.newlandnpt.varyar.tcp.dispose.incoming.locationMode.SetLocationModeResponse;
import com.newlandnpt.varyar.tcp.service.IDeviceLocationService;
import com.newlandnpt.varyar.tcp.service.IDevicePhoneService;
import com.newlandnpt.varyar.tcp.service.IDeviceServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 设备参数下发控制层
 * @author lin.ju
 * @date 2023/2/22
 */
@Controller
@RequestMapping("/tcp/device/phone")
public class DevicePhoneController {

    @Autowired
    private IDevicePhoneService devicePhoneService;
    @Autowired
    private IDeviceButtonService deviceButtonService;
    @Autowired
    private IDeviceServerService deviceServerService;



    @Autowired
    private IDeviceLocationService deviceLocationService;

    @PostMapping("/incoming/call")
    @ResponseBody
    public SetIncomingCallResponse setIncomingCall(@RequestBody SetIncomingCallReq req) {
        return devicePhoneService.setIncomingCall(req);
    }

    /**
     * 设置普通按键与 SOS 按键
     * @param req
     * @return
     */
    @PostMapping("/setButton")
    @ResponseBody
    public SetDeviceButtonResponse setDeviceButtonResponse(@RequestBody SetDeviceButtonReq req) {
        return deviceButtonService.setDeviceButtonResponse(req);
    }
    /**
     * 设置服务信息
     * @param req
     * @return
     */
    @PostMapping("/setServerInfo")
    @ResponseBody
    public SetServerInfoResponse setServerInfoResponse(@RequestBody SetServerInfoReq req) {
        return deviceServerService.setServerInfoResponse(req);
    }

    /**
     * 远程终端操作
     * @param req
     * @return
     */
    @PostMapping("/setOperateTerminal")
    @ResponseBody
    public SetOperateTerminalResponse setOperateTerminalResponse(@RequestBody SetOperateTerminalReq req) {
        return deviceServerService.setOperateTerminalResponse(req);
    }

    @PostMapping("/incoming/locationFrequency")
    @ResponseBody
    public SetLocationFrequencyResponse setLocationFrequency(@RequestBody SetLocationFrequencyReq req){
        return deviceLocationService.setLocationFrequency(req);
    }

    @PostMapping("/incoming/locationMode")
    @ResponseBody
    public SetLocationModeResponse setLocationMode(@RequestBody SetLocationModeReq req){
        return deviceLocationService.setLocationMode(req);
    }

}
