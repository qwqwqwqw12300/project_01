package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TMembercontacts;

/**
 * 会员联络人Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface TMembercontactsMapper 
{
    /**
     * 查询会员联络人
     * 
     * @param membercontactsId 会员联络人主键
     * @return 会员联络人
     */
    public TMembercontacts selectTMembercontactsByMembercontactsId(Long membercontactsId);

    /**
     * 查询会员联络人列表
     * 
     * @param tMembercontacts 会员联络人
     * @return 会员联络人集合
     */
    public List<TMembercontacts> selectTMembercontactsList(TMembercontacts tMembercontacts);

    /**
     * 新增会员联络人
     * 
     * @param tMembercontacts 会员联络人
     * @return 结果
     */
    public int insertTMembercontacts(TMembercontacts tMembercontacts);

    /**
     * 修改会员联络人
     * 
     * @param tMembercontacts 会员联络人
     * @return 结果
     */
    public int updateTMembercontacts(TMembercontacts tMembercontacts);

    /**
     * 删除会员联络人
     * 
     * @param membercontactsId 会员联络人主键
     * @return 结果
     */
    public int deleteTMembercontactsByMembercontactsId(Long membercontactsId);

    /**
     * 批量删除会员联络人
     * 
     * @param membercontactsIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMembercontactsByMembercontactsIds(Long[] membercontactsIds);
}
