package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TEvent;

/**
 * 事件Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface TEventMapper 
{
    /**
     * 查询事件
     * 
     * @param eventId 事件主键
     * @return 事件
     */
    public TEvent selectTEventByEventId(Long eventId);

    /**
     * 查询事件列表
     * 
     * @param tEvent 事件
     * @return 事件集合
     */
    public List<TEvent> selectTEventList(TEvent tEvent);

    /**
     * 新增事件
     * 
     * @param tEvent 事件
     * @return 结果
     */
    public int insertTEvent(TEvent tEvent);

    /**
     * 修改事件
     * 
     * @param tEvent 事件
     * @return 结果
     */
    public int updateTEvent(TEvent tEvent);

    /**
     * 删除事件
     * 
     * @param eventId 事件主键
     * @return 结果
     */
    public int deleteTEventByEventId(Long eventId);

    /**
     * 批量删除事件
     * 
     * @param eventIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTEventByEventIds(Long[] eventIds);
}
