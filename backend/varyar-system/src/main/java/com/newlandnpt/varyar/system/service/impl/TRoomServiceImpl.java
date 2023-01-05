package com.newlandnpt.varyar.system.service.impl;

import java.util.List;
import com.newlandnpt.varyar.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TRoomMapper;
import com.newlandnpt.varyar.system.domain.TRoom;
import com.newlandnpt.varyar.system.service.IRoomService;

/**
 * 房间Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class TRoomServiceImpl implements IRoomService
{
    @Autowired
    private TRoomMapper tRoomMapper;

    /**
     * 查询房间
     * 
     * @param roomId 房间主键
     * @return 房间
     */
    @Override
    public TRoom selectTRoomByRoomId(Long roomId)
    {
        return tRoomMapper.selectTRoomByRoomId(roomId);
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
        return tRoomMapper.selectTRoomList(tRoom);
    }
    public List<TRoom> selectTRoomList(Long memberId){
        return tRoomMapper.selectByMemberIdList(memberId);
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
        return tRoomMapper.insertTRoom(tRoom);
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
        return tRoomMapper.updateTRoom(tRoom);
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
        return tRoomMapper.deleteTRoomByRoomIds(roomIds);
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
        return tRoomMapper.deleteTRoomByRoomId(roomId);
    }
}
