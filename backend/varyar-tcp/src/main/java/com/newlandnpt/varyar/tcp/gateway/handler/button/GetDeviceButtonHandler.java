package com.newlandnpt.varyar.tcp.gateway.handler.button;

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
public class GetDeviceButtonHandler extends AbstractChannelMessageHandler<GetDeviceButtonReq, Response> {

    @Override
    public ApiTypes apiType() {
        return ApiTypes.GET_NORMAL_BUTTON;
    }

//    @Override
//    public String messageIdentify() {
//            return "GET_NORMAL_BUTTON";
//    }

    @Override
    public GetDeviceButtonReq newInstance() {
        return new GetDeviceButtonReq();
    }

    @Override
    public Response handle(ChannelHandlerContext ctx, GetDeviceButtonReq req) {
        GetDeviceButtonResponse getDeviceButtonResponse = new GetDeviceButtonResponse();

        List<GetDeviceButtonResponse.buttonPhone> buttonFroms = new ArrayList<>();
        GetDeviceButtonResponse.buttonPhone buttonPhone = new GetDeviceButtonResponse.buttonPhone();
        buttonPhone.setButton("0");
        buttonPhone.setPhone("15060665801");
        buttonFroms.add(buttonPhone);

        getDeviceButtonResponse.setButtonFroms(buttonFroms);

        return getDeviceButtonResponse;
    }
}
