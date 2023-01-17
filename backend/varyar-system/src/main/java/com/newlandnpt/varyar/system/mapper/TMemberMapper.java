package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TMember;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

/**
 * 会员Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface TMemberMapper
{
    /**
     * 查询会员
     * 
     * @param memberId 会员主键
     * @return 会员
     */
    public TMember selectMemberByMemberId(Long memberId);

    /**
     * 通过手机号查询会员

     */
    public TMember selectMemberByPhone(String phone);

    /**
     * 查询会员列表
     * 
     * @param member 会员
     * @return 会员集合
     */
    public List<TMember> selectMemberList(TMember member);

    /**
     * 新增会员
     * 
     * @param member 会员
     * @return 结果
     */
    public int insertMember(TMember member);

    /**
     * 修改会员
     * 
     * @param member 会员
     * @return 结果
     */
    public int updateMember(TMember member);

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
     * 重置用户密码
     *
     * @param phone 用户名/手机号
     * @param password 密码
     * @return 结果
     */
    public int resetUserPwd(@Param("phone") String phone, @Param("password") String password);


    /**
     * 会员总数
     * @return
     * @param member
     */
    @Select(" <script> " +
            " select count(*) from t_member where " +
            " <if test=\"params.dataScope != null and params.dataScope != ''\"> " +
            " user_id in (select user_id from sys_user u where 1=1 ${params.dataScope}) " +
            " and </if> " +
            " del_flag = '0' " +
            " </script> ")
    public long total(TMember member);

    /**
     * 未分配会员数
     * @return
     * @param member
     */
    @Select(" <script> " +
            " select count(*) from t_member m where distribute_flag = '0' " +
            " <if test=\"params.dataScope != null and params.dataScope != ''\"> " +
            " and m.user_id in (select user_id from sys_user u where 1=1 ${params.dataScope})</if> " +
            " and del_flag = '0' " +
            " </script> ")
    public long notArrangeMemberCount(TMember member);
}
