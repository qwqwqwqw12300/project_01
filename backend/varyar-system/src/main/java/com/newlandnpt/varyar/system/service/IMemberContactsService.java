package com.newlandnpt.varyar.system.service;

import java.util.List;
import com.newlandnpt.varyar.system.domain.MemberContacts;

/**
 * 会员联络人Service接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface IMemberContactsService
{
    /**
     * 查询会员联络人
     * 
     * @param memberContactsId 会员联络人主键
     * @return 会员联络人
     */
    public MemberContacts selectMemberContactsByMemberContactsId(Long memberContactsId);

    /**
     * 查询会员联络人列表
     * 
     * @param memberContacts 会员联络人
     * @return 会员联络人集合
     */
    public List<MemberContacts> selectMemberContactsList(MemberContacts memberContacts);

    /**
     * 新增会员联络人
     * 
     * @param memberContacts 会员联络人
     * @return 结果
     */
    public int insertMemberContacts(MemberContacts memberContacts);

    /**
     * 修改会员联络人
     * 
     * @param memberContacts 会员联络人
     * @return 结果
     */
    public int updateMemberContacts(MemberContacts memberContacts);

    /**
     * 批量删除会员联络人
     * 
     * @param memberContactsIds 需要删除的会员联络人主键集合
     * @return 结果
     */
    public int deleteMemberContactsByMemberContactsIds(Long[] memberContactsIds);

    /**
     * 删除会员联络人信息
     * 
     * @param memberContactsId 会员联络人主键
     * @return 结果
     */
    public int deleteMemberContactsByMemberContactsId(Long memberContactsId);
}
