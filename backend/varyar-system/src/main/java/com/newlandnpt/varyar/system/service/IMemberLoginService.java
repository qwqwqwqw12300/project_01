package com.newlandnpt.varyar.system.service;

import com.newlandnpt.varyar.common.core.domain.model.MemberLoginPwdRequest;
import com.newlandnpt.varyar.common.core.domain.model.MemberLoginSmsRequest;
import com.newlandnpt.varyar.system.domain.TMember;

/**
 * 会员登录方法
 * 
 * @author ruoyi
 */

public interface IMemberLoginService {
    /**
     * 密码登录验证
     */
    TMember loginByPwd(MemberLoginPwdRequest memberLoginPwdRequest);

    /**
     * 短信登录验证
     */
    TMember loginBySms(MemberLoginSmsRequest memberLoginSmsRequest);

}
