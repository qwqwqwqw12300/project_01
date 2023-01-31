package com.newlandnpt.varyar.system.service.impl;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.common.utils.jiguang.JGpushUtils;
import com.newlandnpt.varyar.system.domain.TEvent;
import com.newlandnpt.varyar.system.domain.TMsg;
import com.newlandnpt.varyar.system.service.IJiGuangSendService;
import com.newlandnpt.varyar.system.service.IMemberService;
import com.newlandnpt.varyar.system.service.IMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 版本Service业务层处理
 * 
 * @author chenxw
 * @date 2022-12-24
 */
@Service
public class JiGuangSendServiceImpl implements IJiGuangSendService
{
    @Autowired
    private JGpushUtils myJPushClient;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private IMsgService msgService;
    private static final Logger log = LoggerFactory.getLogger(JiGuangSendServiceImpl.class);


    public void jiGuangSend(List<TMsg> tMsgList){

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (TMsg msg : tMsgList)
        {

            try {

                //通过会员id查询设备注册id
                long MemberId = msg.getMemberId();
                String registrationId = memberService.selectMemberByMemberId(MemberId).getRegistrationId();
                String registrationType = memberService.selectMemberByMemberId(MemberId).getRegistrationType();

    //            String registrationId =  "1a0018970a4f60163e3"   ;
    //            String registrationType = "0"   ;
                List<String> aliasList = Arrays.asList(registrationId);
                String notificationTitle = "艾吉通";
                String msgTitle = "事件通知提醒";
                String msgContent = msg.getContent();

                myJPushClient.sendToAliasList(aliasList,notificationTitle, msgTitle, msgContent, "exts");
                successNum++;
                successMsg.append("<br/>" + successNum + "、推送消息编号 " + msg.getNo() + "成功");
                //推送完成后更新表的状态
                //发送时间更新
                msg.setSendTime(DateUtils.getNowDate());
                //发送更新
                msg.setSendStatus("1");
                msgService.updateTMsg(msg);


            } catch (Exception e) {
                failureNum++;
                String msgResult = "<br/>" + failureNum + "推送消息编号" + msg.getNo() + "失败";
                failureMsg.append(msg + e.getMessage());
                //失败原因
                String reason ="连接失败";
                log.error(msgResult, e);
            }
        }
    }
}
