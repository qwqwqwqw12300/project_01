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

    SEND_HTTP_MSG_TO_TCP_FAIL("1002","给tcp服务器发送请求失败。"),

    TCP_SET_FAIL("1003","对于电子牵挂卡的设置失败"),
    /**
     * 电子牵挂卡 2000-2099
     */
    DEVICE_NOT_CARE_CARD("2000","该设备不是牵挂卡或设备不存在"),

    SET_PERIOD_DISABLE_FAIL("2001","设置时段禁用失败"),

    EXCEED_PERIOD_DISABLE_NUMBER_LIMIT("2002","最多设置10个时段"),

    LOCATION_GUARD_DELETE_FAIL("2003","位置守护删除失败"),

    LOCATION_GUARD_SET_FAIL("2004","位置守护设置失败"),

    FENCE_DELETE_FAIL("2005","电子围栏删除失败"),

    ADDRESS_NUMBER_LIMIT("2006","通讯录最多添加20个白名单用户"),

    ADDRESS_SET_FAIL("2007","通讯录设置失败"),

    FAMILY_NUMBER_SET_FAIL("2008","亲情号码设置失败"),
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
