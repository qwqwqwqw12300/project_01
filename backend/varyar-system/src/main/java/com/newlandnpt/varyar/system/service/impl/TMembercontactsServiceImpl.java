package com.newlandnpt.varyar.system.service.impl;

import java.util.List;
import com.newlandnpt.varyar.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TMembercontactsMapper;
import com.newlandnpt.varyar.system.domain.TMembercontacts;
import com.newlandnpt.varyar.system.service.ITMembercontactsService;

/**
 * 会员联络人Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class TMembercontactsServiceImpl implements ITMembercontactsService 
{
    @Autowired
    private TMembercontactsMapper tMembercontactsMapper;

    /**
     * 查询会员联络人
     * 
     * @param membercontactsId 会员联络人主键
     * @return 会员联络人
     */
    @Override
    public TMembercontacts selectTMembercontactsByMembercontactsId(Long membercontactsId)
    {
        return tMembercontactsMapper.selectTMembercontactsByMembercontactsId(membercontactsId);
    }

    /**
     * 查询会员联络人列表
     * 
     * @param tMembercontacts 会员联络人
     * @return 会员联络人
     */
    @Override
    public List<TMembercontacts> selectTMembercontactsList(TMembercontacts tMembercontacts)
    {
        return tMembercontactsMapper.selectTMembercontactsList(tMembercontacts);
    }

    /**
     * 新增会员联络人
     * 
     * @param tMembercontacts 会员联络人
     * @return 结果
     */
    @Override
    public int insertTMembercontacts(TMembercontacts tMembercontacts)
    {
        tMembercontacts.setCreateTime(DateUtils.getNowDate());
        return tMembercontactsMapper.insertTMembercontacts(tMembercontacts);
    }

    /**
     * 修改会员联络人
     * 
     * @param tMembercontacts 会员联络人
     * @return 结果
     */
    @Override
    public int updateTMembercontacts(TMembercontacts tMembercontacts)
    {
        tMembercontacts.setUpdateTime(DateUtils.getNowDate());
        return tMembercontactsMapper.updateTMembercontacts(tMembercontacts);
    }

    /**
     * 批量删除会员联络人
     * 
     * @param membercontactsIds 需要删除的会员联络人主键
     * @return 结果
     */
    @Override
    public int deleteTMembercontactsByMembercontactsIds(Long[] membercontactsIds)
    {
        return tMembercontactsMapper.deleteTMembercontactsByMembercontactsIds(membercontactsIds);
    }

    /**
     * 删除会员联络人信息
     * 
     * @param membercontactsId 会员联络人主键
     * @return 结果
     */
    @Override
    public int deleteTMembercontactsByMembercontactsId(Long membercontactsId)
    {
        return tMembercontactsMapper.deleteTMembercontactsByMembercontactsId(membercontactsId);
    }
}
