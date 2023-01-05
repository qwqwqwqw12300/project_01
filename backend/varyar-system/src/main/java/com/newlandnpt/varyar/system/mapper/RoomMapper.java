package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import java.util.Map;

import com.newlandnpt.varyar.system.domain.TRoom;

/**
 * 房间Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface RoomMapper
{
    /**
     * 查询房间
     * 
     * @param roomId 房间主键
     * @return 房间
     */
    public TRoom selectTRoomByRoomId(Long roomId);

    /**
     * 查询房间列表
     * 
     * @param tRoom 房间
     * @return 房间集合
     */
    public List<TRoom> selectTRoomList(TRoom tRoom);
    /**
     * 查询房间列表
     *
     * @param tRoom 房间
     * @return 房间集合
     */
    public List<TRoom> selectByMemberIdList(Map map);
    /**
     * 新增房间
     * 
     * @param tRoom 房间
     * @return 结果
     */
    public int insertTRoom(TRoom tRoom);

    /**
     * 修改房间
     * 
     * @param tRoom 房间
     * @return 结果
     */
    public int updateTRoom(TRoom tRoom);

    /**
     * 删除房间
     * 
     * @param roomId 房间主键
     * @return 结果
     */
    public int deleteTRoomByRoomId(Long roomId);

    /**
     * 批量删除房间
     * 
     * @param roomIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTRoomByRoomIds(Long[] roomIds);
}
