package com.newlandnpt.varyar.system.service;

import com.newlandnpt.varyar.common.core.domain.model.MemberInfoRequest;

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

}
