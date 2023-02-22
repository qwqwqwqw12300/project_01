package com.newlandnpt.varyar.tcp.gateway.handler.login;

import com.newlandnpt.varyar.tcp.base.ChannelCache;
import com.newlandnpt.varyar.tcp.base.Response;
import com.newlandnpt.varyar.tcp.gateway.handler.login.DeviceLoginReq;
import com.newlandnpt.varyar.tcp.gateway.handler.login.DeviceLoginResponse;
import com.newlandnpt.varyar.tcp.gateway.AbstractChannelMessageHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * 设备登录处理器
 * @author lin.ju
 * @date 2023/2/22
 */
@Component
public class DeviceLoginHandler extends AbstractChannelMessageHandler<DeviceLoginReq, Response> {


    @Override
    public String messageIdentify() {
        return "DEVICE_LOGIN";
    }

    @Override
    public DeviceLoginReq newInstance() {
        return new DeviceLoginReq();
    }

    @Override
    public Response handle(ChannelHandlerContext ctx, DeviceLoginReq req) {
        DeviceLoginResponse deviceLoginResponse = new DeviceLoginResponse();
        ChannelCache.setCache(req.getDeviceNo(),ctx.channel());
        return deviceLoginResponse;
    }
}
