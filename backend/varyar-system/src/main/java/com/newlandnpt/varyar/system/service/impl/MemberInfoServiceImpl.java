package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.core.domain.model.MemberInfoRequest;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.mapper.TMemberMapper;
import com.newlandnpt.varyar.system.service.IMemberInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 个人中心修改 服务层实现
 * 
 * @author chenxw
 */
@Service
public class MemberInfoServiceImpl implements IMemberInfoService {

    private static final Logger log = LoggerFactory.getLogger(MemberInfoServiceImpl.class);

    @Autowired
    private TMemberMapper memberMapper;

    /**
     * 重置用户密码
     *
     * @param phone 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String phone, String password)
    {
        return memberMapper.resetUserPwd(phone, password);
    }


    @Override
    public void updatePhone(MemberInfoRequest memberInfoRequest) {
        String newPhone = memberInfoRequest.getNewPhone();
        String oldPhone = memberInfoRequest.getOldPhone();
        TMember tMemberQuery = memberMapper.selectMemberByPhone(oldPhone);
        //通过手机号获取用户id
        long memberId=tMemberQuery.getMemberId();

        TMember tMember = new TMember();
        tMember.setMemberId(memberId);
        tMember.setPhone(newPhone);

        memberMapper.updateMember(tMember);


    }
}
