package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.constant.Constants;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.common.utils.SMS.SMSUtils;
import com.newlandnpt.varyar.common.utils.SMS.templates.DeviceOutLineWarn;
import com.newlandnpt.varyar.common.utils.SMS.templates.FallWarn;
import com.newlandnpt.varyar.common.utils.SMS.templates.PassOutWarn;
import com.newlandnpt.varyar.common.utils.SMS.templates.SMSVerificate;
import com.newlandnpt.varyar.system.domain.TMsg;
import com.newlandnpt.varyar.system.service.IMemberService;
import com.newlandnpt.varyar.system.service.IMsgService;
import com.newlandnpt.varyar.system.service.ISmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * 短信 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class SmsServiceImpl implements ISmsService {
    private static final Logger log = LoggerFactory.getLogger(SmsServiceImpl.class);



    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IMsgService msgService;

    //发送状态定义
    private static final String SEND_SUCCESS="1";
    private static final String SEND_NOT="2";
    private static final String SEND_FAILURE="3";
    /**
     * 发送短信
     */
    @Override
    public AjaxResult sendSms(String msg,String phone){
        AjaxResult ajax = AjaxResult.success();
        Integer codeTime  =  Constants.SMS_CAPTCHA_EXPIRATION;
        //短信验证模板
        SMSVerificate smsVerificate = new SMSVerificate(msg,String.valueOf(codeTime)+"分钟");
        if(smsVerificate.isCorrectParams()){
            try {
                SMSUtils.sendMsg(phone,smsVerificate);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            return AjaxResult.error("短信内容参数校验失败！");
        }

        System.out.println("--sendSms--");
        return ajax;
    }

    public void  sendSmsEvent(TMsg msg, String tempType){

        String phone = msg.getDeviceName();
        //获取手机尾号
        String telPhone = phone.substring(phone.length() - 4);
        String deviceName = msg.getDeviceName();
        //事件消息类型：级别为紧急时细分(0:断网,1:人员跌倒,2:人员移动);普通事件(暂无)
        switch (tempType) {
            case "0":
                DeviceOutLineWarn deviceOutLineWarn = new DeviceOutLineWarn(telPhone, deviceName);
                if (deviceOutLineWarn.isCorrectParams()) {
                    try {
                        SMSUtils.sendMsg(phone, deviceOutLineWarn);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    log.error(">>>> 断网模板短信内容参数校验失败");
                }
                break;
            case "1":
                FallWarn fallWarn = new FallWarn(telPhone, deviceName);
                if (fallWarn.isCorrectParams()) {
                    try {
                        SMSUtils.sendMsg(phone, fallWarn);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    log.error(">>>> 人员跌倒模板短信内容参数校验失败");
                }
                break;
            case "2":
                PassOutWarn passOutWarn = new PassOutWarn(telPhone, deviceName, "大卧室", "离开", "50");
                if (passOutWarn.isCorrectParams()) {
                    try {
                        SMSUtils.sendMsg(phone, passOutWarn);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    log.error(">>>> 人员移动模板短信内容参数校验失败");
                }
                break;
        }

        //发送完成更新短信状态
        try {
            msg.setSendTime(DateUtils.getNowDate());
            //发送更新
            msg.setSendStatus(SEND_SUCCESS);
            msgService.updateTMsgSendStatus(msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
