package com.newlandnpt.varyar.system.mapper;

import java.util.List;

import com.newlandnpt.varyar.common.core.domain.model.EventRequest;
import com.newlandnpt.varyar.system.domain.TEvent;
import com.newlandnpt.varyar.system.domain.TEventList;
import com.newlandnpt.varyar.system.domain.dto.event.EventCountGroupByLevelDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 未处理事件数
     * @return
     */
    @Select("select count(*) from t_event where operate_flag = '0' ")
    public long unHandleEventCount();
    /**
     * 机构未处理事件数
     * @return
     * @param orgId
     */
    @Select(" select count(*) from t_event where operate_flag = '0' " +
            " and ( " +
            " ( device_id in (select device_id from t_device where org_id = #{orgId}) )" +
            " or (family_id in (select f.family_id from t_family f,t_member_family mf,t_member m " +
            "                   where f.family_id = mf.family_id " +
            "                         and mf.member_id = m.member_id " +
            "                         and m.user_id in (select user_id from sys_user u where org_id = #{orgId}))) " +
            " ) ")
    public long unHandleEventCountInOrg(@Param("orgId") Long orgId);

    /**
     * 未处理会员事件数
     * @return
     */
    @Select(" select count(*) from t_event where operate_flag = '0' and member_id is not null")
    public long unHandleMemberEventCount();

    /**
     * 根据级别聚合统计设备未处理的事件数
     * @param deviceId
     * @return
     */
    @Select(" select level,count(*) as count from t_event where operate_flag = '0' and device_id = #{deviceId} group by level")
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
     * 查询事件列表
     *
     * @param eventRequest 查询请求条件
     * @return 事件集合
     */
    public List<TEventList> selectEventInfo(EventRequest eventRequest);

}
