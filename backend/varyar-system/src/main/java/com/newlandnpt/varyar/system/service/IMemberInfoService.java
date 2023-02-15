package com.newlandnpt.varyar.system.service;

import com.newlandnpt.varyar.common.core.domain.model.MemberInfoRequest;
import com.newlandnpt.varyar.system.domain.TMember;

/**
 * 会员个人中心方法
 * 
 * @author chenxw
 */

public interface IMemberInfoService {
    /**
     * 重置用户密码
     *
     * @param phone 用户名手机号
     * @param password 密码
     * @return 结果
     */
    public int resetUserPwd(String phone, String password);

    /**
     * 手机号更改
     */
    public void updatePhone(MemberInfoRequest memberInfoRequest);

    /**
     * 修改用户头像
     *
     * @param memberPhone 会员手机号
     * @param avatar 头像地址
     * @return 结果
     */
    public boolean updateMemberAvatar(String memberPhone, String avatar);

    /**
     * 修改会员信息
     *
     * @param member 会员实体类
     * @return 结果
     */
    public int updateMemInfo (TMember member);
}
