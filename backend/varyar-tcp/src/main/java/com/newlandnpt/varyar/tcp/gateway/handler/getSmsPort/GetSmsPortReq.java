package com.newlandnpt.varyar.tcp.gateway.handler.getSmsPort;

import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;

/**
 * 端口获取报文体
 * 报文体格式： null
 * @author ljx
 * @date 2023/2/22
 */
public class GetSmsPortReq  extends MessageHead implements Req {
    /**
     * 默认信息
     */
    private String defaultInfo = "";

    public String getDefaultInfo() {
        return defaultInfo;
    }

    public void setDefaultInfo(String defaultInfo) {
        this.defaultInfo = defaultInfo;
    }

    @Override
    public void handleMessage(String body) {
        if(StringUtils.isBlank(body)){
            return ;
        }
        setDefaultInfo(body);
    }
}
