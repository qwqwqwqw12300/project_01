package com.newlandnpt.varyar.common.exception.user;

import com.newlandnpt.varyar.common.exception.base.BaseException;

/**
 * 会员未注册异常
 * @author lin.ju
 * @date 2023/2/11
 */
public class MemberNotRegisterException extends BaseException {



    private static final long serialVersionUID = 1L;

    public MemberNotRegisterException(String code, Object[] args)
    {
        super("member", code, args, "账号未注册");
    }
}
