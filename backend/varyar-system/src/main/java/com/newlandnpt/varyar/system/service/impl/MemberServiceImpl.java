package com.newlandnpt.varyar.system.service.impl;

import java.util.List;

import com.newlandnpt.varyar.common.core.domain.entity.SysUser;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.MemberMapper;
import com.newlandnpt.varyar.system.domain.Member;
import com.newlandnpt.varyar.system.service.IMemberService;

/**
 * 会员Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class MemberServiceImpl implements IMemberService
{
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询会员
     * 
     * @param memberId 会员主键
     * @return 会员
     */
    @Override
    public Member selectMemberByMemberId(Long memberId)
    {
        return memberMapper.selectMemberByMemberId(memberId);
    }

    /**
     * 查询会员列表
     * 
     * @param member 会员
     * @return 会员
     */
    @Override
    public List<Member> selectMemberList(Member member)
    {
        return memberMapper.selectMemberList(member);
    }

    /**
     * 新增会员
     * 
     * @param member 会员
     * @return 结果
     */
    @Override
    public int insertMember(Member member)
    {
        member.setCreateTime(DateUtils.getNowDate());
        return memberMapper.insertMember(member);
    }

    /**
     * 修改会员
     * 
     * @param member 会员
     * @return 结果
     */
    @Override
    public int updateMember(Member member)
    {
        member.setUpdateTime(DateUtils.getNowDate());
        return memberMapper.updateMember(member);
    }

    /**
     * 批量删除会员
     * 
     * @param memberIds 需要删除的会员主键
     * @return 结果
     */
    @Override
    public int deleteMemberByMemberIds(Long[] memberIds)
    {
        return memberMapper.deleteMemberByMemberIds(memberIds);
    }

    /**
     * 删除会员信息
     * 
     * @param memberId 会员主键
     * @return 结果
     */
    @Override
    public int deleteMemberByMemberId(Long memberId)
    {
        return memberMapper.deleteMemberByMemberId(memberId);
    }

    @Override
    public int arrangeUserToMember(Long memberId, Long userId) {

        Member member = memberMapper.selectMemberByMemberId(memberId);
        if(member == null){
            throw new ServiceException("会员不存在");
        }
        SysUser user = sysUserMapper.selectUserById(userId);
        if(user == null){
            throw new ServiceException("运营不存在");
        }

        member.setUserId(user.getUserId());
        member.setUserName(user.getUserName());
        member.autoSetUpdateByLoginUser();
        return this.updateMember(member);
    }

    @Override
    public long total() {
        return memberMapper.total();
    }
}
