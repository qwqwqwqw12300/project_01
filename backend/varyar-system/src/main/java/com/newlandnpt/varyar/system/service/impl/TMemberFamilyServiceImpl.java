package com.newlandnpt.varyar.system.service.impl;

import java.util.List;
import com.newlandnpt.varyar.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TMemberFamilyMapper;
import com.newlandnpt.varyar.system.domain.TMemberFamily;
import com.newlandnpt.varyar.system.service.ITMemberFamilyService;

/**
 * 会员和家庭关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class TMemberFamilyServiceImpl implements ITMemberFamilyService 
{
    @Autowired
    private TMemberFamilyMapper tMemberFamilyMapper;

    /**
     * 查询会员和家庭关联
     * 
     * @param memberFamilyId 会员和家庭关联主键
     * @return 会员和家庭关联
     */
    @Override
    public TMemberFamily selectTMemberFamilyByMemberFamilyId(Long memberFamilyId)
    {
        return tMemberFamilyMapper.selectTMemberFamilyByMemberFamilyId(memberFamilyId);
    }

    /**
     * 查询会员和家庭关联列表
     * 
     * @param tMemberFamily 会员和家庭关联
     * @return 会员和家庭关联
     */
    @Override
    public List<TMemberFamily> selectTMemberFamilyList(TMemberFamily tMemberFamily)
    {
        return tMemberFamilyMapper.selectTMemberFamilyList(tMemberFamily);
    }

    /**
     * 新增会员和家庭关联
     * 
     * @param tMemberFamily 会员和家庭关联
     * @return 结果
     */
    @Override
    public int insertTMemberFamily(TMemberFamily tMemberFamily)
    {
        tMemberFamily.setCreateTime(DateUtils.getNowDate());
        return tMemberFamilyMapper.insertTMemberFamily(tMemberFamily);
    }

    /**
     * 修改会员和家庭关联
     * 
     * @param tMemberFamily 会员和家庭关联
     * @return 结果
     */
    @Override
    public int updateTMemberFamily(TMemberFamily tMemberFamily)
    {
        tMemberFamily.setUpdateTime(DateUtils.getNowDate());
        return tMemberFamilyMapper.updateTMemberFamily(tMemberFamily);
    }

    /**
     * 批量删除会员和家庭关联
     * 
     * @param memberFamilyIds 需要删除的会员和家庭关联主键
     * @return 结果
     */
    @Override
    public int deleteTMemberFamilyByMemberFamilyIds(Long[] memberFamilyIds)
    {
        return tMemberFamilyMapper.deleteTMemberFamilyByMemberFamilyIds(memberFamilyIds);
    }

    /**
     * 删除会员和家庭关联信息
     * 
     * @param memberFamilyId 会员和家庭关联主键
     * @return 结果
     */
    @Override
    public int deleteTMemberFamilyByMemberFamilyId(Long memberFamilyId)
    {
        return tMemberFamilyMapper.deleteTMemberFamilyByMemberFamilyId(memberFamilyId);
    }
}
