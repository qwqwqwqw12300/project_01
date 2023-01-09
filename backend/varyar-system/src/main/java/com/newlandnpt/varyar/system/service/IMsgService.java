package com.newlandnpt.varyar.system.service;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TMsg;

/**
 * 消息Service接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface IMsgService
{
    /**
     * 查询消息
     * 
     * @param msgId 消息主键
     * @return 消息
     */
    public TMsg selectTMsgByMsgId(Long msgId);

    /**
     * 查询消息列表
     * 
     * @param tMsg 消息
     * @return 消息集合
     */
    public List<TMsg> selectTMsgList(TMsg tMsg);
    /**
     * 查询消息列表
     *
     * @param tMsg 消息
     * @return 消息集合
     */
    public List<TMsg> selectTMsgList(Long memberId);

    /**
     * 新增消息
     * 
     * @param tMsg 消息
     * @return 结果
     */
    public int insertTMsg(TMsg tMsg);

    /**
     * 修改消息
     * 
     * @param tMsg 消息
     * @return 结果
     */
    public int updateTMsg(TMsg tMsg);

    /**
     * 批量删除消息
     * 
     * @param msgIds 需要删除的消息主键集合
     * @return 结果
     */
    public int deleteTMsgByMsgIds(Long[] msgIds);

    /**
     * 删除消息信息
     * 
     * @param msgId 消息主键
     * @return 结果
     */
    public int deleteTMsgByMsgId(Long msgId);
}
