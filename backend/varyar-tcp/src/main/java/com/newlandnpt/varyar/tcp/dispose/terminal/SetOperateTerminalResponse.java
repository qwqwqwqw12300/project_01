package com.newlandnpt.varyar.tcp.dispose.terminal;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Response;

/**
 * @author chenxw1
 * @date 2023/2/23
 *
 * 应答状态, 应答状态按位划分,每位中1代表该位所
 * 代表的操作没有成功完成,0 代表完成, 若应答状态
 * 返回0,则代表所有请求的远程终端操作都已成功完
 * 成
 */
public class SetOperateTerminalResponse extends MessageHead implements Response {

    /**
     * 应答状态(0=完成;1=操作没有成功完成;)
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
