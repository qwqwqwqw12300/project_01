package com.newlandnpt.varyar.tcp.gateway.handler.reportHeartBeat;

import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;


/**
 * 上报心跳数据包报文体
 * 报文体格式： 当前电量@当前步数@
 * @author ljx
 * @date 2023/2/23
 */
public class ReportHeartBeatReq extends MessageHead implements Req {
    /**
     * 当前电量
     */
    private String currentPower = "";

    /**
     * 当前步数
     */
    private String currentSteps = "";

    public String getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(String currentPower) {
        this.currentPower = currentPower;
    }

    public String getCurrentSteps() {
        return currentSteps;
    }

    public void setCurrentSteps(String currentSteps) {
        this.currentSteps = currentSteps;
    }

    public ReportHeartBeatReq() {
    }

    public ReportHeartBeatReq(String currentPower, String currentSteps) {
        this.currentPower = currentPower;
        this.currentSteps = currentSteps;
    }

    @Override
    public void handleMessage(String body) {
        if(StringUtils.isBlank(body)){
            return ;
        }
        String[] str = body.split("@");
        setCurrentPower(str[0]);
        // 旧设备没有当前步数
        if(str.length>1) {
            setCurrentSteps(str[1]);
        }
    }
}
