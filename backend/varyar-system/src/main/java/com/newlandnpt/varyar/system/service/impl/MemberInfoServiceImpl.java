package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.core.domain.model.MemberInfoRequest;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.mapper.TMemberMapper;
import com.newlandnpt.varyar.system.service.IMemberInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;


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

    @Autowired
    private RedisCache redisCache;

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
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public void updatePhone(MemberInfoRequest memberInfoRequest) {
        String newPhone = memberInfoRequest.getNewPhone();
        String oldPhone = memberInfoRequest.getOldPhone();
        TMember tMemberQuery = memberMapper.selectMemberByPhone(oldPhone);
        //通过手机号获取用户id
        tMemberQuery.setPhone(newPhone);
        memberMapper.updateMember(tMemberQuery);
        //清楚缓存用户信息
        Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        for(String key : keys){
            key =  key.replace(CacheConstants.LOGIN_TOKEN_KEY,"");
            String[] it = key.split("_");
            if(tMemberQuery.getMemberId().toString().equals(it[0])){
                redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + key);
            }
        }
    }
    /**
     * 修改会员头像
     *
     * @param memberPhone 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateMemberAvatar(String memberPhone, String avatar)
    {
        return memberMapper.updateMemberAvatar(memberPhone, avatar) > 0;
    }

    /**
     * 修改会员信息
     * @param member 会员实体类
     * @return 结果
     */
    @Override
    public int updateMemInfo (TMember member){

        return memberMapper.updateMember(member);

    }
}
