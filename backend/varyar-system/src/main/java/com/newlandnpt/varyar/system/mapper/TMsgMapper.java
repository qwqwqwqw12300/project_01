package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TMsg;
import org.apache.ibatis.annotations.Param;

/**
 * 消息Mapper接口
 *
 * @author ruoyi
 * @date 2023-01-05
 */
public interface TMsgMapper
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

    public List<TMsg> selectTMsgByMemberId(Long memberId);
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
     * 删除消息
     *
     * @param msgId 消息主键
     * @return 结果
     */
    public int deleteTMsgByMsgId(Long msgId);

    /**
     * 批量删除消息
     *
     * @param msgIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMsgByMsgIds(Long[] msgIds);

    /**
     * 查询消息列表
     *
     * @param tMsg 消息
     * @return 消息集合
     */
    public List<TMsg> selectTMsgInfo(TMsg tMsg);
    /**
     * 根据查询消息总数
     *
     * @param operateFlag 已读未读
     * @return 消息总数
     */
    int selectMsgCountByFlag(String operateFlag);

    /**
     * 查询消息总数
     *
     * @param operateFlag 已读未读
     * @return 消息总数
     */
    int selectMsgCount(String operateFlag);

}
