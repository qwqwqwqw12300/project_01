package com.newlandnpt.varyar.tcp.gateway.handler.getClassModel;

import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.tcp.base.Response;
import com.newlandnpt.varyar.tcp.gateway.AbstractChannelMessageHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * 获取课堂模式处理器
 * @author ljx
 * @date 2023/2/23
 */
@Component
public class GetClassModelHandler extends AbstractChannelMessageHandler<GetClassModelReq, Response> {

    @Override
    public ApiTypes apiType() {
        return ApiTypes.GET_CLASS_MODEL;
    }

    @Override
    public GetClassModelReq newInstance() {
        return new GetClassModelReq();
    }

    @Override
    public Response handle(ChannelHandlerContext ctx, GetClassModelReq req) {
        GetClassModelResponse getClassModelResponse = new GetClassModelResponse();
        return getClassModelResponse;
    }
}
