package com.newlandnpt.varyar.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.newlandnpt.varyar.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.RoomMapper;
import com.newlandnpt.varyar.system.domain.TRoom;
import com.newlandnpt.varyar.system.service.IRoomService;

/**
 * 房间Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class RoomServiceImpl implements IRoomService
{
    @Autowired
    private RoomMapper roomMapper;

    /**
     * 查询房间
     * 
     * @param roomId 房间主键
     * @return 房间
     */
    @Override
    public TRoom selectTRoomByRoomId(Long roomId)
    {
        return roomMapper.selectTRoomByRoomId(roomId);
    }
    @Override
    public List<TRoom>  selectTRoomByFamilyId(Long familyId){
        return roomMapper.selectTRoomByFamilyId(familyId);
    }
    /**
     * 查询房间列表
     * 
     * @param tRoom 房间
     * @return 房间
     */
    @Override
    public List<TRoom> selectTRoomList(TRoom tRoom)
    {
        return roomMapper.selectTRoomList(tRoom);
    }

    /**
     * 新增房间
     * 
     * @param tRoom 房间
     * @return 结果
     */
    @Override
    public int insertTRoom(TRoom tRoom)
    {
        tRoom.setCreateTime(DateUtils.getNowDate());
        return roomMapper.insertTRoom(tRoom);
    }

    /**
     * 修改房间
     * 
     * @param tRoom 房间
     * @return 结果
     */
    @Override
    public int updateTRoom(TRoom tRoom)
    {
        tRoom.setUpdateTime(DateUtils.getNowDate());
        return roomMapper.updateTRoom(tRoom);
    }

    /**
     * 批量删除房间
     * 
     * @param roomIds 需要删除的房间主键
     * @return 结果
     */
    @Override
    public int deleteTRoomByRoomIds(Long[] roomIds)
    {
        return roomMapper.deleteTRoomByRoomIds(roomIds);
    }

    /**
     * 删除房间信息
     * 
     * @param roomId 房间主键
     * @return 结果
     */
    @Override
    public int deleteTRoomByRoomId(Long roomId)
    {
        return roomMapper.deleteTRoomByRoomId(roomId);
    }
}
