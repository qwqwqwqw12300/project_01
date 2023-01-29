package com.newlandnpt.varyar.system.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.newlandnpt.varyar.common.annotation.DataScope;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.common.utils.uuid.IdUtils;
import com.newlandnpt.varyar.common.utils.uuid.UUID;
import com.newlandnpt.varyar.system.domain.TEvent;
import com.newlandnpt.varyar.system.domain.TMemberFamily;
import com.newlandnpt.varyar.system.service.IEventService;
import com.newlandnpt.varyar.system.service.IMemberFamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TMsgMapper;
import com.newlandnpt.varyar.system.domain.TMsg;
import com.newlandnpt.varyar.system.service.IMsgService;
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
public class MsgServiceImpl implements IMsgService
{
    @Autowired
    private TMsgMapper tMsgMapper;
    @Autowired
    private IEventService iEventService;
    @Autowired
    private IMemberFamilyService memberFamilyService;
    /**
     * 查询消息
     * 
     * @param msgId 消息主键
     * @return 消息
     */
    @Override
    public TMsg selectTMsgByMsgId(Long msgId)
    {
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
    public List<TMsg> selectTMsgList(TMsg tMsg)
    {
        return tMsgMapper.selectTMsgList(tMsg);
    }
    public List<TMsg> selectTMsgList(Long memberId){
        return tMsgMapper.selectTMsgByMemberId(memberId);
    }

    /**
     * 新增消息
     * 
     * @param tMsg 消息
     * @return 结果
     */
    @Override
    public int insertTMsg(TMsg tMsg)
    {
        tMsg.setCreateTime(DateUtils.getNowDate());
        tMsg.setOperateFlag("0");
        return tMsgMapper.insertTMsg(tMsg);
    }

    /**
     * 修改消息
     * 
     * @param tMsg 消息
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public int updateTMsg(TMsg tMsg)
    {
        tMsg.setUpdateTime(DateUtils.getNowDate());
        tMsgMapper.updateTMsg(tMsg);
        //标志事件已处理
        TEvent tEvent = iEventService.selectTEventByEventId(tMsg.getEventId());
        if (tEvent!=null){
            tEvent.setOperateFlag("1");
        }
        return iEventService.updateTEvent(tEvent);
    }
    @Override
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public int updateTMsgs(List<TMsg> tMsgs) throws Exception {
        try{
            for(TMsg item : tMsgs){
                item.setUpdateTime(DateUtils.getNowDate());
                tMsgMapper.updateTMsg(item);
                //标志事件已处理
                TEvent tEvent = iEventService.selectTEventByEventId(item.getEventId());
                if (tEvent!=null){
                    tEvent.setOperateFlag("1");
                }
                iEventService.updateTEvent(tEvent);
            }
        }catch (Exception e){
            throw new Exception("批量修改消息失败！");
        }
        return tMsgs.size();
    }

    /**
     * 批量删除消息
     * 
     * @param msgIds 需要删除的消息主键
     * @return 结果
     */
    @Override
    public int deleteTMsgByMsgIds(Long[] msgIds)
    {
        return tMsgMapper.deleteTMsgByMsgIds(msgIds);
    }

    /**
     * 删除消息信息
     * 
     * @param msgId 消息主键
     * @return 结果
     */
    @Override
    public int deleteTMsgByMsgId(Long msgId)
    {
        return tMsgMapper.deleteTMsgByMsgId(msgId);
    }

    /**
     * 根据查询消息总数
     *
     * @param operateFlag 已读未读
     * @return 消息总数
     */
    @Override
    public int selectMsgCountByFlag(String operateFlag)
    {
        return tMsgMapper.selectMsgCountByFlag(operateFlag);
    }

    /**
     * 查询消息总数
     *
     * @param operateFlag 已读未读
     * @return 消息总数
     */
    @Override
    public int selectMsgCount(String operateFlag)
    {
        return tMsgMapper.selectMsgCount(operateFlag);
    }

    @Override
    public int sendMsgByEvent(TEvent event) {
        // 目前会员事件会同步发送app消息给 设备创建会员以及被分享家庭的会员
        Long memberId = event.getMemberId();
        Set<Long> memberIds = new HashSet<>();
        memberIds.add(memberId);

        TMemberFamily tMemberFamily = new TMemberFamily();
        tMemberFamily.setCreateMemberId(memberId);
        tMemberFamily.setFamilyId(event.getFamilyId());
        tMemberFamily.setDelFlag(DEL_FLAG_NOT_ACTIVE);
        List<TMemberFamily> memberFamilies = memberFamilyService.selectTMemberFamilyList(tMemberFamily);

        int result = 0;
        for(TMemberFamily memberFamily:memberFamilies){
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
            result+=insertTMsg(msg);
        }

        return result;
    }
}
