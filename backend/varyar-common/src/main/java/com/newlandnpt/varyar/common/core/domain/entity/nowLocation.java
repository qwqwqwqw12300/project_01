package com.newlandnpt.varyar.common.core.domain.entity;

import java.io.Serializable;

public class nowLocation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String xPosCm;

    private String yPosCm;

    private String zPosCm;
    /**
     * 状态 0：正常  1：跌倒
     * */
    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getxPosCm() {
        return xPosCm;
    }

    public void setxPosCm(String xPosCm) {
        this.xPosCm = xPosCm;
    }

    public String getyPosCm() {
        return yPosCm;
    }

    public void setyPosCm(String yPosCm) {
        this.yPosCm = yPosCm;
    }

    public String getzPosCm() {
        return zPosCm;
    }

    public void setzPosCm(String zPosCm) {
        this.zPosCm = zPosCm;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
