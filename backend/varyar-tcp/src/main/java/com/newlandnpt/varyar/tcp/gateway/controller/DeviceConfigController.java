package com.newlandnpt.varyar.tcp.gateway.controller;

import com.newlandnpt.varyar.tcp.dispose.ChannelMessageDisposer;
import com.newlandnpt.varyar.tcp.dispose.incoming.call.IncomingCallReq;
import com.newlandnpt.varyar.tcp.dispose.incoming.call.IncomingCallResponse;
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
@RequestMapping("/tcp/device/config")
public class DeviceConfigController {

    @Autowired
    private ChannelMessageDisposer disposer;

    @PostMapping("/incoming/call")
    @ResponseBody
    public IncomingCallResponse incomingCall(@RequestBody IncomingCallReq req) {
        return disposer.dispose(req, IncomingCallResponse::new);
    }

}
