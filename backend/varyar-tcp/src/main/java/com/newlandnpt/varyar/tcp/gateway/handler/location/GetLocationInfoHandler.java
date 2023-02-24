package com.newlandnpt.varyar.tcp.gateway.handler.location;

import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.tcp.base.Response;
import com.newlandnpt.varyar.tcp.gateway.AbstractChannelMessageHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * 实时位置获取
 * @author chenxw1
 * @date 2023/2/23
 */
@Component
public class GetLocationInfoHandler extends AbstractChannelMessageHandler<GetLocationInfoReq, Response> {

    @Override
    public ApiTypes apiType() {
        return ApiTypes.LOCATION_INFO_GET;
    }

    @Override
    public GetLocationInfoReq newInstance() {
        return new GetLocationInfoReq();
    }

    @Override
    public Response handle(ChannelHandlerContext ctx, GetLocationInfoReq req) {
        GetLocationInfoResponse getLocationInfoResponse = new GetLocationInfoResponse();
        return getLocationInfoResponse;
    }
}
