package com.newlandnpt.varyar.system.domain.req;

import lombok.Data;

/**
 * 高德-轨迹服务新增请求类
 * @author lisd
 * @date 2023/1/3
 **/
@Data
public class TrackAddReq {

    /**
     * 高德key
     * 用户在高德地图官网申请Web服务API类型Key
     */
    private String key;

    /**
     * 服务名称
     * Service 的名字，名字在同一个 Key 下不可重复，不可为空。
     * 命名规则：仅支持中文、英文大小字母、英文下划线"_"、英文横线"-"和数字,不能以"_"开头，最长不得超过128个字符。
     */
    private String name;

    /**
     * 服务描述
     * 针对此 Service 的文字描述，方便用户对 Service 进行记忆。
     * 命名规则：仅支持中文、英文大小字母、英文下划线"_"、英文横线"-"和数字, 不能以"_"开头，最长不得超过128个字符。
     */
    private String desc;

}
