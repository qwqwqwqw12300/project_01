package com.newlandnpt.varyar.common.utils.tcp.req;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设置课堂模式
 * @author ljx
 * @date 2023/2/28
 */
public class SetClassModelReq extends MessageHead{
    /**
     * SOS呼入标识 (0 ：SOS不可呼入 1：SOS可以呼入)
     */
    private String sosIncomingSign = "1";

    /**
     * SOS呼出标识 (0：不可拨打SOS 1：可以拨打SOS)
     */
    private String sosOutgoingSign = "1";

    /**
     * 时段设置
     */
    private List<timePeriod> timePeriodSetting;

    public SetClassModelReq(){
        setApiType("SET_CLASS_MODEL");
        setMsgType("1");
    }

    public static class timePeriod{

        /**
         * 开始时间结束时间
         */
        private String time;

        /**
         * 周期
         */
        private String period;

        /**
         * 是否生效
         */
        private String enable;


        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getEnable() {
            return enable;
        }

        public void setEnable(String enable) {
            this.enable = enable;
        }

        public String generateMessage(){
            return Arrays.asList(time,period,enable).stream().collect(Collectors.joining("!"));
        }
    }

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


    public List<timePeriod> getTimePeriodSetting() {
        return timePeriodSetting;
    }

    public void setTimePeriodSetting(List<timePeriod> timePeriodSetting) {
        this.timePeriodSetting = timePeriodSetting;
    }
}
