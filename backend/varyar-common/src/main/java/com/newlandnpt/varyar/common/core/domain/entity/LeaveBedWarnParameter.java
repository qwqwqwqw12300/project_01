package com.newlandnpt.varyar.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class LeaveBedWarnParameter implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 离床间隔是否开启:0关闭 1开启
     */
    private String leaveBedInterval = "0";
    /**
     * 间隔时间 s
     */
    private long intervalTime =300;
    private SetRuleDate setRuleDate = new SetRuleDate();

    public String getLeaveBedInterval() {
        return leaveBedInterval;
    }

    public void setLeaveBedInterval(String leaveBedInterval) {
        this.leaveBedInterval = leaveBedInterval;
    }

    public long getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
    }

    public SetRuleDate getSetRuleDate() {
        return setRuleDate;
    }

    public void setSetRuleDate(SetRuleDate setRuleDate) {
        this.setRuleDate = setRuleDate;
    }

    public static class SetRuleDate {
        /**
         * 日期类型
         */
        private String dateType="1";
        /**
         * 星期
         */
        private String[] week = {"0", "1", "2", "3", "4", "5", "6"};
        /**
         * 开始日期 data yyyy-MM-dd
         */
        @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
        private Date startDate;
        /**
         * 结束日期 data yyyy-MM-dd
         */
        @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
        private Date endDate;

        /**
         * 结束时间 开始时间
         */
        @JsonFormat(pattern = "HH:mm",timezone="GMT+8")
        private Date startTime;
        /**
         * 结束时间 data HH:mm
         */
        @JsonFormat(pattern = "HH:mm",timezone="GMT+8")
        private Date endTime;

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
