package com.newlandnpt.varyar.system.domain.req;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.Map;

/**
 * 高德-轨迹点请求类
 * @author lisd
 * @date 2023/1/3
 **/
@Data
public class TracePointsReq {

    /**
     * 经纬度坐标
     *
     * 格式为：X,Y
     * 小数点后最多6位
     */
    private String location;

    /**
     * 此次定位的时间点
     *
     * 需要用unix时间戳表示，需要精确到毫秒，若有相同的unix时间戳则会覆盖旧的点。
     */
    private String locatetime;

    /**
     * 速度
     *
     * 单位：km/h
     * 小数点后最多3位
     */
    private String speed;

    /**
     * 方向
     *
     * 取值范围[0~360]，0代表正北方，采取顺时针方向取值
     * 小数点后最多4位
     */
    private String direction;

    /**
     * 高度
     *
     * 单位：米
     * 小数点后最多3位
     */
    private String height;

    /**
     * 定位精度
     *
     * 仅允许输入数字
     * 小数点后最多3位
     */
    private String accuracy;

    /**
     * 用户自定义字段
     *
     * 此字段为用户针对轨迹的自定义字段，若想使用必须先在“增加轨迹的自定义字段”中进行创建字段。
     * 并且此之中必须为一个json格式对象，需要按照定义的类型上传数据，例如定义为int，则仅上传数字。
     * 仅支持中文、英文大小字母、英文下划线"_"、英文横线"-"、数字和“.”,不能以"_"开头。
     * 内容最长不得超过128字符 
     */
    private Map<String, T> props;
}
