package com.newlandnpt.varyar.system.service;

import com.newlandnpt.varyar.common.core.domain.model.RegMemberRequest;

/**
 * 注册会员
 */
public interface IRegMemberService {
    /**
     * 注册会员
     */
    void regMember(RegMemberRequest regMemberRequest);
}
