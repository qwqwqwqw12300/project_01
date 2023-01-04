package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.constant.Constants;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.LoginUser;
import com.newlandnpt.varyar.common.core.domain.model.MemberLoginPwdRequest;
import com.newlandnpt.varyar.common.core.domain.model.MemberLoginSmsRequest;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.user.CaptchaException;
import com.newlandnpt.varyar.common.exception.user.CaptchaExpireException;
import com.newlandnpt.varyar.framework.web.service.TokenService;
import com.newlandnpt.varyar.system.domain.Member;
import com.newlandnpt.varyar.system.service.IMemberLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员登录
 * 
 * @author newlandnpt
 */
@RestController
@RequestMapping("/api")
public class MemberLoginController extends BaseController {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IMemberLoginService memberLoginService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/loginByPwd")
    public AjaxResult loginByPwd(
            @RequestBody @Validated MemberLoginPwdRequest memberLoginPwdRequest) {

        AjaxResult ajax = AjaxResult.success();
        // check captcha
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + memberLoginPwdRequest.getUuid();
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!captcha.equalsIgnoreCase(memberLoginPwdRequest.getCode())) {
            throw new CaptchaException();
        }

        // 生成令牌
        Member tMember = memberLoginService.loginByPwd(memberLoginPwdRequest);
        LoginUser loginUser = new LoginUser();
        loginUser.setMemberPhone(tMember.getPhone());
        loginUser.setMemberId(tMember.getMemberId());
        String token = tokenService.createToken(loginUser);
        ajax.put(Constants.TOKEN, token);

        return ajax;
    }

    @PostMapping("/loginBySms")
    public AjaxResult loginBySms(
            @RequestBody @Validated MemberLoginSmsRequest memberLoginSmsRequest) {

        AjaxResult ajax = AjaxResult.success();
        // check captcha
        String verifyKey = CacheConstants.SMS_CODE_KEY + memberLoginSmsRequest.getUuid();
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!captcha.equalsIgnoreCase(memberLoginSmsRequest.getCode())) {
            throw new CaptchaException();
        }

        // 生成令牌
        Member tMember = memberLoginService.loginBySms(memberLoginSmsRequest);

        LoginUser loginUser = new LoginUser();
        loginUser.setMemberPhone(tMember.getPhone());
        String token = tokenService.createToken(loginUser);

        ajax.put(Constants.TOKEN, token);

        return ajax;
    }
}