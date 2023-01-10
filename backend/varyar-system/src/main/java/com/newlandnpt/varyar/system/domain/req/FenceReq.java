package com.newlandnpt.varyar.system.domain.req;

import lombok.Data;

/**
 * 高德-地理围栏查询请求类
 * @author lisd
 * @date 2022/12/26
 **/
@Data
@SuppressWarnings({"ALL", "pmd:LowerCamelCaseVariableNamingRule"})
public class FenceReq {

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
     * 是否返回形状信息
     * 1：是 0：否
     */
    private Integer outputshape;

    /**
     * 围栏的唯一标识
     * 支持一次传入多个，以","分割；单次最多支持100个，如超出将只截取前100个作为输入。
     */
    private String gfids;

    /**
     * 终端id
     * 支持一次添加多个终端tid，以“，”分割。单次最多支持100个，如超出将只截取前100个作为输入。
     */
    private String tids;

    /**
     * 指定坐标
     * 格式：x,y
     */
    private String location;

    /**
     * 查询页数
     * 默认1
     */
    private Integer page;

    /**
     * 每页围栏数量
     * 单页数据数量，取值 [1, 100]
     */
    private Integer pagesize;

}
