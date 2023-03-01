package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.constant.Constants;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.common.utils.SMS.SMSUtils;
import com.newlandnpt.varyar.common.utils.SMS.templates.DeviceOutLineWarn;
import com.newlandnpt.varyar.common.utils.SMS.templates.FallWarn;
import com.newlandnpt.varyar.common.utils.SMS.templates.PassOutWarn;
import com.newlandnpt.varyar.common.utils.SMS.templates.SMSVerificate;
import com.newlandnpt.varyar.common.utils.uuid.IdUtils;
import com.newlandnpt.varyar.system.domain.TEvent;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.domain.TMsg;
import com.newlandnpt.varyar.system.domain.vo.SmsEventParamsVo;
import com.newlandnpt.varyar.system.service.IMemberService;
import com.newlandnpt.varyar.system.service.IMsgService;
import com.newlandnpt.varyar.system.service.ISmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.newlandnpt.varyar.common.constant.MsgConstants.*;


/**
 * 短信 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class SmsServiceImpl implements ISmsService {
    private static final Logger log = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Autowired
    private IMsgService msgService;
    @Autowired
    private IMemberService memberService;

    /**
     * 发送短信
     */
    @Override
    public AjaxResult sendSms(String msg,String phone){
        AjaxResult ajax = AjaxResult.success();
        Integer codeTime  =  Constants.SMS_CAPTCHA_EXPIRATION;
        String content = "【艾吉通】验证码"+msg+"，您正在进行身份验证，请勿告诉他人，此验证码"+codeTime+"分钟时间内有效。";
        TMember tMemberQuery= memberService.selectMemberByPhone(phone);
        TMsg tmsg =new TMsg();
        tmsg.setMsgType(MSG_TYPE_SMS);
        tmsg.setNo(IdUtils.fastSimpleUUID());
        tmsg.setContent(content);
        if(tMemberQuery != null){
            tmsg.setMemberId(tMemberQuery.getMemberId());
        }

        //发送时间更新
        tmsg.setSendTime(DateUtils.getNowDate());
        //短信验证模板
        SMSVerificate smsVerificate = new SMSVerificate(msg,String.valueOf(codeTime)+"分钟");
        if(smsVerificate.isCorrectParams()){
            try {
                SMSUtils.sendMsg(phone,smsVerificate);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            tmsg.setSendStatus(SEND_STATUS_SEND);
            tmsg.setOperateFlag(OPERATE_FLAG_HANDLED);
            //记入信息表
            msgService.insertTMsg(tmsg);
        }else {
            tmsg.setSendStatus(SEND_STATUS_SEND_FAILURE);
            tmsg.setOperateFlag(OPERATE_FLAG_NOT_HANDLE);
            tmsg.setReason("短信内容参数校验失败！");
            //记入信息表
            msgService.insertTMsg(tmsg);
            return AjaxResult.error("短信内容参数校验失败！");
        }

        return ajax;
    }
    @Override
    public void  sendSmsEvent(TEvent event, SmsEventParamsVo smsEventParamsVo , String tempType) {


        String phone = event.getMemberPhone();
        //获取手机尾号
        String telPhone = phone.substring(phone.length() - 4);
        String deviceName = event.getDeviceName();
        TMsg msg = new TMsg();
        //短信类型
        msg.setMsgType(MSG_TYPE_SMS);
        msg.setDeviceType(event.getDeviceType());
        msg.setEventLevel(event.getLevel());
        //事件类型
        msg.setEventType(event.getEventType());
        msg.setNo(IdUtils.fastSimpleUUID());
        msg.setEventId(event.getEventId());
        msg.setDeviceId(event.getDeviceId());
        msg.setOrgId(event.getOrgId());
        msg.setOperator("系统");
        //发送时间更新
        msg.setSendTime(DateUtils.getNowDate());
        //事件消息类型：级别为紧急时细分(0:断网,1:人员跌倒,2:人员移动);普通事件(暂无)
        switch (tempType) {
            case "0":
                msg.setContent("【艾吉通】会员（手机尾号"+telPhone+"）您好，艾吉通监测发现：设备 "+deviceName+ "疑似断网，请及时处理！");
                DeviceOutLineWarn deviceOutLineWarn = new DeviceOutLineWarn(telPhone, deviceName);
                if (deviceOutLineWarn.isCorrectParams()) {
                    try {
                        SMSUtils.sendMsg(phone, deviceOutLineWarn);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    msg.setSendStatus(SEND_STATUS_SEND);
                    msg.setOperateFlag(OPERATE_FLAG_HANDLED);
                    //记入信息表
                    msgService.insertTMsg(msg);
                } else {
                    msg.setSendStatus(SEND_STATUS_SEND_FAILURE);
                    msg.setOperateFlag(OPERATE_FLAG_NOT_HANDLE);
                    msg.setReason("断网模板短信内容参数校验失败！");
                    //记入信息表
                    msgService.insertTMsg(msg);
                    log.error(">>>> 断网模板短信内容参数校验失败！");
                }
                break;
            case "1":
                msg.setContent("【艾吉通】会员（手机尾号"+telPhone+"）您好，艾吉通监测发现：设备 "+deviceName+ "检测到有人疑似跌倒，请及时处理！");
                FallWarn fallWarn = new FallWarn(telPhone, deviceName);
                if (fallWarn.isCorrectParams()) {
                    try {
                        SMSUtils.sendMsg(phone, fallWarn);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    msg.setSendStatus(SEND_STATUS_SEND);
                    msg.setOperateFlag(OPERATE_FLAG_HANDLED);
                    //记入信息表
                    msgService.insertTMsg(msg);
                } else {
                    msg.setSendStatus(SEND_STATUS_SEND_FAILURE);
                    msg.setOperateFlag(OPERATE_FLAG_NOT_HANDLE);
                    msg.setReason("人员跌倒模板短信内容参数校验失败！");
                    //记入信息表
                    msgService.insertTMsg(msg);
                    log.error(">>>> 人员跌倒模板短信内容参数校验失败！");
                }
                break;
            case "2":
                msg.setContent("【艾吉通】会员（手机尾号"+telPhone+"）您好，艾吉通监测发现：设备 "+deviceName+ "监控到("+smsEventParamsVo.getField()+")内"+"【"+ smsEventParamsVo.getAction() +"】超过"+smsEventParamsVo.getTime()+"分钟，请及时处理！");
                PassOutWarn passOutWarn = new PassOutWarn(telPhone, deviceName, smsEventParamsVo.getField(), smsEventParamsVo.getAction(), String.valueOf(smsEventParamsVo.getTime()));
                if (passOutWarn.isCorrectParams()) {
                    try {
                        SMSUtils.sendMsg(phone, passOutWarn);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    msg.setSendStatus(SEND_STATUS_SEND);
                    msg.setOperateFlag(OPERATE_FLAG_HANDLED);
                    //记入信息表
                    msgService.insertTMsg(msg);
                } else {
                    msg.setSendStatus(SEND_STATUS_SEND_FAILURE);
                    msg.setOperateFlag(OPERATE_FLAG_NOT_HANDLE);
                    msg.setReason("人员移动模板短信内容参数校验失败！");
                    //记入信息表
                    msgService.insertTMsg(msg);
                    log.error(">>>> 人员移动模板短信内容参数校验失败！");
                }
                break;
        }


    }
}
