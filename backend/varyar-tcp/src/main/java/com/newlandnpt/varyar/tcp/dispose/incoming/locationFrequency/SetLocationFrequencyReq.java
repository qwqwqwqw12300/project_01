package com.newlandnpt.varyar.tcp.dispose.incoming.locationFrequency;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 设置位置上报频率
 * @author ljx
 * @date 2023/2/23
 */
public class SetLocationFrequencyReq extends MessageHead implements Req {

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

    @Override
    public List<String> getRequests() {
        // 限制上报频率的最小值为5分钟
        String timeFrequency = (Integer.parseInt(getReportFrequency()) >= 5 ) ? getReportFrequency() : "5" ;
        return Collections.singletonList(timeFrequency);
    }
}
