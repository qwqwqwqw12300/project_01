package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.core.domain.entity.MemberParameter;
import com.newlandnpt.varyar.common.core.domain.model.RegMemberRequest;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.SecurityUtils;
import com.newlandnpt.varyar.common.utils.uuid.IdUtils;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.domain.TMemberFamily;
import com.newlandnpt.varyar.system.mapper.TMemberMapper;
import com.newlandnpt.varyar.system.service.IMemberFamilyService;
import com.newlandnpt.varyar.system.service.IRegMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 注册会员 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class RegMemberServiceImpl implements IRegMemberService {
    private static final Logger log = LoggerFactory.getLogger(RegMemberServiceImpl.class);
    @Autowired
    private TMemberMapper memberMapper;

    @Autowired
    private IMemberFamilyService iMemberFamilyService;
    @Override
    public TMember regMember(RegMemberRequest regMemberRequest) {
        String phone = regMemberRequest.getPhone();
        String pwd = regMemberRequest.getPassword();

        TMember tMemberQuery = memberMapper.selectMemberByPhone(phone);
        if (tMemberQuery != null) {
            log.error("异常信息:{}", "该用户已注册！");
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
        tMember.setRegistrationId(regMemberRequest.getRegistrationId());
        tMember.setRegistrationType(regMemberRequest.getRegistrationType());
        memberMapper.insertMember(tMember);
        //查询会员与家庭表 添加共享家庭家庭信息
        List<TMemberFamily> mFList = iMemberFamilyService.selectTMemberFamilyByPhone(regMemberRequest.getPhone());
        if (mFList.size()>0&&mFList !=null){
            for(TMemberFamily item:mFList){
                item.setPhone("");
                item.setMemberId(tMember.getMemberId());
                iMemberFamilyService.updateTMemberFamily(item);
            }
        }
        return tMember;
    }
}
