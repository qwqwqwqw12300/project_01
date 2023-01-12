package com.newlandnpt.varyar.system.service;

import com.newlandnpt.varyar.common.core.domain.model.RegMemberRequest;
import com.newlandnpt.varyar.system.domain.TMember;

/**
 * 注册会员
 */
public interface IRegMemberService {
    /**
     * 注册会员
     */
    TMember regMember(RegMemberRequest regMemberRequest);
}
