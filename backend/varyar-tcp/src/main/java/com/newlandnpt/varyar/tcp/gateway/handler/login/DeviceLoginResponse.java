package com.newlandnpt.varyar.tcp.gateway.handler.login;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Response;

import java.util.Arrays;
import java.util.List;

/**
 * 设备登录响应
 * @author lin.ju
 * @date 2023/2/22
 */
public class DeviceLoginResponse extends MessageHead implements Response {

    /**
     * 应答状态(0=设置成功;1=非平台用户;2=设置异常;)
     */
    private String status = "0";

    /**
     * 端口号 发送短信时需要提供
     */
    private String port="0";

    /**
     * 发送短信 1发送0不发送
     */
    private String sendMsg="0";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(String sendMsg) {
        this.sendMsg = sendMsg;
    }

    @Override
    public List<String> getResponses() {
        return Arrays.asList(getStatus(),getPort(),getSendMsg());
    }
}
