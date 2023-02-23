package com.newlandnpt.varyar.tcp.gateway.handler.sos;

import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;

/**
 * SOS 触发报警报文体
 * 报文体格式: 当前位置信息
 * 字段说明：
 * 1. 当前位置信息
 * 报文示例：
 * [35380100360174,898600MFSSYYGXXXXXXP,201805082000001000,REPORT_SO
 * S,3,20180528111820,1,1]
 *
 * @author ljx
 * @date 2023/2/22
 */
public class SosWarnReq extends MessageHead implements Req {
    /**
     * 当前位置信息
     */
    private String currentLocation;

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    @Override
    public void handleMessage(String body) {
        if(StringUtils.isBlank(body)){
            return ;
        }
        String[] str = body.split("@");

        setCurrentLocation(str[0]);
    }
}
