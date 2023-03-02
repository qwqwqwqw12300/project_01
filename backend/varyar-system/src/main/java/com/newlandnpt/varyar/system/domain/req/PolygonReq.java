package com.newlandnpt.varyar.system.domain.req;

/**
 * 高德-多边形地理围栏新增请求类
 * @author ljx
 * @date 2023/3/1
 */
public class PolygonReq {
    /**
     * 高德key
     * 用户在高德地图官网申请Web服务API类型Key
     */
    private String key;

    /**
     * 服务唯一编号
     * sid为猎鹰service唯一编号
     */
    private String sid;

    /**
     * 围栏id
     * sid为猎鹰service唯一编号
     */
    private String gfid;

    /**
     * 围栏名称
     * 在同一个 sid 下不可重复，不可为空。
     *
     * 支持中文、英文大小字母、英文下划线"_"、英文横线"-"和数字，长度不大于128个字符
     */
    private String name;

    /**
     * 围栏描述
     */
    private String desc;

    /**
     * 多边形顶点坐标
     * 格式 X1,Y1;X2,Y2;...
     * 顶点顺序可按顺时针或逆时针排列；
     */
    private String points;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getGfid() {
        return gfid;
    }

    public void setGfid(String gfid) {
        this.gfid = gfid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
