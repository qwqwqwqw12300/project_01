package com.newlandnpt.varyar.system.service.impl;

import java.io.IOException;
import java.util.*;

import com.newlandnpt.varyar.common.annotation.DataScope;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.common.utils.SMS.SMSUtils;
import com.newlandnpt.varyar.common.utils.SMS.templates.DeviceOutLineWarn;
import com.newlandnpt.varyar.common.utils.SMS.templates.FallWarn;
import com.newlandnpt.varyar.common.utils.SMS.templates.PassOutWarn;
import com.newlandnpt.varyar.common.utils.uuid.IdUtils;
import com.newlandnpt.varyar.common.utils.uuid.UUID;
import com.newlandnpt.varyar.system.domain.TEvent;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.domain.TMemberFamily;
import com.newlandnpt.varyar.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TMsgMapper;
import com.newlandnpt.varyar.system.domain.TMsg;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.newlandnpt.varyar.common.constant.Constants.DEL_FLAG_NOT_ACTIVE;
import static com.newlandnpt.varyar.common.constant.MsgConstants.*;

/**
 * 消息Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class MsgServiceImpl implements IMsgService {
    private static final Logger log = LoggerFactory.getLogger(MsgServiceImpl.class);


    @Autowired
    private TMsgMapper tMsgMapper;
    @Autowired
    private IEventService iEventService;
    @Autowired
    private IMemberFamilyService memberFamilyService;
    @Autowired
    private IJiGuangSendService jiGuangSendService;
    @Autowired
    private IMemberService memberService;

    @Autowired
    private ISmsService smsService;

    /**
     * 查询消息
     *
     * @param msgId 消息主键
     * @return 消息
     */
    @Override
    public TMsg selectTMsgByMsgId(Long msgId) {
        return tMsgMapper.selectTMsgByMsgId(msgId);
    }

    /**
     * 查询消息列表
     *
     * @param tMsg 消息
     * @return 消息
     */
    @DataScope(orgAlias = "m")
    @Override
    public List<TMsg> selectTMsgList(TMsg tMsg) {
        return tMsgMapper.selectTMsgList(tMsg);
    }

    public List<TMsg> selectTMsgList(Long memberId) {
        return tMsgMapper.selectTMsgByMemberId(memberId);
    }

    /**
     * 新增消息
     *
     * @param tMsg 消息
     * @return 结果
     */
    @Override
    public int insertTMsg(TMsg tMsg) {
        tMsg.setCreateTime(DateUtils.getNowDate());
        tMsg.setOperateFlag("0");
        return tMsgMapper.insertTMsg(tMsg);
    }

    /**
     * 修改推送消息状态
     *
     * @param tMsg 消息
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int updateTMsgSendStatus(TMsg tMsg) {
        return tMsgMapper.updateTMsg(tMsg);
    }

    /**
     * app端设备消息已读状态修改
     *
     * @param tMsg 消息
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int updateTMsg(TMsg tMsg) {
        tMsg.setUpdateTime(DateUtils.getNowDate());
        tMsgMapper.updateTMsg(tMsg);
        //标志事件已处理
        TEvent tEvent = iEventService.selectTEventByEventId(tMsg.getEventId());
        if (tEvent != null) {
            tEvent.setOperateTime(new Date());
            tEvent.setOperateFlag("1");
        }
        return iEventService.updateTEvent(tEvent);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int updateTMsgs(List<TMsg> tMsgs) throws Exception {
        try {
            for (TMsg item : tMsgs) {
                item.setUpdateTime(DateUtils.getNowDate());
                tMsgMapper.updateTMsg(item);
                //标志事件已处理
                TEvent tEvent = iEventService.selectTEventByEventId(item.getEventId());
                if (tEvent != null) {
                    tEvent.setOperateTime(new Date());
                    tEvent.setOperateFlag("1");
                }
                iEventService.updateTEvent(tEvent);
            }
        } catch (Exception e) {
            throw new Exception("批量修改消息失败！");
        }
        return tMsgs.size();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int updateBatchTMsgs(TMsg tMsg) throws Exception {
        int rownum = 0;
        try {
            //查询获取消息事件id
            List<TMsg> tMsgs = tMsgMapper.selectTMsgList(tMsg);
            //更新消息表记录
            rownum = tMsgMapper.updateTMsgs(tMsg);
            if (rownum > 0) {
                for (TMsg msg : tMsgs) {
                    //标志事件已处理
                    TEvent tEvent = iEventService.selectTEventByEventId(msg.getEventId());
                    if (tEvent != null) {
                        tEvent.setOperateTime(new Date());
                        tEvent.setOperateFlag("1");
                    }
                    iEventService.updateTEvent(tEvent);
                }
            }
        } catch (Exception e) {
            throw new Exception("批量修改消息失败！");
        }
        return rownum;
    }

    /**
     * 批量删除消息
     *
     * @param msgIds 需要删除的消息主键
     * @return 结果
     */
    @Override
    public int deleteTMsgByMsgIds(Long[] msgIds) {
        return tMsgMapper.deleteTMsgByMsgIds(msgIds);
    }

    /**
     * 删除消息信息
     *
     * @param msgId 消息主键
     * @return 结果
     */
    @Override
    public int deleteTMsgByMsgId(Long msgId) {
        return tMsgMapper.deleteTMsgByMsgId(msgId);
    }

    /**
     * 根据查询消息总数
     *
     * @param operateFlag 已读未读
     * @return 消息总数
     */
    @Override
    public int selectMsgCountByFlag(String operateFlag) {
        return tMsgMapper.selectMsgCountByFlag(operateFlag);
    }

    /**
     * 查询消息总数
     *
     * @param operateFlag 已读未读
     * @return 消息总数
     */
    @Override
    public int selectMsgCount(String operateFlag) {
        return tMsgMapper.selectMsgCount(operateFlag);
    }

    @Override
    public int sendMsgByEvent(TEvent event) {
        // 目前会员事件会同步发送app消息给 设备创建会员以及被分享家庭的会员
        Long memberId = event.getMemberId();

        TMemberFamily tMemberFamily = new TMemberFamily();
        tMemberFamily.setCreateMemberId(memberId);
        tMemberFamily.setFamilyId(event.getFamilyId() == null ? -1 : event.getFamilyId());
        tMemberFamily.setDelFlag(DEL_FLAG_NOT_ACTIVE);
        List<TMemberFamily> memberFamilies = memberFamilyService.selectTMemberFamilyList(tMemberFamily);
        int result = 0;

        if (memberFamilies == null || memberFamilies.stream().noneMatch(p -> p.getMemberId().longValue() == memberId.longValue())) {
            //会员设备未加入家庭组的情况 单独发消息
            TMsg msg = new TMsg();
            msg.setMsgType(MSG_TYPE_APP);
            msg.setDeviceType(event.getDeviceType());
            msg.setEventLevel(event.getLevel());
            msg.setNo(IdUtils.fastSimpleUUID());
            msg.setContent(event.getContent());
            msg.setEventId(event.getEventId());
            msg.setDeviceId(event.getDeviceId());
            msg.setMemberId(memberId);
            msg.setOrgId(event.getOrgId());
            msg.setOperator("系统");
            msg.setSendStatus(SEND_STATUS_NOT_SEND);
            msg.setOperateFlag(OPERATE_FLAG_NOT_HANDLE);
            result += insertTMsg(msg);
            try {
                jiGuangSendService.jiGuangSend(Arrays.asList(msg));
            } catch (Exception e) {
                log.error(">>>> 极光推送发送失败", e);
            }
        }

        for (TMemberFamily memberFamily : memberFamilies) {

            if (memberFamily.getMemberId() == null) {
                log.warn(">>>>> 家庭id:{}对应会员id为null，忽略发送消息", memberFamily.getMemberFamilyId());
                continue;
            }

            TMember member = memberService.selectMemberByMemberId(memberFamily.getMemberId());

            if (member == null) {
                log.warn(">>>>> 家庭id:{}对应会员id:{}获取不到会员信息，忽略发送消息", memberFamily.getMemberFamilyId(),
                        memberFamily.getMemberId());
                continue;
            }


            TMsg msg = new TMsg();
            msg.setMsgType(MSG_TYPE_APP);
            msg.setDeviceType(event.getDeviceType());
            msg.setEventLevel(event.getLevel());
            msg.setNo(IdUtils.fastSimpleUUID());
            msg.setContent(event.getContent());
            msg.setEventId(event.getEventId());
            msg.setDeviceId(event.getDeviceId());
            msg.setFamilyId(memberFamily.getFamilyId());
            msg.setMemberId(memberFamily.getMemberId());
            msg.setOrgId(event.getOrgId());
            msg.setOperator("系统");
            msg.setSendStatus(SEND_STATUS_NOT_SEND);
            msg.setOperateFlag(OPERATE_FLAG_NOT_HANDLE);
            result += insertTMsg(msg);
            try {
                jiGuangSendService.jiGuangSend(Arrays.asList(msg));
            } catch (Exception e) {
                log.error(">>>> 极光推送发送失败", e);
            }
        }

        //如果是紧急事件的话同时发送短信
/*        if ("urgent".equals(event.getLevel())) {
            if (memberFamilies == null || memberFamilies.stream().noneMatch(p -> p.getMemberId().longValue() == memberId.longValue())) {
                //会员设备未加入家庭组的情况 单独发消息
                TMsg msg = new TMsg();
                //短信类型
                msg.setMsgType(MSG_TYPE_SMS);
                msg.setDeviceType(event.getDeviceType());
                msg.setEventLevel(event.getLevel());
                //事件类型
                msg.setEventType(event.getEventType());
                msg.setNo(IdUtils.fastSimpleUUID());
                msg.setContent(event.getContent());
                msg.setEventId(event.getEventId());
                msg.setDeviceId(event.getDeviceId());
                msg.setOrgId(event.getOrgId());
                msg.setOperator("系统");
                msg.setSendStatus(SEND_STATUS_NOT_SEND);
                msg.setOperateFlag(OPERATE_FLAG_NOT_HANDLE);
                result += insertTMsg(msg);

                //发送短信接口处
                smsService.sendSmsEvent(msg,event.getEventType());
            }
            for (TMemberFamily memberFamily : memberFamilies) {
                TMsg msg = new TMsg();
                //短信类型
                msg.setMsgType(MSG_TYPE_SMS);
                msg.setDeviceType(event.getDeviceType());
                msg.setEventLevel(event.getLevel());
                //事件类型
                msg.setEventType(event.getEventType());
                msg.setNo(IdUtils.fastSimpleUUID());
                msg.setContent(event.getContent());
                msg.setEventId(event.getEventId());
                msg.setDeviceId(event.getDeviceId());
                msg.setFamilyId(memberFamily.getFamilyId());
                msg.setMemberId(memberFamily.getMemberId());
                msg.setOrgId(event.getOrgId());
                msg.setOperator("系统");
                msg.setSendStatus(SEND_STATUS_NOT_SEND);
                msg.setOperateFlag(OPERATE_FLAG_NOT_HANDLE);
                result += insertTMsg(msg);

                //发送短信接口处
                smsService.sendSmsEvent(msg,event.getEventType());

            }

        }*/
        return result;
    }
}

