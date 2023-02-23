package com.newlandnpt.varyar.tcp.gateway.handler.login;

import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.tcp.base.DeviceChannelCache;
import com.newlandnpt.varyar.tcp.base.Response;
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
    public ApiTypes apiType() {
        return ApiTypes.DEVICE_LOGIN;
    }

    @Override
    public DeviceLoginReq newInstance() {
        return new DeviceLoginReq();
    }

    @Override
    public Response handle(ChannelHandlerContext ctx, DeviceLoginReq req) {
        DeviceLoginResponse deviceLoginResponse = new DeviceLoginResponse();
        DeviceChannelCache.setCache(req.getDeviceNo(),req.getIccid(),
                req.getMsgTime(),ctx.channel());
        return deviceLoginResponse;
    }
}
