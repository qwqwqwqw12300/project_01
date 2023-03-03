package com.newlandnpt.varyar.system.domain.req;

/**
 * 终端创建请求
 * @author lin.ju
 * @date 2023/2/28
 */
public class TerminalAddReq {

    /**
     * 希望创建终端的名字，同一个service下不可出现名字相同的终端，不可为空
     *
     * 命名规则：仅支持中文、英文大小字母、英文下划线"_"、英文横线"-"和数字
     *
     * 最长不得超过128字符, 不能以"_"开头
     *
     * 需要说明，此字段将作为日后可视化管理的显示字段，不可修改
     *
     * 必填
     *
     * 平台这里使用{设备类型}+"_"+{设备编号}作为终端名字
     */
    private String name;

    /**
     * 针对增加终端的可读性描述
     *
     * 命名规则：仅支持中文、英文大小字母、英文下划线"_"、英文横线"-"和数字
     *
     * 最长不得超过128字符,不能以"_"开头
     *
     * 平台这里不提供描述
     */
    private String desc;

    /**
     * 此字段为用户针对终端的自定义字段，若想使用必须先在“针对终端自定义字段”中进行创建字段。
     *
     * 此中必须为一个json格式对象，需要按照定义的类型上传数据，例如定义为int，则仅上传数字。
     *
     * 仅支持中文、英文大小字母、英文下划线"_"、英文横线"-"、数字和”.”，,不允许"_"开头。
     *
     * 内容最长不得超过128字符
     */
    private String props;

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

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }
}
