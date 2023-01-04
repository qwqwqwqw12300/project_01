package com.newlandnpt.varyar.system.service;

import com.newlandnpt.varyar.common.core.domain.model.ResetMemberPwdRequest;

/**
 * 重置密码服务
 */
public interface IResetMemberPwdService {
    /**
     * 重置密码
     */
    void resetMemberPwd(ResetMemberPwdRequest resetMemberPwdRequest);

}
