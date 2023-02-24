package com.newlandnpt.varyar.tcp.gateway.handler.reportHeartBeat;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 上报心跳数据包响应
 * @author ljx
 * @date 2023/2/23
 */
public class ReportHeartBeatResponse  extends MessageHead implements Response {
    /**
     * 应答状态（0＝正常，1=非平台用户，2＝其他异常）
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
        return Collections.singletonList(getStatus());
    }
}
