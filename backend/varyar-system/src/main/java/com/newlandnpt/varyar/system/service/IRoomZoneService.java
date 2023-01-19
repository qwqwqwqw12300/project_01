package com.newlandnpt.varyar.system.service;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TRoomZone;

/**
 * 子区域模块生成Service接口
 * 
 * @author ruoyi
 * @date 2023-01-05
 */
public interface IRoomZoneService
{
    /**
     * 查询子区域模块生成
     * 
     * @param roomZoneId 子区域模块生成主键
     * @return 子区域模块生成
     */
    public TRoomZone selectTRoomZoneByRoomZoneId(Long roomZoneId);

    /**
     * 查询子区域模块生成列表
     * 
     * @param tRoomZone 子区域模块生成
     * @return 子区域模块生成集合
     */
    public List<TRoomZone> selectTRoomZoneList(TRoomZone tRoomZone);
    public List<TRoomZone> selectTRoomZoneByDeviceId(Long deviceId);

    /**
     * 新增子区域模块生成
     * 
     * @param tRoomZone 子区域模块生成
     * @return 结果
     */
    public int insertTRoomZone(TRoomZone tRoomZone);

    /**
     * 修改子区域模块生成
     * 
     * @param tRoomZone 子区域模块生成
     * @return 结果
     */
    public int updateTRoomZone(TRoomZone tRoomZone);

    /**
     * 批量删除子区域模块生成
     * 
     * @param roomZoneIds 需要删除的子区域模块生成主键集合
     * @return 结果
     */
    public int deleteTRoomZoneByRoomZoneIds(Long[] roomZoneIds);

    /**
     * 删除子区域模块生成信息
     * 
     * @param roomZoneId 子区域模块生成主键
     * @return 结果
     */
    public int deleteTRoomZoneByRoomZoneId(Long roomZoneId);
}
