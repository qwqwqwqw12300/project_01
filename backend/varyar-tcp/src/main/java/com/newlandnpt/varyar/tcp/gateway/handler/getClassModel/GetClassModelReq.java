package com.newlandnpt.varyar.tcp.gateway.handler.getClassModel;

import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;

/**
 * 获取课堂模式报文体
 * 报文体格式：
 * 1
 * @author ljx
 * @date 2023/2/22
 */
public class GetClassModelReq extends MessageHead implements Req {
    /**
     * 课堂模式 一般为 “1”
     */
    private String modelType;

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    @Override
    public void handleMessage(String body) {
        if(StringUtils.isBlank(body)){
            return ;
        }
        String[] str = body.split("@");
        setModelType(str[0]);
    }
}
