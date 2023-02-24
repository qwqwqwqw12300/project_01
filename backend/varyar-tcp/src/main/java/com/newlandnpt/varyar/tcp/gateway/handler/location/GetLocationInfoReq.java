package com.newlandnpt.varyar.tcp.gateway.handler.location;

import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;

/**
 * 位置信息请求
 * @author chenxw1
 * @date 2023/2/23
 */
public class GetLocationInfoReq extends MessageHead implements Req {


    @Override
    public void handleMessage(String body) {
        if(StringUtils.isBlank(body)){
            return ;
        }
    }
}
