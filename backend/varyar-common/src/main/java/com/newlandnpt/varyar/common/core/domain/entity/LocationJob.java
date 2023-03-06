package com.newlandnpt.varyar.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.newlandnpt.varyar.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.List;

/**
 * 位置任务
 * @author ljx
 * @date 2023/3/1
 */
public class LocationJob {

    /**
     * 设备NO
     */
    private String deviceNo;

    /**
     * uuid
     */
    private String uuid;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务地点
     */
    private List<place> places;

    /**
     * 最早时间
     */
    private Date firstDate;

    /**
     * 最迟时间
     */
    private Date lastDate;

    /**
     * 任务地点定义
     */
    public static class place{

        /**
         * 守护类型
         */
        private String guardType;

        /**
         * 高德API服务-电子围栏id
         */
        private Long geoFenceId;


        /**
         * 地址
         */
        private String address;


        /**
         * 经度
         */
        private String longitude;


        /**
         * 纬度
         */
        private String latitude;

        /**
         * 半径
         */
        private String radius;

        /**
         * 多边形顶点坐标
         * 格式 X1,Y1;X2,Y2;...
         * 顶点顺序可按顺时针或逆时针排列；
         */
        private String points;

        /**
         * 预定时间
         */
        @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy/MM/dd HH:mm")
        @DateTimeFormat(pattern="yyyy/MM/dd HH:mm")
        private Date estimatedTime;

        public String getGuardType() {
            return guardType;
        }

        public void setGuardType(String guardType) {
            this.guardType = guardType;
        }

        public Long getGeoFenceId() {
            return geoFenceId;
        }

        public void setGeoFenceId(Long geoFenceId) {
            this.geoFenceId = geoFenceId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getRadius() {
            return radius;
        }

        public void setRadius(String radius) {
            this.radius = radius;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public Date getEstimatedTime() {
            return estimatedTime;
        }

        public void setEstimatedTime(Date estimatedTime) {
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

    public Date getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(Date firstDate) {
        this.firstDate = firstDate;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
