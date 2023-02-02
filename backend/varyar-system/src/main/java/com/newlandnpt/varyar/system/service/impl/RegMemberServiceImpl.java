package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.core.domain.entity.MemberParameter;
import com.newlandnpt.varyar.common.core.domain.model.RegMemberRequest;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.SecurityUtils;
import com.newlandnpt.varyar.common.utils.uuid.IdUtils;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.mapper.TMemberMapper;
import com.newlandnpt.varyar.system.service.IRegMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 注册会员 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class RegMemberServiceImpl implements IRegMemberService {

    @Autowired
    private TMemberMapper memberMapper;

    @Override
    public TMember regMember(RegMemberRequest regMemberRequest) {
        String phone = regMemberRequest.getPhone();
        String pwd = regMemberRequest.getPassword();

        TMember tMemberQuery = memberMapper.selectMemberByPhone(phone);
        if (tMemberQuery != null) {
            throw new ServiceException("该用户已注册！");
        }
        MemberParameter parameter = new MemberParameter();
        parameter.setPushMessage("0");
        TMember tMember = new TMember();
        tMember.setOrgId(Long.valueOf("101"));
        tMember.setParameter(parameter);
        tMember.setNo(IdUtils.fastSimpleUUID());
        tMember.setPhone(phone);
        tMember.setPassword(SecurityUtils.encryptPassword(pwd));
        tMember.setCreateTime(new Date());
        memberMapper.insertMember(tMember);
        return tMember;
    }
}
