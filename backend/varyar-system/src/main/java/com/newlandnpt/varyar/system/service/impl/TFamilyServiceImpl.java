package com.newlandnpt.varyar.system.service.impl;

import java.util.List;
import com.newlandnpt.varyar.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TFamilyMapper;
import com.newlandnpt.varyar.system.domain.TFamily;
import com.newlandnpt.varyar.system.service.ITFamilyService;

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
    public int insertTFamily(TFamily tFamily)
    {
        tFamily.setCreateTime(DateUtils.getNowDate());
        return tFamilyMapper.insertTFamily(tFamily);
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
    public int deleteTFamilyByFamilyId(Long familyId)
    {
        return tFamilyMapper.deleteTFamilyByFamilyId(familyId);
    }
}
