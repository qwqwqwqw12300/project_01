package com.newlandnpt.varyar.system.service.impl;

import java.util.List;
import com.newlandnpt.varyar.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TMemberContactsMapper;
import com.newlandnpt.varyar.system.domain.TMemberContacts;
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
    private TMemberContactsMapper memberContactsMapper;

    /**
     * 查询会员联络人
     * 
     * @param memberContactsId 会员联络人主键
     * @return 会员联络人
     */
    @Override
    public TMemberContacts selectMemberContactsByMemberContactsId(Long memberContactsId)
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
    public List<TMemberContacts> selectMemberContactsList(TMemberContacts memberContacts)
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
    public int insertMemberContacts(TMemberContacts memberContacts)
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
    public int updateMemberContacts(TMemberContacts memberContacts)
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
