package com.newlandnpt.varyar.tcp.base;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 响应
 * @author lin.ju
 * @date 2023/2/22
 */
public interface Response {


    String getDeviceNo();
    void setDeviceNo(String deviceNo);
    String getIccid();
    void setIccid(String iccid);
    String getTranNo();
    void setTranNo(String tranNo);
    String getApiType();
    void setApiType(String apiType);
    String getMsgType();
    void setMsgType(String msgType);
    String getMsgTime();
    void setMsgTime(String msgTime);
    String getMsgLen();
    void setMsgLen(String msgLen);

    default void setHeadByRequest(Req req){
        setDeviceNo(req.getDeviceNo());
        setIccid(req.getIccid());
        setTranNo(req.getTranNo());
        setApiType(req.getApiType());
        setMsgType(req.getMsgType());
        setMsgTime(req.getMsgTime());
        setMsgLen(req.getMsgLen());
    }

    List<String> getResponses();

    default String generateMessage(){
        List<String> responses = getResponses();
        String responseBody = responses.stream().collect(Collectors.joining("@"));
        setMsgLen(""+responseBody.length());
        return Arrays.asList(getDeviceNo(),getIccid(),getTranNo(),getApiType(),getMsgType(),
                getMsgTime(),getMsgLen(),responseBody)
                .stream().collect(Collectors.joining(",","[","]"));
    }
}
