package com.newlandnpt.varyar.system.domain.req;

/**
 * 刪除高德电子围栏
 * @author ljx
 * @date 2023/3/3
 */
public class DeleteFenceReq {
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
}
