package com.newlandnpt.varyar.system.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.domain.TMemberFamily;
import com.newlandnpt.varyar.system.domain.TRoom;
import com.newlandnpt.varyar.system.service.IDeviceService;
import com.newlandnpt.varyar.system.service.IMemberFamilyService;
import com.newlandnpt.varyar.system.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TFamilyMapper;
import com.newlandnpt.varyar.system.domain.TFamily;
import com.newlandnpt.varyar.system.service.IFamilyService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 家庭Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class FamilyServiceImpl implements IFamilyService
{
    @Autowired
    private TFamilyMapper familyMapper;
    @Autowired
    private IMemberFamilyService iMemberFamilyService;
    @Autowired
    private IRoomService iRoomService;
    @Autowired
    private IDeviceService iDeviceService;

    /**
     * 查询家庭
     * 
     * @param familyId 家庭主键
     * @return 家庭
     */
    @Override
    public TFamily selectTFamilyByFamilyId(Long familyId)
    {
        return familyMapper.selectTFamilyByFamilyId(familyId);
    }

    /**
     * 查询家庭列表
     * 
     * @param tFamily 家庭
     * @return 家庭
     */
    @Override
    public List<TFamily> selectTFamilyList(TFamily tFamily)
    {
        return familyMapper.selectTFamilyList(tFamily);
    }

    @Override
    public List<TFamily> selectMembersFamilyList(Long memberId) {
        return familyMapper.selectMembersFamilyList(memberId);
    }
    /**
     * 新增家庭
     * 
     * @param tFamily 家庭
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public int insertTFamily(TFamily tFamily,Long memberId)
    {
        tFamily.setCreateTime(DateUtils.getNowDate());
        familyMapper.insertTFamily(tFamily);
        //添加 会员和家庭关联信息
        TMemberFamily tMemberFamily = new TMemberFamily();
        tMemberFamily.setFamilyId(tFamily.getFamilyId());
        tMemberFamily.setMemberId(memberId);
        tMemberFamily.setCreateMemberId(memberId);
        tMemberFamily.setDelFlag("0");
        tMemberFamily.setShareFlag("2");
        return iMemberFamilyService.insertTMemberFamily(tMemberFamily);
    }

    /**
     * 新增家庭
     *
     * @param tFamily 家庭
     * @return 结果
     */
    @Override
    public int insertTFamily(TFamily tFamily)
    {
        tFamily.setCreateTime(DateUtils.getNowDate());
        return familyMapper.insertTFamily(tFamily);
    }

    /**
     * 修改家庭
     * 
     * @param tFamily 家庭
     * @return 结果
     */
    @Override

    public int updateTFamily(TFamily tFamily)
    {
        tFamily.setUpdateTime(DateUtils.getNowDate());
        return familyMapper.updateTFamily(tFamily);
    }

    /**
     * 批量删除家庭
     * 
     * @param familyIds 需要删除的家庭主键
     * @return 结果
     */
    @Override
    public int deleteTFamilyByFamilyIds(Long[] familyIds)
    {
        return familyMapper.deleteTFamilyByFamilyIds(familyIds);
    }

    /**
     * 删除家庭信息
     * 
     * @param familyId 家庭主键
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public int deleteTFamilyByFamilyId(Long familyId,Long memberId){
        //删除对应的我的家庭信息
        familyMapper.deleteTFamilyByFamilyId(familyId);
        TMemberFamily tfcond = new TMemberFamily();
        tfcond.setCreateMemberId(memberId);
        tfcond.setFamilyId(familyId);
        List<TMemberFamily>  tMemberFamilies = iMemberFamilyService.selectTMemberFamilyList(tfcond);
        for (TMemberFamily item : tMemberFamilies){
            iMemberFamilyService.deleteTMemberFamilyByMemberFamilyId(item.getMemberFamilyId());
        }
        //查询对应家庭下的所有设备并解除绑定
        TDevice deviceCond = new TDevice();
        deviceCond.setFamilyId(familyId);
        List<TDevice> TDevices = iDeviceService.selectDeviceList(deviceCond);
        for (TDevice item : TDevices){
            item.setFamilyId(0L);
            item.setRoomId(0L);
            iDeviceService.updateDevice(item);
        }
        Set<Long> roomIdSet = new HashSet<>();
        TRoom tRoom = new TRoom();
        tRoom.setFamilyId(familyId);
        //查询对应家庭下的房间信息
        List<TRoom> list = iRoomService.selectTRoomList(tRoom);
        if(list.size()==0){
            return 1;
        }
        for (TRoom item : list){
            roomIdSet.add(item.getRoomId());
        }
        Long[] roomIds = new Long[roomIdSet.size()];
        roomIdSet.toArray(roomIds);
        //删除对应的房间信息
        return iRoomService.deleteTRoomByRoomIds(roomIds);
    }
}
