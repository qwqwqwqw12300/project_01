package com.newlandnpt.varyar.tcp.gateway.handler.getClassModel;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 获取课堂模式响应
 * 响应报文体: SOS呼入标识@SOS呼出标识@1=开始时间-结束时间!周期!是否生效@2=开始时间-结束时间!周期!是否生效
 * @author ljx
 * @date 2023/2/22
 */
public class GetClassModelResponse extends MessageHead implements Response {
    /**
     * SOS呼入标识 (0 ：SOS不可呼入 1：SOS可以呼入)
     */
    private String sosIncomingSign;

    /**
     * SOS呼出标识 (0：不可拨打SOS 1：可以拨打SOS)
     */
    private String sosOutgoingSign;

    /**
     * 时段设置
     */
    private List<String> timePeriodSetting;

    public String getSosIncomingSign() {
        return sosIncomingSign;
    }

    public void setSosIncomingSign(String sosIncomingSign) {
        this.sosIncomingSign = sosIncomingSign;
    }

    public String getSosOutgoingSign() {
        return sosOutgoingSign;
    }

    public void setSosOutgoingSign(String sosOutgoingSign) {
        this.sosOutgoingSign = sosOutgoingSign;
    }

    public List<String> getTimePeriodSetting() {
        return timePeriodSetting;
    }

    public void setTimePeriodSetting(List<String> timePeriodSetting) {
        this.timePeriodSetting = timePeriodSetting;
    }

    @Override
    public List<String> getResponses() {
        List<String> responses = Arrays.asList(getSosIncomingSign(),getSosOutgoingSign());
        responses.addAll(getTimePeriodSetting());
        return responses;
    }
}
