package com.newlandnpt.varyar.tcp.gateway.handler.contacts;

import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.tcp.base.Response;
import com.newlandnpt.varyar.tcp.gateway.AbstractChannelMessageHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备通讯录处理器
 * @author chenxw1
 * @date 2023/2/22
 */
@Component
public class GetIncomingCallHandler extends AbstractChannelMessageHandler<GetIncomingCallReq, Response> {

    @Override
    public ApiTypes apiType() {
        return ApiTypes.GET_INCOMING_CALL;
    }
//    @Override
//    public String messageIdentify() {
//            return "GET_INCOMING_CALL";
//    }

    @Override
    public GetIncomingCallReq newInstance() {
        return new GetIncomingCallReq();
    }

    @Override
    public Response handle(ChannelHandlerContext ctx, GetIncomingCallReq req) {
        GetIncomingCallResponse getIncomingCallResponse = new GetIncomingCallResponse();
        List<GetIncomingCallResponse.AddPhoneNumbers> addPhoneNumbers = new ArrayList<>();
        GetIncomingCallResponse.AddPhoneNumbers addPhoneNumbers1 = new GetIncomingCallResponse.AddPhoneNumbers();
        addPhoneNumbers1.setPhone("15060665801");
//        ChannelCache.setCache(req.getDeviceNo(),ctx.channel());
        return getIncomingCallResponse;
    }
}
