package com.newlandnpt.varyar.system.service.impl;

import java.util.List;

import com.newlandnpt.varyar.common.annotation.DataScope;
import com.newlandnpt.varyar.common.core.domain.entity.SysUser;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TMemberMapper;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.service.IMemberService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    private TMemberMapper memberMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询会员
     * 
     * @param memberId 会员主键
     * @return 会员
     */
    @Override
    public TMember selectMemberByMemberId(Long memberId)
    {
        return memberMapper.selectMemberByMemberId(memberId);
    }
    /**
     * 查询会员
     *
     * @param phone 手机号
     * @return 会员
     */
    public TMember selectMemberByPhone(String phone){
        return memberMapper.selectMemberByPhone(phone);
    }

    /**
     * 查询会员列表
     * 
     * @param member 会员
     * @return 会员
     */
    @Override
    @DataScope(orgAlias = "m")
    public List<TMember> selectMemberList(TMember member)
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
    public int insertMember(TMember member)
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
    public int updateMember(TMember member)
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

        TMember member = memberMapper.selectMemberByMemberId(memberId);
        if(member == null){
            throw new ServiceException("会员不存在");
        }
        if(userId!=null){
            SysUser user = sysUserMapper.selectUserById(userId);
            if(user == null){
                throw new ServiceException("运营不存在");
            }
            //分配运营时默认将会员分给下属机构
            member.setOrgId(user.getOrgId());
            member.setDistributeFlag("1");
            member.setUserId(user.getUserId());
            member.setUserName(user.getUserName());
        }else{
            member.setDistributeFlag("0");
            member.setUserId(null);
            member.setUserName(null);
        }
        member.autoSetUpdateByLoginUser();
        return this.updateMember(member);
    }

    @Override
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public int arrangeMembers(Long[] memberIds, Long userId) {
        int sum = 0;
        if(memberIds!=null){
            for(long memberId:memberIds){
                arrangeUserToMember(memberId,userId);
                sum++;
            }
        }
        return sum;
    }

    @Override
    public long total(TMember member) {
        return memberMapper.total(member);
    }

    @Override
    @DataScope(orgAlias = "m")
    public long notArrangeMemberCount(TMember member) {
        return memberMapper.notArrangeMemberCount(member);
    }
}
