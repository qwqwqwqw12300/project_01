package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TFamily;

/**
 * 家庭Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface TFamilyMapper
{
    /**
     * 查询家庭
     * 
     * @param familyId 家庭主键
     * @return 家庭
     */
    public TFamily selectTFamilyByFamilyId(Long familyId);

    /**
     * 查询家庭列表
     * 
     * @param tFamily 家庭
     * @return 家庭集合
     */
    public List<TFamily> selectTFamilyList(TFamily tFamily);

    /**
     * 查询会员家庭列表
     *
     * @param memberId 会员id
     * @return 家庭集合
     */
    public List<TFamily> selectMembersFamilyList(Long memberId);
    /**
     * 新增家庭
     * 
     * @param tFamily 家庭
     * @return 结果
     */
    public int insertTFamily(TFamily tFamily);

    /**
     * 修改家庭
     * 
     * @param tFamily 家庭
     * @return 结果
     */
    public int updateTFamily(TFamily tFamily);

    /**
     * 删除家庭
     * 
     * @param familyId 家庭主键
     * @return 结果
     */
    public int deleteTFamilyByFamilyId(Long familyId);

    /**
     * 批量删除家庭
     * 
     * @param familyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTFamilyByFamilyIds(Long[] familyIds);
}
