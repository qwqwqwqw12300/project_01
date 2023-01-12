package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.core.domain.entity.RsaUtils;
import com.newlandnpt.varyar.common.core.domain.model.RegMemberRequest;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.user.CaptchaException;
import com.newlandnpt.varyar.common.exception.user.CaptchaExpireException;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.domain.TMemberFamily;
import com.newlandnpt.varyar.system.service.IMemberFamilyService;
import com.newlandnpt.varyar.system.service.IRegMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;

import java.util.List;

/**
 * 注册验证
 * 
 * @author newlandnpt
 */
@RestController
@RequestMapping("/api")
public class RegMemberController extends BaseController {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IRegMemberService regMemberService;

    @Value("${location.privateKey}")
    private String privateKey;

    @Autowired
    private IMemberFamilyService iMemberFamilyService;
    @PostMapping("/regMember")
    public AjaxResult regMember(
            @RequestBody @Validated RegMemberRequest regMemberRequest) {

        AjaxResult ajax = AjaxResult.success();
        // check sms
        String verifyKey = CacheConstants.SMS_CODE_KEY + regMemberRequest.getSmsUuid();
        String code = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (code == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(regMemberRequest.getCode())) {
            throw new CaptchaException();
        }
        //解密
        try {
            regMemberRequest.setPassword(RsaUtils.decryptByPrivateKey(privateKey,regMemberRequest.getPassword()));
        } catch (Exception e) {
            ajax = AjaxResult.error("密钥解密失败！");
            return ajax;
        }
        TMember member = new TMember();
        try {
            member = regMemberService.regMember(regMemberRequest);
        } catch (Exception e){
            ajax = AjaxResult.error("注册失败");
            return ajax;
        }
        //查询会员与家庭表 添加共享家庭家庭信息
        List<TMemberFamily> mFList = iMemberFamilyService.selectTMemberFamilyByPhone(regMemberRequest.getPhone());
        if (mFList.size()>0&&mFList !=null){
            for(TMemberFamily item:mFList){
                item.setPhone("");
                item.setMemberId(member.getMemberId());
                iMemberFamilyService.insertTMemberFamily(item);
            }
        }

        return ajax;
    }
}
