package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TMemberContacts;

/**
 * 会员联络人Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface TMemberContactsMapper
{
    /**
     * 查询会员联络人
     * 
     * @param memberContactsId 会员联络人主键
     * @return 会员联络人
     */
    public TMemberContacts selectMemberContactsByMemberContactsId(Long memberContactsId);

    /**
     * 查询会员联络人列表
     * 
     * @param memberContacts 会员联络人
     * @return 会员联络人集合
     */
    public List<TMemberContacts> selectMemberContactsList(TMemberContacts memberContacts);

    /**
     * 新增会员联络人
     * 
     * @param memberContacts 会员联络人
     * @return 结果
     */
    public int insertMemberContacts(TMemberContacts memberContacts);

    /**
     * 修改会员联络人
     * 
     * @param memberContacts 会员联络人
     * @return 结果
     */
    public int updateMemberContacts(TMemberContacts memberContacts);

    /**
     * 删除会员联络人
     * 
     * @param memberContactsId 会员联络人主键
     * @return 结果
     */
    public int deleteMemberContactsByMemberContactsId(Long memberContactsId);

    /**
     * 批量删除会员联络人
     * 
     * @param memberContactsIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMemberContactsByMemberContactsIds(Long[] memberContactsIds);
}
