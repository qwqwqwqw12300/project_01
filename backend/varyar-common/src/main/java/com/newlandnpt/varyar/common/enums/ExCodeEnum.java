package com.newlandnpt.varyar.common.enums;

/**
 * 业务异常信息
 * @author ljx
 */
public enum ExCodeEnum {
    /**
     * 成功响应0000
     */
    SUCCESS("0000", "SUCCESS"),

    /**
     * 失败响应9999
     */
    ERROR("9999", "ERROR"),

    /**
     * 通用 1000-1999
     */
    DB_NOT_DEVICE("1001","数据库中不存在此设备"),

    /**
     * 电子牵挂卡 2000-2099
     */
    DEVICE_NOT_CARE_CARD("2000","该设备不是牵挂卡或设备不存在"),

    SET_PERIOD_DISABLE_FAIL("2001","设置时段禁用失败"),

    EXCEED_PERIOD_DISABLE_NUMBER_LIMIT("2002","最多设置10个时段"),


    ;




    private String code;
    private String msg;

    ExCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
