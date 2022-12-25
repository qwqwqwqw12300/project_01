package com.newlandnpt.varyar.system.service.impl;

import java.util.List;
import com.newlandnpt.varyar.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TMembercontactsMapper;
import com.newlandnpt.varyar.system.domain.TMemberContacts;
import com.newlandnpt.varyar.system.service.ITMemberContactsService;

/**
 * 会员联络人Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class TMemberContactsServiceImpl implements ITMemberContactsService
{
    @Autowired
    private TMembercontactsMapper tMembercontactsMapper;

    /**
     * 查询会员联络人
     * 
     * @param memberContactsId 会员联络人主键
     * @return 会员联络人
     */
    @Override
    public TMemberContacts selectTMemberContactsByMemberContactsId(Long memberContactsId)
    {
        return tMembercontactsMapper.selectTMembercontactsByMembercontactsId(memberContactsId);
    }

    /**
     * 查询会员联络人列表
     * 
     * @param tMemberContacts 会员联络人
     * @return 会员联络人
     */
    @Override
    public List<TMemberContacts> selectTMemberContactsList(TMemberContacts tMemberContacts)
    {
        return tMembercontactsMapper.selectTMembercontactsList(tMemberContacts);
    }

    /**
     * 新增会员联络人
     * 
     * @param tMemberContacts 会员联络人
     * @return 结果
     */
    @Override
    public int insertTMemberContacts(TMemberContacts tMemberContacts)
    {
        tMemberContacts.setCreateTime(DateUtils.getNowDate());
        return tMembercontactsMapper.insertTMembercontacts(tMemberContacts);
    }

    /**
     * 修改会员联络人
     * 
     * @param tMemberContacts 会员联络人
     * @return 结果
     */
    @Override
    public int updateTMemberContacts(TMemberContacts tMemberContacts)
    {
        tMemberContacts.setUpdateTime(DateUtils.getNowDate());
        return tMembercontactsMapper.updateTMembercontacts(tMemberContacts);
    }

    /**
     * 批量删除会员联络人
     * 
     * @param memberContactsIds 需要删除的会员联络人主键
     * @return 结果
     */
    @Override
    public int deleteTMemberContactsByMemberContactsIds(Long[] memberContactsIds)
    {
        return tMembercontactsMapper.deleteTMembercontactsByMembercontactsIds(memberContactsIds);
    }

    /**
     * 删除会员联络人信息
     * 
     * @param memberContactsId 会员联络人主键
     * @return 结果
     */
    @Override
    public int deleteTMemberContactsByMemberContactsId(Long memberContactsId)
    {
        return tMembercontactsMapper.deleteTMembercontactsByMembercontactsId(memberContactsId);
    }
}
