package com.newlandnpt.varyar.system.domain.vo;

import lombok.Data;

import java.util.Map;

/**
 * @author lisd
 * @date 2023/1/10
 **/
@Data
public class GeoResultVo {

    /**
     * 响应码 10000代表成功
     */
    private Integer errcode;

    /**
     * 响应消息 成功时为"OK"
     */
    private String errmsg;

    /**
     * 其他响应参数 通常为空
     */
    private String errdetail;

    /**
     * 响应数据
     */
    private Map<String, Object> data;

}
