package com.newlandnpt.varyar.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DeviceWarnParameter implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 跌倒预警开关 ：1开 0关  */
    private String fallWarn="1";

    /** 无人预警 */
    private NobodyWarn nobodyWarn;


//    private LeaveBedWarn leaveBedWarn;

    public String getFallWarn() {
        return fallWarn;
    }

    public void setFallWarn(String fallWarn) {
        this.fallWarn = fallWarn;
    }

    public NobodyWarn getNobodyWarn() {
        return nobodyWarn;
    }

    public void setNobodyWarn(NobodyWarn nobodyWarn) {
        this.nobodyWarn = nobodyWarn;
    }

    public static class NobodyWarn {

        /** 无人预警开关 ：1开 0关 */
        private String noBody = "1";
        /** 无人预警连续24小时开关 1开 0关 */
        private String noBodyContinue = "1";
        /** 无人预警规列表 */
        private List<WarnRule> warnRules;

        public String getNoBody() {
            return noBody;
        }

        public void setNoBody(String noBody) {
            this.noBody = noBody;
        }

        public String getNoBodyContinue() {
            return noBodyContinue;
        }

        public void setNoBodyContinue(String noBodyContinue) {
            this.noBodyContinue = noBodyContinue;
        }

        public List<WarnRule> getWarnRules() {
            return warnRules;
        }

        public void setWarnRules(List<WarnRule> warnRules) {
            this.warnRules = warnRules;
        }
    }

    public static class WarnRule {
        /**
         * 规则编号
         */
        private String  ruleNo;
        /**
         * 规则名称
         */
        private String WarnRuleName;

        /**
         * 规则开关
         */
        private String  ruleSwitch="1";
        /**
         * 日期类型
         */
        private String dateType;
        /**
         * 星期 0 星期天
         */
        private String[] week = {"0", "1", "2", "3", "4", "5", "6"};
        /**
         * 开始日期 data yyyy-MM-dd
         */
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date startDate;
        /**
         * 结束日期 data yyyy-MM-dd
         */
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date endDate;

        /**
         * 结束时间 开始时间
         */
        @JsonFormat(pattern = "HH:mm")
        private Date startTime;
        /**
         * 结束时间 data HH:mm
         */
        @JsonFormat(pattern = "HH:mm")
        private Date endTime;

        public String getRuleNo() {
            return ruleNo;
        }

        public void setRuleNo(String ruleNo) {
            this.ruleNo = ruleNo;
        }

        public String getWarnRuleName() {
            return WarnRuleName;
        }

        public void setWarnRuleName(String warnRuleName) {
            WarnRuleName = warnRuleName;
        }

        public String getRuleSwitch() {
            return ruleSwitch;
        }

        public void setRuleSwitch(String ruleSwitch) {
            this.ruleSwitch = ruleSwitch;
        }

        public String getDateType() {
            return dateType;
        }

        public void setDateType(String dateType) {
            this.dateType = dateType;
        }

        public String[] getWeek() {
            return week;
        }

        public void setWeek(String[] week) {
            this.week = week;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public Date getStartTime() {
            return startTime;
        }

        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }
    }

}










