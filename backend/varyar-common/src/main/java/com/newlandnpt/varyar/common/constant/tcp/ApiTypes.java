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
    /** 获取课堂模式 */
    GET_CLASS_MODEL(REPORT_REQ,REPORT_RESPONSE),
    /**获取端口**/
    GET_SMS_PORT(REPORT_REQ,REPORT_RESPONSE),
    /**位置上报**/
    REPORT_LOCATION_INFO(REPORT_REQ,REPORT_RESPONSE),
    /**SOS触发报警上报**/
    REPORT_SOS(REPORT_REQ,REPORT_RESPONSE)
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
