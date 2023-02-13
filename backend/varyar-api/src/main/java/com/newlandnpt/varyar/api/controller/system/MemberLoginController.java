package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.constant.Constants;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.exception.user.UserException;
import com.newlandnpt.varyar.common.utils.RSA.RsaUtils;
import com.newlandnpt.varyar.common.core.domain.model.LoginUser;
import com.newlandnpt.varyar.common.core.domain.model.MemberLoginPwdRequest;
import com.newlandnpt.varyar.common.core.domain.model.MemberLoginSmsRequest;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.user.CaptchaException;
import com.newlandnpt.varyar.common.exception.user.CaptchaExpireException;
import com.newlandnpt.varyar.framework.web.service.TokenService;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.service.IMemberLoginService;
import com.newlandnpt.varyar.system.service.IMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 会员登录
 * 
 * @author newlandnpt
 */
@RestController
@RequestMapping("/api")
public class MemberLoginController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(MemberLoginController.class);
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IMemberLoginService memberLoginService;
    @Autowired
    private IMemberService iMemberService;

    @Autowired
    private TokenService tokenService;

    @Value("${location.privateKey}")
    private String privateKey;


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
        //解密
        try {
            memberLoginPwdRequest.setPassword(RsaUtils.decryptByPrivateKey(privateKey,memberLoginPwdRequest.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 生成令牌
        TMember tMember = memberLoginService.loginByPwd(memberLoginPwdRequest);
        LoginUser loginUser = new LoginUser();
        loginUser.setMemberPhone(tMember.getPhone());
        loginUser.setMemberId(tMember.getMemberId());
        loginUser.setMemberPassword(tMember.getPassword());
        String token = tokenService.createToken(loginUser);
        ajax.put(Constants.TOKEN, token);
        //更新登录信息
        try {
            tMember.setRegistrationId(memberLoginPwdRequest.getRegistrationId());
            tMember.setRegistrationType(memberLoginPwdRequest.getRegistrationType());
            iMemberService.updateMember(tMember);
        }catch(Exception e){
            return error("登录失败！");
        }
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
        TMember tMember = memberLoginService.loginBySms(memberLoginSmsRequest);
        LoginUser loginUser = new LoginUser();
        loginUser.setMemberPhone(tMember.getPhone());
        loginUser.setMemberId(tMember.getMemberId());
        String token = tokenService.createToken(loginUser);

        ajax.put(Constants.TOKEN, token);
        //更新登录信息
        try {
            tMember.setRegistrationId(memberLoginSmsRequest.getRegistrationId());
            tMember.setRegistrationType(memberLoginSmsRequest.getRegistrationType());
            iMemberService.updateMember(tMember);
        }catch(Exception e){
            log.error(e.getMessage());
            return error("登录失败！");
        }
        return ajax;
    }

    //用户退出
    @PostMapping("/loginOut")
    public AjaxResult loginOut() {
        //获取当前登录用户信息
        LoginUser loginUser = getLoginUser();
        String token = loginUser.getToken();
        AjaxResult ajax = AjaxResult.success();
        // 清除缓存token
        String tokenKey = CacheConstants.LOGIN_TOKEN_KEY + token;
        redisCache.deleteObject(tokenKey);
        return ajax;
    }
}
