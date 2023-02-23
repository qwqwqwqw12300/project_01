package com.newlandnpt.varyar.tcp.gateway.handler.locationInfo;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Response;

import java.util.Arrays;
import java.util.List;

/**
 * 位置信息响应
 * @author ljx
 * @date 2023/2/22
 */
public class LocationInfoResponse extends MessageHead implements Response {

    /**
     * 应答状态(0＝正常，1=非平台用户，2＝其他异常)
     */
    private String status = "0";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public List<String> getResponses() {
        return Arrays.asList(getStatus());
    }
}
