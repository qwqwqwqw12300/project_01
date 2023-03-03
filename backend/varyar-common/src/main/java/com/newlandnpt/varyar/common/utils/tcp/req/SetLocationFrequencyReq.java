package com.newlandnpt.varyar.common.utils.tcp.req;

import java.util.Collections;
import java.util.List;

/**
 * 设置位置上报频率
 * @author ljx
 * @date 2023/2/23
 */
public class SetLocationFrequencyReq extends MessageHead {

    /**
     * 上报频率(最小值为5)
     */
    private String reportFrequency = "5";

    public SetLocationFrequencyReq() {
        setApiType("FREQUENCY_LOCATION_SET");
        setMsgType("1");
    }

    public String getReportFrequency() {
        return reportFrequency;
    }

    public void setReportFrequency(String reportFrequency) {
        this.reportFrequency = reportFrequency;
    }

}
