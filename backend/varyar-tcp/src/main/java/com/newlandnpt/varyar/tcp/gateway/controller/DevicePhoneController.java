package com.newlandnpt.varyar.tcp.gateway.controller;

import com.newlandnpt.varyar.tcp.dispose.incoming.call.SetIncomingCallReq;
import com.newlandnpt.varyar.tcp.dispose.incoming.call.SetIncomingCallResponse;
import com.newlandnpt.varyar.tcp.service.IDevicePhoneService;
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

    @PostMapping("/incoming/call")
    @ResponseBody
    public SetIncomingCallResponse setIncomingCall(@RequestBody SetIncomingCallReq req) {
        return devicePhoneService.setIncomingCall(req);
    }

}
