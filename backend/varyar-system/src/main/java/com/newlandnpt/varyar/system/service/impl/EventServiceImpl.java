package com.newlandnpt.varyar.system.service.impl;

import java.util.List;

import com.newlandnpt.varyar.common.annotation.DataScope;
import com.newlandnpt.varyar.common.core.domain.model.EventRequest;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.system.domain.TEventList;
import com.newlandnpt.varyar.system.domain.dto.event.EventCountGroupByLevelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TEventMapper;
import com.newlandnpt.varyar.system.domain.TEvent;
import com.newlandnpt.varyar.system.service.IEventService;

/**
 * 事件Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class EventServiceImpl implements IEventService
{
    @Autowired
    private TEventMapper tEventMapper;

    /**
     * 查询事件
     * 
     * @param eventId 事件主键
     * @return 事件
     */
    @Override
    public TEvent selectTEventByEventId(Long eventId)
    {
        return tEventMapper.selectTEventByEventId(eventId);
    }

    /**
     * 查询事件列表
     * 
     * @param tEvent 事件
     * @return 事件
     */
    @DataScope(orgAlias = "eve")
    @Override
    public List<TEvent> selectTEventList(TEvent tEvent)
    {
        return tEventMapper.selectTEventList(tEvent);
    }

    /**
     * 新增事件
     * 
     * @param tEvent 事件
     * @return 结果
     */
    @Override
    public int insertTEvent(TEvent tEvent)
    {
        tEvent.setCreateTime(DateUtils.getNowDate());
        return tEventMapper.insertTEvent(tEvent);
    }

    /**
     * 修改事件
     * 
     * @param tEvent 事件
     * @return 结果
     */
    @Override
    public int updateTEvent(TEvent tEvent)
    {
        tEvent.setUpdateTime(DateUtils.getNowDate());
        return tEventMapper.updateTEvent(tEvent);
    }

    /**
     * 批量删除事件
     * 
     * @param eventIds 需要删除的事件主键
     * @return 结果
     */
    @Override
    public int deleteTEventByEventIds(Long[] eventIds)
    {
        return tEventMapper.deleteTEventByEventIds(eventIds);
    }

    /**
     * 删除事件信息
     * 
     * @param eventId 事件主键
     * @return 结果
     */
    @Override
    public int deleteTEventByEventId(Long eventId)
    {
        return tEventMapper.deleteTEventByEventId(eventId);
    }

    @Override
    public long unHandleEventCount() {
        return tEventMapper.unHandleEventCount();
    }

    @Override
    public long unHandleEventCountInOrg(Long orgId) {
        return tEventMapper.unHandleEventCountInOrg(orgId);
    }

    @Override
    public long unHandleMemberEventCount() {
        return tEventMapper.unHandleMemberEventCount();
    }

    @Override
    public List<EventCountGroupByLevelDto> countUnHandleByDeviceGroupByLevel(Long deviceId) {
        return tEventMapper.countUnHandleByDeviceGroupByLevel(deviceId);
    }

    /**
     * 根据查询消息总数
     *
     * @param operateFlag 已读未读
     * @return 消息总数
     */
    @Override
    public int selectEventCountByFlag(String operateFlag)
    {
        return tEventMapper.selectEventCountByFlag(operateFlag);
    }

    /**
     * 查询消息总数
     *
     * @param operateFlag 已读未读
     * @return 消息总数
     */
    @Override
    public int selectEventCount(String operateFlag)
    {
        return tEventMapper.selectEventCount(operateFlag);
    }

    /**
     * app查询事件列表
     *
     * @param eventRequest 事件条件请求
     * @return 事件
     */
    @Override
    public List<TEventList> selectEventInfo(EventRequest eventRequest)
    {
        return tEventMapper.selectEventInfo(eventRequest);
    }

}
