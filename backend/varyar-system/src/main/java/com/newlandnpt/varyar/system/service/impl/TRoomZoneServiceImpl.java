package com.newlandnpt.varyar.system.service.impl;

import java.util.List;

import com.newlandnpt.varyar.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.RoomZoneMapper;
import com.newlandnpt.varyar.system.domain.TRoomZone;
import com.newlandnpt.varyar.system.service.IRoomZoneService;

/**
 * 子区域模块生成Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-01-05
 */
@Service
public class TRoomZoneServiceImpl implements IRoomZoneService
{
    @Autowired
    private RoomZoneMapper roomZoneMapper;

    /**
     * 查询子区域模块生成
     * 
     * @param roomZoneId 子区域模块生成主键
     * @return 子区域模块生成
     */
    @Override
    public TRoomZone selectTRoomZoneByRoomZoneId(Long roomZoneId)
    {
        return roomZoneMapper.selectTRoomZoneByRoomZoneId(roomZoneId);
    }

    /**
     * 查询子区域模块生成列表
     * 
     * @param tRoomZone 子区域模块生成
     * @return 子区域模块生成
     */
    @Override
    public List<TRoomZone> selectTRoomZoneList(TRoomZone tRoomZone)
    {
        return roomZoneMapper.selectTRoomZoneList(tRoomZone);
    }

    /**
     * 新增子区域模块生成
     * 
     * @param tRoomZone 子区域模块生成
     * @return 结果
     */
    @Override
    public int insertTRoomZone(TRoomZone tRoomZone)
    {
        tRoomZone.setCreateTime(DateUtils.getNowDate());
        return roomZoneMapper.insertTRoomZone(tRoomZone);
    }

    /**
     * 修改子区域模块生成
     * 
     * @param tRoomZone 子区域模块生成
     * @return 结果
     */
    @Override
    public int updateTRoomZone(TRoomZone tRoomZone)
    {
        tRoomZone.setUpdateTime(DateUtils.getNowDate());
        return roomZoneMapper.updateTRoomZone(tRoomZone);
    }

    /**
     * 批量删除子区域模块生成
     * 
     * @param roomZoneIds 需要删除的子区域模块生成主键
     * @return 结果
     */
    @Override
    public int deleteTRoomZoneByRoomZoneIds(Long[] roomZoneIds)
    {
        return roomZoneMapper.deleteTRoomZoneByRoomZoneIds(roomZoneIds);
    }

    /**
     * 删除子区域模块生成信息
     * 
     * @param roomZoneId 子区域模块生成主键
     * @return 结果
     */
    @Override
    public int deleteTRoomZoneByRoomZoneId(Long roomZoneId)
    {
        return roomZoneMapper.deleteTRoomZoneByRoomZoneId(roomZoneId);
    }
}
