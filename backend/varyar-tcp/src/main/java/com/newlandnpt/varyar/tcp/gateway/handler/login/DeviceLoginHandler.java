package com.newlandnpt.varyar.tcp.gateway.handler.login;

import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.tcp.base.DeviceChannelCache;
import com.newlandnpt.varyar.tcp.base.Response;
import com.newlandnpt.varyar.tcp.gateway.AbstractChannelMessageHandler;
import com.newlandnpt.varyar.tcp.gateway.handler.locationInfo.LocationInfoHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 设备登录处理器
 * @author lin.ju
 * @date 2023/2/22
 */
@Component
public class DeviceLoginHandler extends AbstractChannelMessageHandler<DeviceLoginReq, Response> {

    private static final Logger log = LoggerFactory.getLogger(DeviceLoginHandler.class);
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
        log.debug("设备【{}】,登录平台",req.getDeviceNo());
        DeviceChannelCache.setCache(req.getDeviceNo(),req.getIccid(),
                req.getMsgTime(),ctx.channel());
        return deviceLoginResponse;
    }
}
