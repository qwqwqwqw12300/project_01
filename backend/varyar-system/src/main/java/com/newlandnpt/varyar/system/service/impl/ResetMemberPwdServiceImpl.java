package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.core.domain.model.ResetMemberPwdRequest;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.SecurityUtils;
import com.newlandnpt.varyar.common.utils.uuid.IdUtils;
import com.newlandnpt.varyar.system.domain.Member;
import com.newlandnpt.varyar.system.mapper.MemberMapper;
import com.newlandnpt.varyar.system.service.IResetMemberPwdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    public void ResetMemberPwd(ResetMemberPwdRequest resetMemberPwdRequest) {
        String phone = resetMemberPwdRequest.getPhone();
        String pwd = resetMemberPwdRequest.getPassword();

        System.out.println(phone);
        Member tMemberQuery = memberMapper.selectMemberByPhone(phone);
        if (tMemberQuery == null) {
            throw new ServiceException("该用户不存在，请先注册！");
        }
        //System.out.println(tMemberQuery.toString());
        Long memberId = tMemberQuery.getMemberId();
        //更新用户密码
        Member tMember = new Member();
        tMember.setNo(IdUtils.fastSimpleUUID());
//        tMember.setPhone(phone);
        tMember.setMemberId(memberId);
        tMember.setPassword(SecurityUtils.encryptPassword(pwd));
        //tMember.setCreateTime(new Date());

        memberMapper.updateMember(tMember);
    }
}
