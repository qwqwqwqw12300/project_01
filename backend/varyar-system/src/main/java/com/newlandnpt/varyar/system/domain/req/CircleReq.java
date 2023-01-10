package com.newlandnpt.varyar.system.domain.req;

import lombok.Data;

/**
 * 高德-圆形地理围栏新增请求类
 * @author lisd
 * @date 2022/12/25
 */
@Data
public class CircleReq {
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
     * 支持中文、英文大小字母、英文下划线"_"、英文横线"-"和数字，长度不大于128个字符
     */
    private String desc;

    /**
     * 围栏中心点坐标
     * 格式X,Y
     */
    private String center;

    /**
     * 围栏半径
     * 单位：米，整数，取值范[1,50000]
     */
    private String radius;

}
