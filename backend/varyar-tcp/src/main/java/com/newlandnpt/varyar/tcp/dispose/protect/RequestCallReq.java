package com.newlandnpt.varyar.tcp.dispose.protect;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;

import java.util.Collections;
import java.util.List;

/**
 * 安全防护
 * @author ljx
 * @date 2023/2/28
 */
public class RequestCallReq extends MessageHead implements Req {
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

    @Override
    public List<String> getRequests() {
        return Collections.singletonList(getRequestCall());
    }
}
