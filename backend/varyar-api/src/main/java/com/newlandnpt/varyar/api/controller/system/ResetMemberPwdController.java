package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.utils.RSA.RsaUtils;
import com.newlandnpt.varyar.common.core.domain.model.ResetMemberPwdRequest;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.user.CaptchaException;
import com.newlandnpt.varyar.common.exception.user.CaptchaExpireException;
import com.newlandnpt.varyar.system.service.IResetMemberPwdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 重置密码服务
 * 
 * @author chenxw
 */
@RestController
@RequestMapping("/api")
public class ResetMemberPwdController{
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IResetMemberPwdService resetMemberPwdService;

    @Value("${location.privateKey}")
    private String privateKey;

    @PostMapping("/resetMemberPwd")
    public AjaxResult resetMemberPwd(
            @RequestBody @Validated ResetMemberPwdRequest resetMemberPwdRequest) {

        AjaxResult ajax = AjaxResult.success();
        // check sms
        String verifyKey = CacheConstants.SMS_CODE_KEY + resetMemberPwdRequest.getUuid();
        String code = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (code == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(resetMemberPwdRequest.getCode())) {
            throw new CaptchaException();
        }
        //解密
        try {
            resetMemberPwdRequest.setPassword(RsaUtils.decryptByPrivateKey(privateKey,resetMemberPwdRequest.getPassword()));
        } catch (Exception e) {
            ajax = AjaxResult.error("密钥解密失败！");
            return ajax;
        }
        try {
            resetMemberPwdService.resetMemberPwd(resetMemberPwdRequest);
        } catch (Exception e){
            ajax = AjaxResult.error("重置失败");
            return ajax;
        }

        return ajax;
    }
}
