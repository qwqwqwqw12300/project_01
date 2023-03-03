package com.newlandnpt.varyar.system.domain;


import com.newlandnpt.varyar.common.core.domain.BaseEntity;

/**
 * 位置守护
 * @author ljx
 * @date 2023/3/2
 */
public class LocationGuard extends BaseEntity {

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
}
