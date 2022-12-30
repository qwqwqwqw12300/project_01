package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.core.domain.model.RegMemberRequest;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.user.CaptchaException;
import com.newlandnpt.varyar.common.exception.user.CaptchaExpireException;
import com.newlandnpt.varyar.system.service.IRegMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;

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

        try {
            regMemberService.regMember(regMemberRequest);
        } catch (Exception e){
            ajax = AjaxResult.error("注册失败");
            return ajax;
        }

        return ajax;
    }
}
