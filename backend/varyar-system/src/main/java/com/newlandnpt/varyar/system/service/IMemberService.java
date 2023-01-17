package com.newlandnpt.varyar.system.service;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TMember;

/**
 * 会员Service接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface IMemberService
{
    /**
     * 查询会员
     * 
     * @param memberId 会员主键
     * @return 会员
     */
    public TMember selectMemberByMemberId(Long memberId);
    /**
     * 查询会员
     *
     * @param phone 手机号
     * @return 会员
     */
    public TMember selectMemberByPhone(String phone);

    /**
     * 查询会员列表
     * 
     * @param member 会员
     * @return 会员集合
     */
    public List<TMember> selectMemberList(TMember member);

    /**
     * 新增会员
     * 
     * @param member 会员
     * @return 结果
     */
    public int insertMember(TMember member);

    /**
     * 修改会员
     * 
     * @param member 会员
     * @return 结果
     */
    public int updateMember(TMember member);

    /**
     * 批量删除会员
     * 
     * @param memberIds 需要删除的会员主键集合
     * @return 结果
     */
    public int deleteMemberByMemberIds(Long[] memberIds);

    /**
     * 删除会员信息
     * 
     * @param memberId 会员主键
     * @return 结果
     */
    public int deleteMemberByMemberId(Long memberId);

    /**
     * 分配运营给会员
     * @param memberId
     * @param userId
     * @return
     */
    public int arrangeUserToMember(Long memberId,Long userId);

    /**
     * 会员总数
     * @return
     * @param member
     */
    public long total(TMember member);
    /**
     * 未分配会员数
     * @return
     * @param member
     */
    public long notArrangeMemberCount(TMember member);
}
