package com.newlandnpt.varyar.tcp.dispose.setClassModel;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Response;
/**
 * 设置课堂模式
 * @author ljx
 * @date 2023/2/28
 */
public class SetClassModelResponse extends MessageHead implements Response {
    /**
     * 应答状态(0=设置成功;1=设置异常;)
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
