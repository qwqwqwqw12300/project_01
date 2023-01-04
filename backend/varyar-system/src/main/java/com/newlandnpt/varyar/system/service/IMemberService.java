package com.newlandnpt.varyar.system.service;

import java.util.List;
import com.newlandnpt.varyar.system.domain.Member;

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
    public Member selectMemberByMemberId(Long memberId);

    /**
     * 查询会员列表
     * 
     * @param member 会员
     * @return 会员集合
     */
    public List<Member> selectMemberList(Member member);

    /**
     * 新增会员
     * 
     * @param member 会员
     * @return 结果
     */
    public int insertMember(Member member);

    /**
     * 修改会员
     * 
     * @param member 会员
     * @return 结果
     */
    public int updateMember(Member member);

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
     * 根据机构id统计机构服务会员数量
     * @return
     */
    public long total();
}
