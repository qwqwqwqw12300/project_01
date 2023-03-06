package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.utils.RSA.RsaUtils;
import com.newlandnpt.varyar.common.core.domain.model.ResetMemberPwdRequest;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.user.CaptchaException;
import com.newlandnpt.varyar.common.exception.user.CaptchaExpireException;
import com.newlandnpt.varyar.system.service.IResetMemberPwdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Api(tags = "重置密码相关服务")
@RestController
@RequestMapping("/api")
public class ResetMemberPwdController{
    private static final Logger log = LoggerFactory.getLogger(ResetMemberPwdController.class);
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IResetMemberPwdService resetMemberPwdService;

    @Value("${encrypt.privateKey}")
    private String privateKey;

    @ApiOperation("找回密码(重置密码)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号",required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "password", value = "密码",required = true,  dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "code", value = "图形验证码",required = true,  dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "uuid", value = "唯一标识", required = true, dataType = "String", dataTypeClass = String.class)
    })
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
        String[] codes = code.split("_");
        if(codes.length<2){
            throw new CaptchaExpireException();
        }
        code = codes[1];
        String phone = codes[0];
        if (!code.equalsIgnoreCase(resetMemberPwdRequest.getCode()) || !phone.equalsIgnoreCase(resetMemberPwdRequest.getPhone())) {
            throw new CaptchaException();
        }
        //解密
        try {
            resetMemberPwdRequest.setPassword(RsaUtils.decryptByPrivateKey(privateKey,resetMemberPwdRequest.getPassword()));
        } catch (Exception e) {
            log.error(e.getMessage());
            ajax = AjaxResult.error("密钥解密失败！");
            return ajax;
        }
        try {
            resetMemberPwdService.resetMemberPwd(resetMemberPwdRequest);
        } catch (Exception e){
            log.error(e.getMessage());
            ajax = AjaxResult.error("重置失败");
            return ajax;
        }

        return ajax;
    }
}
