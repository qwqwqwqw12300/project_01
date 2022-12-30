package com.newlandnpt.varyar.system.service.impl;

import java.util.List;
import com.newlandnpt.varyar.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.MemberContactsMapper;
import com.newlandnpt.varyar.system.domain.MemberContacts;
import com.newlandnpt.varyar.system.service.IMemberContactsService;

/**
 * 会员联络人Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class MemberContactsServiceImpl implements IMemberContactsService
{
    @Autowired
    private MemberContactsMapper memberContactsMapper;

    /**
     * 查询会员联络人
     * 
     * @param memberContactsId 会员联络人主键
     * @return 会员联络人
     */
    @Override
    public MemberContacts selectMemberContactsByMemberContactsId(Long memberContactsId)
    {
        return memberContactsMapper.selectMemberContactsByMemberContactsId(memberContactsId);
    }

    /**
     * 查询会员联络人列表
     * 
     * @param memberContacts 会员联络人
     * @return 会员联络人
     */
    @Override
    public List<MemberContacts> selectMemberContactsList(MemberContacts memberContacts)
    {
        return memberContactsMapper.selectMemberContactsList(memberContacts);
    }

    /**
     * 新增会员联络人
     * 
     * @param memberContacts 会员联络人
     * @return 结果
     */
    @Override
    public int insertMemberContacts(MemberContacts memberContacts)
    {
        memberContacts.setCreateTime(DateUtils.getNowDate());
        return memberContactsMapper.insertMemberContacts(memberContacts);
    }

    /**
     * 修改会员联络人
     * 
     * @param memberContacts 会员联络人
     * @return 结果
     */
    @Override
    public int updateMemberContacts(MemberContacts memberContacts)
    {
        memberContacts.setUpdateTime(DateUtils.getNowDate());
        return memberContactsMapper.updateMemberContacts(memberContacts);
    }

    /**
     * 批量删除会员联络人
     * 
     * @param memberContactsIds 需要删除的会员联络人主键
     * @return 结果
     */
    @Override
    public int deleteMemberContactsByMemberContactsIds(Long[] memberContactsIds)
    {
        return memberContactsMapper.deleteMemberContactsByMemberContactsIds(memberContactsIds);
    }

    /**
     * 删除会员联络人信息
     * 
     * @param memberContactsId 会员联络人主键
     * @return 结果
     */
    @Override
    public int deleteMemberContactsByMemberContactsId(Long memberContactsId)
    {
        return memberContactsMapper.deleteMemberContactsByMemberContactsId(memberContactsId);
    }
}
