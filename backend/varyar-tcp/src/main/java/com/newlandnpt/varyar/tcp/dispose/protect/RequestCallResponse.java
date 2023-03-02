package com.newlandnpt.varyar.tcp.dispose.protect;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Response;

/**
 * 安全防护 响应
 * @author ljx
 * @date 2023/2/28
 */
public class RequestCallResponse extends MessageHead implements Response {
    /**
     * 应答状态(0=请求成功;1=请求异常;)
     */
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void handleMessage(String body) {
        String[] str = body.split("@");
        setStatus(str[0]);
    }
}
