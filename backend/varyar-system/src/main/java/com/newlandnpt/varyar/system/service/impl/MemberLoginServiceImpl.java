package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.core.domain.model.MemberLoginPwdRequest;
import com.newlandnpt.varyar.common.core.domain.model.MemberLoginSmsRequest;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.exception.user.UserException;
import com.newlandnpt.varyar.common.exception.user.UserPasswordNotMatchException;
import com.newlandnpt.varyar.common.utils.SecurityUtils;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.mapper.TMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newlandnpt.varyar.system.service.IMemberLoginService;


/**
 * 注册会员 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class MemberLoginServiceImpl implements IMemberLoginService {

    private static final Logger log = LoggerFactory.getLogger(MemberLoginServiceImpl.class);

    @Autowired
    private TMemberMapper memberMapper;

    @Override
    public TMember loginByPwd(MemberLoginPwdRequest memberLoginPwdRequest) {

        String phone = memberLoginPwdRequest.getPhone();
        String pwd = memberLoginPwdRequest.getPassword();

        TMember tMember = memberMapper.selectMemberByPhone(phone);
        if (StringUtils.isNull(tMember)) {
            throw new UserException("user.not.exists",null);
        }

        boolean match = SecurityUtils.matchesPassword(pwd, tMember.getPassword());
        if(match==false){
            throw new UserPasswordNotMatchException();
        }

        return tMember;
    }

    @Override
    public TMember loginBySms(MemberLoginSmsRequest memberLoginSmsRequest) {
        String phone = memberLoginSmsRequest.getPhone();
        TMember tMember = memberMapper.selectMemberByPhone(phone);
        if (StringUtils.isNull(tMember)) {
            throw new UserException("user.not.exists",null);
        }
        return tMember;
    }
}
