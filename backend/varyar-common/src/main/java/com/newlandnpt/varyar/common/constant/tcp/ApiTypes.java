package com.newlandnpt.varyar.common.constant.tcp;

import java.util.stream.Stream;

import static com.newlandnpt.varyar.common.constant.tcp.MsgTypeConstants.*;

/**
 * 接口唯一标识
 * @author lin.ju
 * @date 2023/2/23
 */
public enum ApiTypes {

    /** 设备登录 */
    DEVICE_LOGIN(REPORT_REQ,REPORT_RESPONSE),
    /** 设置呼入号码 */
    SET_INCOMING_CALL(DISPOSE_REQ,DISPOSE_RESPONSE),

    /** 获取呼入号码 */
    GET_INCOMING_CALL(REPORT_REQ,REPORT_RESPONSE),

    /** 按键获取 */
    GET_NORMAL_BUTTON(REPORT_REQ,REPORT_RESPONSE),

    /** 获取课堂模式 */
    GET_CLASS_MODEL(REPORT_REQ,REPORT_RESPONSE),
    /**获取端口**/
    GET_SMS_PORT(REPORT_REQ,REPORT_RESPONSE),
    /**位置上报**/
    REPORT_LOCATION_INFO(REPORT_REQ,REPORT_RESPONSE),
    /**SOS触发报警上报**/
    REPORT_SOS(REPORT_REQ,REPORT_RESPONSE),
    /**设置位置上报频率**/
    FREQUENCY_LOCATION_SET(DISPOSE_REQ,DISPOSE_RESPONSE),
    /**设置定位模式**/
    SET_LOCATION_MODE(DISPOSE_REQ,DISPOSE_RESPONSE),
    /**上报心跳数据包**/
    REPORT_HEARTBEAT(REPORT_REQ,REPORT_RESPONSE),
    /**缺电报警、关机报警**/
    ALARM_POWER(REPORT_REQ,REPORT_RESPONSE),
    /**设置服务信息**/
    SET_SERVER_INFO(DISPOSE_REQ,DISPOSE_RESPONSE),
    /**远程终端操作**/
    REMOTE_OPERATE_TERMINAL(DISPOSE_REQ,DISPOSE_RESPONSE),
    /**设置普通按键与 SOS 按键**/
    SET_NORMAL_BUTTON(DISPOSE_REQ,DISPOSE_RESPONSE),
    /**实时位置获取**/
    LOCATION_INFO_GET(REPORT_REQ,REPORT_RESPONSE),
    /** 安全防护**/
    REQUEST_CALL(DISPOSE_REQ,DISPOSE_RESPONSE),
    /** 课堂模式**/
    SET_CLASS_MODEL(DISPOSE_REQ,DISPOSE_RESPONSE)
    ;

    /**
     * 请求消息类型
     */
    private String reqMsgType;

    /**
     * 响应消息类型
     */
    private String responseMsgType;

    ApiTypes(String reqMsgType, String responseMsgType) {
        this.reqMsgType = reqMsgType;
        this.responseMsgType = responseMsgType;
    }

    public String getReqMsgType() {
        return reqMsgType;
    }

    public String getResponseMsgType() {
        return responseMsgType;
    }

    public static String responseMsgType(String apiType){
        return Stream.of(ApiTypes.values())
                .filter(p->p.name().equals(apiType))
                .findAny()
                .map(p->p.getResponseMsgType())
                .orElse(null);
    }

}
