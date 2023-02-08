package com.newlandnpt.varyar.system.service;

import com.newlandnpt.varyar.system.domain.TMsg;

import java.util.List;

/**
 * 极光推送接口
 * @author
 * @date 2023/1/31
 **/
public interface IJiGuangSendService {

    /**
     * 设备状态事件
     */
    public void jiGuangSend(List<TMsg> tMsgList);
    public void jiGuangSend(TMsg msg);


}
