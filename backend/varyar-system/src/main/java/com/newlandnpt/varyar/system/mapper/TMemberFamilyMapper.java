package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import java.util.Map;

import com.newlandnpt.varyar.system.domain.TMemberFamily;

/**
 * 会员和家庭关联Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface TMemberFamilyMapper 
{
    /**
     * 查询会员和家庭关联
     * 
     * @param memberFamilyId 会员和家庭关联主键
     * @return 会员和家庭关联
     */
    public TMemberFamily selectTMemberFamilyByMemberFamilyId(Long memberFamilyId);
    /**
     * 查询会员和家庭关联
     *
     * @param phone 会员和家庭关联主键
     * @return 会员和家庭关联
     */
    public List<TMemberFamily> selectTMemberFamilyByFamilyId(Map map);
    public List<TMemberFamily>  selectTMemberFamilyByShare(Map map);
    public List<TMemberFamily>  selectTMemberFamilyByshare(Long map);
    /**
     * 查询会员和家庭关联
     *
     * @param phone 会员和家庭关联主键
     * @return 会员和家庭关联
     */
    public List<TMemberFamily> selectTMemberFamilyByPhone(String phone);
    /**
     * 用 会员id 查询
     *
     * @param memberId
     * @return 会员和家庭关联
     */
    public List<TMemberFamily> selectTMemberFamilyByMember(Long memberId);
    /**被共享的家庭关系*/
    public List<TMemberFamily> selectTMemberFamilyByshare(Long memberId);

    /**
     * 用 会员id 查询  创建的家庭关系
     *
     * @param memberId
     * @return 会员和家庭关联
     */
    public List<TMemberFamily> selectTMemberFamilyByCreMember(Long memberId);
    /**
     * 用 会员id 查询
     *
     * @param memberId
     * @return 会员和家庭关联
     */
    public List<TMemberFamily> selectTMemberFamilyByCreMember(Long memberId);
    /**
     * 查询会员和家庭关联列表
     * 
     * @param tMemberFamily 会员和家庭关联
     * @return 会员和家庭关联集合
     */
    public List<TMemberFamily> selectTMemberFamilyList(TMemberFamily tMemberFamily);

    /**
     * 新增会员和家庭关联
     * 
     * @param tMemberFamily 会员和家庭关联
     * @return 结果
     */
    public int insertTMemberFamily(TMemberFamily tMemberFamily);

    /**
     * 修改会员和家庭关联
     * 
     * @param tMemberFamily 会员和家庭关联
     * @return 结果
     */
    public int updateTMemberFamily(TMemberFamily tMemberFamily);

    /**
     * 删除会员和家庭关联
     * 
     * @param memberFamilyId 会员和家庭关联主键
     * @return 结果
     */
    public int deleteTMemberFamilyByMemberFamilyId(Long memberFamilyId);

    /**
     * 批量删除会员和家庭关联
     * 
     * @param memberFamilyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMemberFamilyByMemberFamilyIds(Long[] memberFamilyIds);
}
