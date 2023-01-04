package com.newlandnpt.varyar.system.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.system.domain.TMemberFamily;
import com.newlandnpt.varyar.system.domain.TRoom;
import com.newlandnpt.varyar.system.service.ITMemberFamilyService;
import com.newlandnpt.varyar.system.service.ITRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TFamilyMapper;
import com.newlandnpt.varyar.system.domain.TFamily;
import com.newlandnpt.varyar.system.service.ITFamilyService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 家庭Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class TFamilyServiceImpl implements ITFamilyService 
{
    @Autowired
    private TFamilyMapper tFamilyMapper;
    @Autowired
    private ITMemberFamilyService itMemberFamilyService;
    @Autowired
    private ITRoomService itRoomService;

    /**
     * 查询家庭
     * 
     * @param familyId 家庭主键
     * @return 家庭
     */
    @Override
    public TFamily selectTFamilyByFamilyId(Long familyId)
    {
        return tFamilyMapper.selectTFamilyByFamilyId(familyId);
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
        return tFamilyMapper.selectTFamilyList(tFamily);
    }

    @Override
    public List<TFamily> selectMembersFamilyList(Long memberId) {
        return tFamilyMapper.selectMembersFamilyList(memberId);
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
        tFamilyMapper.insertTFamily(tFamily);
        //添加 会员和家庭关联信息
        TMemberFamily tMemberFamily = new TMemberFamily();
        tMemberFamily.setFamilyId(tFamily.getFamilyId());
        tMemberFamily.setMemberId(memberId);
        tMemberFamily.setCreateMemberId(memberId);
        tMemberFamily.setDelFlag("0");
        return itMemberFamilyService.insertTMemberFamily(tMemberFamily);
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
        return tFamilyMapper.updateTFamily(tFamily);
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
        return tFamilyMapper.deleteTFamilyByFamilyIds(familyIds);
    }

    /**
     * 删除家庭信息
     * 
     * @param familyId 家庭主键
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public int deleteTFamilyByFamilyId(Long familyId)
    {   //删除对应的我的家庭信息
        tFamilyMapper.deleteTFamilyByFamilyId(familyId);
        Set<Long> roomIdSet = new HashSet<>();
        TRoom tRoom = new TRoom();
        tRoom.setFamilyId(familyId);
        //查询对应家庭下的房间信息
        List<TRoom> list = itRoomService.selectTRoomList(tRoom);
        for (TRoom item : list){
            roomIdSet.add(item.getRoomId());
        }
        Long[] roomIds = new Long[roomIdSet.size()];
        roomIdSet.toArray(roomIds);
        //删除对应的房间信息
        return itRoomService.deleteTRoomByRoomIds(roomIds);
    }
}
