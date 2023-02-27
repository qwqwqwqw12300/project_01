package com.newlandnpt.varyar.system.service;

import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.system.domain.TMsg;

/**
 * 短信 服务层
 * 
 * @author ruoyi
 */
public interface ISmsService
{
    /**
     * 发送短信
     */
    AjaxResult sendSms(String msg,String phone);

    void sendSmsEvent(TMsg msg, String tempType);

}
