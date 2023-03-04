package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.constant.Constants;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.SmsRequest;
import com.newlandnpt.varyar.common.exception.user.UserException;
import com.newlandnpt.varyar.common.utils.RSA.RsaUtils;
import com.newlandnpt.varyar.common.core.domain.model.LoginUser;
import com.newlandnpt.varyar.common.core.domain.model.MemberLoginPwdRequest;
import com.newlandnpt.varyar.common.core.domain.model.MemberLoginSmsRequest;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.user.CaptchaException;
import com.newlandnpt.varyar.common.exception.user.CaptchaExpireException;
import com.newlandnpt.varyar.common.utils.SecurityUtils;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.common.utils.uuid.IdUtils;
import com.newlandnpt.varyar.framework.web.service.TokenService;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.service.IMemberLoginService;
import com.newlandnpt.varyar.system.service.IMemberService;
import com.newlandnpt.varyar.system.service.ISmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * 会员登录
 * 
 * @author newlandnpt
 */
@Api(tags = "会员登录")
@RestController
@RequestMapping("/api")
public class MemberLoginController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(MemberLoginController.class);
    private static final Logger clientLog = LoggerFactory.getLogger("client");
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IMemberLoginService memberLoginService;
    @Autowired
    private IMemberService iMemberService;

    @Autowired
    private TokenService tokenService;

    @Value("${encrypt.privateKey}")
    private String privateKey;
    @Autowired
    private ISmsService smsService;


    @ApiOperation("账号密码登录")
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

    @ApiOperation("申请短信登录验证码")
    @PostMapping("/applyLoginBySms")
    public AjaxResult loginBySms(@RequestBody @Validated SmsRequest smsRequest) {

        AjaxResult ajax = AjaxResult.success();
        if(org.apache.commons.lang3.StringUtils.isBlank(smsRequest.getPhone())){
            return AjaxResult.error("手机号码不能为空");
        }
        TMember tMember = iMemberService.selectMemberByPhone(smsRequest.getPhone());
        if (StringUtils.isNull(tMember)) {
            return AjaxResult.error("用户未注册");
        }

        // check captcha
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + smsRequest.getUuid();
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!captcha.equalsIgnoreCase(smsRequest.getCaptcha())) {
            throw new CaptchaException();
        }

        // 保存验证码信息
        String smsUuid = IdUtils.simpleUUID();
        String smsVerifyKey = CacheConstants.SMS_CODE_KEY + smsUuid;
        // 生成验证码
        Random random = new Random();
        StringBuffer randomSb = new StringBuffer();
        for(int i = 0; i < 6; ++i) {
            randomSb.append(random.nextInt(10));
        }
        String code = randomSb.toString();
//        String code = "1234";
        redisCache.setCacheObject(smsVerifyKey, smsRequest.getPhone()+"_"+code, Constants.SMS_CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        smsService.sendSms(code,smsRequest.getPhone());

        ajax.put("smsUuid", smsUuid);
        return ajax;
    }

    @ApiOperation("短信验证码登录")
    @PostMapping("/loginBySms")
    public AjaxResult loginBySms(
            @RequestBody @Validated MemberLoginSmsRequest memberLoginSmsRequest) {

        AjaxResult ajax = AjaxResult.success();
        // check captcha
        String verifyKey = CacheConstants.SMS_CODE_KEY + memberLoginSmsRequest.getUuid();
        String code = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (code == null) {
            throw new CaptchaExpireException();
        }
        String[] codes = code.split("_");
        if(codes.length<2){
            throw new CaptchaExpireException();
        }
        code = codes[1];
        String phone = codes[0];
        if (!code.equalsIgnoreCase(memberLoginSmsRequest.getCode()) ||!phone.equalsIgnoreCase(memberLoginSmsRequest.getCode())) {
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

    @ApiOperation("用户登出")
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

    @ApiOperation("客户端日志")
    @GetMapping("/logging")
    public AjaxResult logging(@RequestParam(name = "message",required = false) String message){
        clientLog.debug(">>>>>> 用户[{}],日志:{}", Optional.ofNullable(SecurityUtils.getLoginUserWithoutException())
                .map(p -> p.getUsername())
                .orElse("匿名"),message);
        return AjaxResult.success();
    }
}
