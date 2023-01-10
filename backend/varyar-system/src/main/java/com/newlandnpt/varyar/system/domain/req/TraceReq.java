package com.newlandnpt.varyar.system.domain.req;

import lombok.Data;

import java.util.List;

/**
 * 高德-轨迹请求类
 * @author lisd
 * @date 2023/1/3
 **/
@Data
public class TraceReq {

    /**
     * 高德key
     * 用户在高德地图官网申请Web服务API类型Key
     */
    private String key;

    /**
     * 服务编号
     *
     * sid为用户希望增加轨迹的终端所属 Service 唯一编号
     */
    private String sid;

    /**
     * 设备编号
     *
     * tid为用户希望增加轨迹的终端唯一编号
     */
    private String tid;

    /**
     * 轨迹名称
     *
     * 开发者可为每条轨迹添加自定义名称，格式支持中文、英文大小写字母、英文下划线"_"、英文横线"-"和数字 。
     * 长度不得超过128个字符。
     */
    private String trname;

    /**
     * 轨迹ID
     *
     * 此ID为轨迹的唯一编号
     */
    private String trid;

    /**
     * 轨迹ID
     *
     * 此ID为轨迹的唯一编号
     */
    private List<TracePointsReq> points;
}
