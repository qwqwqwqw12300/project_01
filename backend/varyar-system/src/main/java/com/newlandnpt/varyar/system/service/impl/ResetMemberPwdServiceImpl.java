package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.core.domain.model.ResetMemberPwdRequest;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.SecurityUtils;
import com.newlandnpt.varyar.system.domain.Member;
import com.newlandnpt.varyar.system.mapper.MemberMapper;
import com.newlandnpt.varyar.system.service.IResetMemberPwdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 重置/找回密码 服务层实现
 * 
 * @author chenxw1
 */
@Service
public class ResetMemberPwdServiceImpl implements IResetMemberPwdService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public void resetMemberPwd(ResetMemberPwdRequest resetMemberPwdRequest) {
        String phone = resetMemberPwdRequest.getPhone();
        String pwd = resetMemberPwdRequest.getPassword();
        //校验用户是否存在
        Member tMemberQuery = memberMapper.selectMemberByPhone(phone);
        if (tMemberQuery == null) {
            throw new ServiceException("该用户不存在，请先注册！");
        }
        //更新用户密码
        memberMapper.resetUserPwd(phone,SecurityUtils.encryptPassword(pwd));
    }
}
