package com.newlandnpt.varyar.common.core.domain.entity;

import com.newlandnpt.varyar.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;

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

        @ApiModelProperty("设备电子围栏id")
        @Excel(name = "高德API服务-电子围栏类型")
        private String fenceType;

        @ApiModelProperty("高德API服务-电子围栏id")
        @Excel(name = "高德API服务-电子围栏id")
        private Long geoFenceId;

        @ApiModelProperty("经度")
        @Excel(name = "经度")
        private String longitude;

        @ApiModelProperty("纬度")
        @Excel(name = "纬度")
        private String latitude;

        @ApiModelProperty("半径")
        @Excel(name = "半径")
        private String radius;

        /**
         * 多边形顶点坐标
         * 格式 X1,Y1;X2,Y2;...
         * 顶点顺序可按顺时针或逆时针排列；
         */
        @ApiModelProperty("多边形顶点坐标")
        @Excel(name = "多边形顶点坐标")
        private String points;

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

        public String getFenceType() {
            return fenceType;
        }

        public void setFenceType(String fenceType) {
            this.fenceType = fenceType;
        }

        public Long getGeoFenceId() {
            return geoFenceId;
        }

        public void setGeoFenceId(Long geoFenceId) {
            this.geoFenceId = geoFenceId;
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
