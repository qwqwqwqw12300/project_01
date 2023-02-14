package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.constant.Constants;
import com.newlandnpt.varyar.common.core.domain.model.LoginUser;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.RSA.RsaUtils;
import com.newlandnpt.varyar.common.core.domain.model.RegMemberRequest;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.user.CaptchaException;
import com.newlandnpt.varyar.common.exception.user.CaptchaExpireException;
import com.newlandnpt.varyar.framework.web.service.TokenService;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.service.IRegMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private static final Logger log = LoggerFactory.getLogger(RegMemberController.class);
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IRegMemberService regMemberService;

    @Value("${location.privateKey}")
    private String privateKey;
    @Autowired
    private TokenService tokenService;

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
            log.error(e.getMessage());
            return error("注册失败");
        }
        try {

            TMember tMember = regMemberService.regMember(regMemberRequest);
            LoginUser loginUser = new LoginUser();
            loginUser.setMemberPhone(tMember.getPhone());
            loginUser.setMemberId(tMember.getMemberId());
            loginUser.setMemberPassword(tMember.getPassword());
            String token = tokenService.createToken(loginUser);
            ajax.put(Constants.TOKEN, token);
        } catch (ServiceException e){
            log.error(e.getMessage());
            return error(e.getMessage());
        } catch (Exception e){
            log.error(e.getMessage());
            return error("注册失败");
        }
        return ajax;
    }
}
