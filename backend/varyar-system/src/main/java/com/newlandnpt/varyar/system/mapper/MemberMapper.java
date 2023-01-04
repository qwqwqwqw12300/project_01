package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.Member;
import org.apache.ibatis.annotations.Select;

/**
 * 会员Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface MemberMapper
{
    /**
     * 查询会员
     * 
     * @param memberId 会员主键
     * @return 会员
     */
    public Member selectMemberByMemberId(Long memberId);

    /**
     * 通过手机号查询会员

     */
    public Member selectMemberByPhone(String phone);

    /**
     * 查询会员列表
     * 
     * @param member 会员
     * @return 会员集合
     */
    public List<Member> selectMemberList(Member member);

    /**
     * 新增会员
     * 
     * @param member 会员
     * @return 结果
     */
    public int insertMember(Member member);

    /**
     * 修改会员
     * 
     * @param member 会员
     * @return 结果
     */
    public int updateMember(Member member);

    /**
     * 删除会员
     * 
     * @param memberId 会员主键
     * @return 结果
     */
    public int deleteMemberByMemberId(Long memberId);

    /**
     * 批量删除会员
     * 
     * @param memberIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMemberByMemberIds(Long[] memberIds);

    /**
     * 根据机构id统计机构服务会员数量
     * @return
     */
    @Select("select count(*) from t_member where del_flag = '0'")
    public long total();
}