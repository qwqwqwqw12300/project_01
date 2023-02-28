package com.newlandnpt.varyar.system.service;

import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.system.domain.TEvent;
import com.newlandnpt.varyar.system.domain.TMsg;
import com.newlandnpt.varyar.system.domain.vo.SmsEventParamsVo;

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

    void sendSmsEvent(TEvent event, SmsEventParamsVo smsEventParamsVo , String tempType);

}
