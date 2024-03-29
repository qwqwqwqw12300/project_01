package com.newlandnpt.varyar.system.service;

import java.util.List;

import com.newlandnpt.varyar.common.core.domain.model.EventRequest;
import com.newlandnpt.varyar.system.domain.TEvent;
import com.newlandnpt.varyar.system.domain.TEventList;
import com.newlandnpt.varyar.system.domain.dto.event.EventCountGroupByLevelDto;

/**
 * 事件Service接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface IEventService
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
     * 查询事件列表
     *
     * @param eventIds 事件
     * @return 事件集合
     */
    public List<TEvent> selectTEventListByIds(Long[] eventIds);

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
     * 批量删除事件
     * 
     * @param eventIds 需要删除的事件主键集合
     * @return 结果
     */
    public int deleteTEventByEventIds(Long[] eventIds);

    /**
     * 删除事件信息
     * 
     * @param eventId 事件主键
     * @return 结果
     */
    public int deleteTEventByEventId(Long eventId);

    /**
     * 未处理事件数
     * @return
     */
    public long unHandleEventCount();
    /**
     * 机构未处理事件数
     * @return
     * @param orgId
     */
    public long unHandleEventCountInOrg(Long orgId);

    /**
     * 未处理会员事件数
     * @return
     */
    public long unHandleMemberEventCount();

    /**
     * 根据级别聚合统计设备的事件数
     * @param deviceId
     * @return
     */
    public List<EventCountGroupByLevelDto> countUnHandleByDeviceGroupByLevel(Long deviceId);

    /**
     * 根据查询消息总数
     *
     * @param operateFlag 已读未读
     * @return 消息总数
     */
    int selectEventCountByFlag(String operateFlag);

    /**
     * 查询消息总数
     *
     * @param operateFlag 已读未读
     * @return 消息总数
     */
    int selectEventCount(String operateFlag);

    /**
     * app查询事件列表
     *
     * @param eventRequest 事件条件请求
     * @return 事件集合
     */
    public List<TEventList> selectEventInfo(EventRequest eventRequest);
}
