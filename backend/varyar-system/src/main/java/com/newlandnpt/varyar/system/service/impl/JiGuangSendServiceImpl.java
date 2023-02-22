package com.newlandnpt.varyar.system.service.impl;


import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.common.utils.jiguang.JGpushUtils;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.domain.TMsg;
import com.newlandnpt.varyar.system.service.IJiGuangSendService;
import com.newlandnpt.varyar.system.service.IMemberService;
import com.newlandnpt.varyar.system.service.IMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 版本Service业务层处理
 *
 * @author chenxw
 * @date 2022-12-24
 */
@Service
public class JiGuangSendServiceImpl implements IJiGuangSendService {
    @Autowired
    private JGpushUtils myJPushClient;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private IMsgService msgService;

    //极光参数获取

    @Value("${jpush.appKey:}")
    private String appKey;

    @Value("${jpush.masterSecret:}")
    private String masterSecret;

    @Value("${jpush.apnsProduction:false}")
    private boolean apnsProduction;
    //发送状态定义
    private static final String SEND_SUCCESS="1";
    private static final String SEND_NOT="2";
    private static final String SEND_FAILURE="3";

    private static final Logger log = LoggerFactory.getLogger(JiGuangSendServiceImpl.class);
    public void sendValue(){
        myJPushClient.getValue(appKey,masterSecret,apnsProduction);
    }

    public void jiGuangSend(List<TMsg> tMsgList) {
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        this.sendValue();
        for (TMsg msg : tMsgList) {
            try {
                //通过会员id查询设备注册id
                long MemberId = msg.getMemberId();
                TMember member =memberService.selectMemberByMemberId(MemberId);
                String registrationId = member.getRegistrationId();
                String registrationType = member.getRegistrationType();
                String pushMessage = member.getParameter().getPushMessage();
                if(pushMessage.equals("1")){
                    List<String> aliasList = Arrays.asList(registrationId);
                    String notificationTitle = "艾吉通";
                    String msgTitle = "事件通知提醒";
                    String msgContent = msg.getContent();
                    myJPushClient.sendToAliasList(aliasList, notificationTitle, msgTitle, msgContent, "exts");
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、推送消息编号 " + msg.getNo() + "成功");
                    //推送完成后更新表的状态
                    //发送时间更新
                    msg.setSendTime(DateUtils.getNowDate());
                    //发送更新
                    msg.setSendStatus(SEND_SUCCESS);
                    msgService.updateTMsgSendStatus(msg);
                    log.info("会员:"+MemberId+"消息已推送");

                }else
                {
                    msg.setSendTime(DateUtils.getNowDate());
                    //发送更新
                    msg.setSendStatus(SEND_NOT);
                    msg.setReason("会员用户未开启推送消息开关");
                    msgService.updateTMsgSendStatus(msg);
                    log.info("会员:"+MemberId+"消息推送开关未启用！");
                }

            } catch (Exception e) {
                failureNum++;
                String msgResult = "<br/>" + failureNum + "推送消息编号" + msg.getNo() + "失败";
                failureMsg.append(msg + e.getMessage());
                //失败原因
                String reason = e.getMessage();
                msg.setSendStatus(SEND_FAILURE);
                msg.setSendTime(null);
                msg.setReason(reason);
                msgService.updateTMsgSendStatus(msg);
                log.error(msgResult, e);
            }
        }
    }

    public void jiGuangSend(TMsg msg) {
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        this.sendValue();
        try {
            //通过会员id查询设备注册id
            long MemberId = msg.getMemberId();
            TMember member =memberService.selectMemberByMemberId(MemberId);
            String registrationId = member.getRegistrationId();
            String registrationType = member.getRegistrationType();
            String pushMessage = member.getParameter().getPushMessage();
            //判断是否打开消息推送开关
            if(pushMessage.equals("1")){
                List<String> aliasList = Arrays.asList(registrationId);
                String notificationTitle = "艾吉通";
                String msgTitle = "事件通知提醒";
                String msgContent = msg.getContent();
                myJPushClient.sendToAliasList(aliasList, notificationTitle, msgTitle, msgContent, "exts");
                successNum++;
                successMsg.append("<br/>" + successNum + "、推送消息编号 " + msg.getNo() + "成功");
                //推送完成后更新表的状态
                //发送时间更新
                msg.setSendTime(DateUtils.getNowDate());
                //发送更新
                msg.setSendStatus(SEND_SUCCESS);
                msgService.updateTMsgSendStatus(msg);
                log.info("会员:"+MemberId+"消息已推送");

            }else
            {
                msg.setSendTime(DateUtils.getNowDate());
                //发送更新
                msg.setSendStatus(SEND_NOT);
                msg.setReason("会员用户未开启推送消息开关");
                msgService.updateTMsgSendStatus(msg);
                log.info("会员:"+MemberId+"消息推送开关未启用！");

            }
        } catch (Exception e) {
            failureNum++;
            String msgResult = "<br/>" + failureNum + "推送消息编号" + msg.getNo() + "失败";
            failureMsg.append(msg + e.getMessage());
            //失败原因
            String reason = e.getMessage();
            msg.setSendStatus(SEND_FAILURE);
            msg.setSendTime(null);
            msg.setReason(reason);
            msgService.updateTMsgSendStatus(msg);
            log.error(msgResult, e);
        }
    }
}
