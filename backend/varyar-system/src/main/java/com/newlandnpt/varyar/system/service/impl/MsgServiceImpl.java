package com.newlandnpt.varyar.system.service.impl;

import java.util.List;

import com.newlandnpt.varyar.common.annotation.DataScope;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.system.domain.TEvent;
import com.newlandnpt.varyar.system.service.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TMsgMapper;
import com.newlandnpt.varyar.system.domain.TMsg;
import com.newlandnpt.varyar.system.service.IMsgService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
}
