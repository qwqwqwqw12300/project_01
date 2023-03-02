package com.newlandnpt.varyar.common.utils.tcp.req;


import java.util.Collections;
import java.util.List;

/**
 * 安全防护
 * @author ljx
 * @date 2023/2/28
 */
public class RequestCallReq extends MessageHead{
    /**
     * 发起监听请求
     */
    private String requestCall = "1";

    public RequestCallReq() {
        setApiType("REQUEST_CALL");
        setMsgType("1");
    }

    public String getRequestCall() {
        return requestCall;
    }

    public void setRequestCall(String requestCall) {
        this.requestCall = requestCall;
    }

}
