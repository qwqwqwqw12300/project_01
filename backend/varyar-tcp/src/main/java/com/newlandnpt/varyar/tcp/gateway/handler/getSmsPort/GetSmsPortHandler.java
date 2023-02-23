package com.newlandnpt.varyar.tcp.gateway.handler.getSmsPort;

import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.tcp.base.Response;
import com.newlandnpt.varyar.tcp.gateway.AbstractChannelMessageHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * 端口获取处理器
 * @author ljx
 * @date 2023/2/23
 */
@Component
public class GetSmsPortHandler extends AbstractChannelMessageHandler<GetSmsPortReq, Response> {

    @Override
    public ApiTypes apiType() {
        return ApiTypes.GET_SMS_PORT;
    }

    @Override
    public GetSmsPortReq newInstance() {
        return new GetSmsPortReq();
    }

    @Override
    public Response handle(ChannelHandlerContext ctx, GetSmsPortReq req) {
        GetSmsPortResponse getSmsPortResponse = new GetSmsPortResponse();
        return getSmsPortResponse;
    }
}
