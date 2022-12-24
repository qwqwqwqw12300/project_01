package com.newlandnpt.varyar.system.service.impl;

import java.util.List;
import com.newlandnpt.varyar.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TMemberMapper;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.service.ITMemberService;

/**
 * 会员Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class TMemberServiceImpl implements ITMemberService 
{
    @Autowired
    private TMemberMapper tMemberMapper;

    /**
     * 查询会员
     * 
     * @param memberId 会员主键
     * @return 会员
     */
    @Override
    public TMember selectTMemberByMemberId(Long memberId)
    {
        return tMemberMapper.selectTMemberByMemberId(memberId);
    }

    /**
     * 查询会员列表
     * 
     * @param tMember 会员
     * @return 会员
     */
    @Override
    public List<TMember> selectTMemberList(TMember tMember)
    {
        return tMemberMapper.selectTMemberList(tMember);
    }

    /**
     * 新增会员
     * 
     * @param tMember 会员
     * @return 结果
     */
    @Override
    public int insertTMember(TMember tMember)
    {
        tMember.setCreateTime(DateUtils.getNowDate());
        return tMemberMapper.insertTMember(tMember);
    }

    /**
     * 修改会员
     * 
     * @param tMember 会员
     * @return 结果
     */
    @Override
    public int updateTMember(TMember tMember)
    {
        tMember.setUpdateTime(DateUtils.getNowDate());
        return tMemberMapper.updateTMember(tMember);
    }

    /**
     * 批量删除会员
     * 
     * @param memberIds 需要删除的会员主键
     * @return 结果
     */
    @Override
    public int deleteTMemberByMemberIds(Long[] memberIds)
    {
        return tMemberMapper.deleteTMemberByMemberIds(memberIds);
    }

    /**
     * 删除会员信息
     * 
     * @param memberId 会员主键
     * @return 结果
     */
    @Override
    public int deleteTMemberByMemberId(Long memberId)
    {
        return tMemberMapper.deleteTMemberByMemberId(memberId);
    }
}
