package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TMember;

/**
 * 会员Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface TMemberMapper 
{
    /**
     * 查询会员
     * 
     * @param memberId 会员主键
     * @return 会员
     */
    public TMember selectTMemberByMemberId(Long memberId);

    /**
     * 查询会员列表
     * 
     * @param tMember 会员
     * @return 会员集合
     */
    public List<TMember> selectTMemberList(TMember tMember);

    /**
     * 新增会员
     * 
     * @param tMember 会员
     * @return 结果
     */
    public int insertTMember(TMember tMember);

    /**
     * 修改会员
     * 
     * @param tMember 会员
     * @return 结果
     */
    public int updateTMember(TMember tMember);

    /**
     * 删除会员
     * 
     * @param memberId 会员主键
     * @return 结果
     */
    public int deleteTMemberByMemberId(Long memberId);

    /**
     * 批量删除会员
     * 
     * @param memberIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMemberByMemberIds(Long[] memberIds);
}
