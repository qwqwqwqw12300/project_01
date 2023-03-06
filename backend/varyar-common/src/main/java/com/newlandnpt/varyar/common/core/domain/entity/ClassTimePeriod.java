package com.newlandnpt.varyar.common.core.domain.entity;

/**
 * 课堂模式周期
 * @author ljx
 * @date 2023/3/1
 */
public class ClassTimePeriod {
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

    public ClassTimePeriod() {
    }

    public ClassTimePeriod(String time, String period, String enable) {
        this.time = time;
        this.period = period;
        this.enable = enable;
    }

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
}
