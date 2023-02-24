package com.newlandnpt.varyar.tcp.gateway.handler.alarmPower;

import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;

/**
 * 缺电报警、关机报警 报文体
 * 报文体格式: 上报类型@剩余电量
 * 字段说明：
 * 1. 上报类型(1=缺电报警; 2=关机报警; 3=自动关机报警; 4=开机报警)
 * 2. 剩余电量
 * 缺电报警取值：1@10%
 * 关机报警取值：2@
 * 自动关机报警取值：3@5%
 * 开机报警取值:4@
 * @author ljx
 * @date 2023/2/23
 */
public class AlarmPowerReq extends MessageHead implements Req {
    /**
     * 缺电警告
     */
    private final static String POWER_SHORTAGE_ALARM = "1";

    /**
     * 自动关机警告
     */
    private final static String AUTOMATIC_SHUTDOWN_ALARM = "3";

    /**
     * 上报类型
     */
    private String reportType = "";

    /**
     * 剩余电量
     */
    private String remainPower = "";

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getRemainPower() {
        return remainPower;
    }

    public void setRemainPower(String remainPower) {
        this.remainPower = remainPower;
    }

    public AlarmPowerReq() {
    }

    public AlarmPowerReq(String reportType, String remainPower) {
        this.reportType = reportType;
        this.remainPower = remainPower;
    }

    @Override
    public void handleMessage(String body) {
        if(StringUtils.isBlank(body)){
            return ;
        }
        String[] str = body.split("@");
        setReportType(str[0]);
        if(POWER_SHORTAGE_ALARM.equals(str[0]) || AUTOMATIC_SHUTDOWN_ALARM.equals(str[0])) {
            setRemainPower(str[1]);
        }
    }
}
