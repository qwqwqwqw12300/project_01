package com.newlandnpt.varyar.common.constant.tcp;

/**
 * 消息类型
 * 标识通信方向， 取值如下: 1. 下发请求 2. 下发响应 3. 上报请求 4. 上报响应
 * 说明：平台下发 请求标示 1，则终 端响应标示为 2， 终端上报接口标 示为 3 平台响应 标示为 4
 * @author lin.ju
 * @date 2023/2/23
 */
public class MsgTypeConstants {

    /**
     * 下发请求
     */
    public static final String DISPOSE_REQ = "1";

    /**
     * 下发响应
     */
    public static final String DISPOSE_RESPONSE = "2";


    /**
     * 上报请求
     */
    public static final String REPORT_REQ = "3";

    /**
     * 上报响应
     */
    public static final String REPORT_RESPONSE = "4";



}
