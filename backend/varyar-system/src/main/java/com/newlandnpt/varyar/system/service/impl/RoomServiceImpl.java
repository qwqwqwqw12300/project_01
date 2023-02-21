package com.newlandnpt.varyar.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.RoomMapper;
import com.newlandnpt.varyar.system.domain.TRoom;
import com.newlandnpt.varyar.system.service.IRoomService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private IDeviceService deviceService;

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
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteTRoomByRoomId(Long roomId)
    {
        TDevice device = new TDevice();
        device.setRoomId(roomId);
        List<TDevice> devices = deviceService.selectDeviceList(device);
        for (TDevice item : devices){
            item.setFamilyId(0L);
            item.setRoomId(0L);
            deviceService.updateDevice(item);
        }
        return roomMapper.deleteTRoomByRoomId(roomId);
    }
}
