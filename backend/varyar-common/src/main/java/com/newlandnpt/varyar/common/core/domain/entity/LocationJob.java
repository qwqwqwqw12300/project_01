package com.newlandnpt.varyar.common.core.domain.entity;

import java.util.List;

/**
 * 位置任务
 * @author ljx
 * @date 2023/3/1
 */
public class LocationJob {
    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务地点
     */
    private List<place> places;

    /**
     * 任务地点定义
     */
    public static class place{
        /**
         * 地址
         */
        private String address;

        /**
         * 预定时间
         */
        private String estimatedTime;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEstimatedTime() {
            return estimatedTime;
        }

        public void setEstimatedTime(String estimatedTime) {
            this.estimatedTime = estimatedTime;
        }
    }

    public LocationJob() {
    }

    public LocationJob(String jobName, List<place> places) {
        this.jobName = jobName;
        this.places = places;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public List<place> getPlaces() {
        return places;
    }

    public void setPlaces(List<place> places) {
        this.places = places;
    }
}
