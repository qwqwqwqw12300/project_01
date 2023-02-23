package com.newlandnpt.varyar.tcp.gateway.handler.getSmsPort;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Response;

import java.util.Arrays;
import java.util.List;

/**
 * 端口获取响应
 * @author ljx
 * @date 2023/2/22
 */
public class GetSmsPortResponse extends MessageHead implements Response {
    /**
     * 端口号
     */
    private String port;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public List<String> getResponses() {
        return Arrays.asList(getPort());
    }
}
