package com.newlandnpt.varyar.system.domain.req;

import lombok.Data;

/**
 * 高德-终端请求类
 * @author lisd
 * @date 2023/1/3
 **/
@Data
public class TerminalReq {

    /**
     * 高德key
     * 用户在高德地图官网申请Web服务API类型Key
     */
    private String key;

    /**
     * 服务的唯一编号
     *
     * 开发者希望在哪个 Service下创建终端，填入对应 Service 的id 
     */
    private String sid;

    /**
     * 服务名称
     *
     * 希望创建终端的名字，同一个service下不可出现名字相同的终端，不可为空
     * 命名规则：仅支持中文、英文大小字母、英文下划线"_"、英文横线"-"和数字
     * 最长不得超过128字符, 不能以"_"开头
     * 需要说明，此字段将作为日后可视化管理的显示字段，不可修改 
     */
    private String name;

    /**
     * 服务描述
     *
     * 针对增加终端的可读性描述
     * 命名规则：仅支持中文、英文大小字母、英文下划线"_"、英文横线"-"和数字
     * 最长不得超过128字符,不能以"_"开头 
     */
    private String desc;

    /**
     * 自定义字段
     *
     * 此字段为用户针对终端的自定义字段，若想使用必须先在“针对终端自定义字段”中进行创建字段。
     * 此中必须为一个json格式对象，需要按照定义的类型上传数据，例如定义为int，则仅上传数字。
     * 仅支持中文、英文大小字母、英文下划线"_"、英文横线"-"、数字和”.”，,不允许"_"开头。
     * 内容最长不得超过128字符 
     */
    private String props;

    /**
     * 查询的设备ID
     *
     * 终端id
     */
    private String tid;

    /**
     * 当前显示页数
     * 每页显示50条数据
     */
    private Integer page;
}
