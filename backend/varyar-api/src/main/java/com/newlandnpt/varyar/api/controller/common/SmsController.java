package com.newlandnpt.varyar.api.controller.common;

import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.constant.Constants;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.SmsRequest;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.user.CaptchaException;
import com.newlandnpt.varyar.common.exception.user.CaptchaExpireException;
import com.newlandnpt.varyar.common.utils.uuid.IdUtils;
import com.newlandnpt.varyar.system.service.IMemberService;
import com.newlandnpt.varyar.system.service.ISmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 短信操作处理
 * 
 * @author newlandnpt
 */
@RestController
@RequestMapping("/api")
public class SmsController {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISmsService smsService;
    /**
     * 发送短信
     */
    @PostMapping("/sendSms")
    public AjaxResult send(@RequestBody @Validated SmsRequest smsRequest) {
        AjaxResult ajax = AjaxResult.success();

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
        /*for(int i = 0; i < 4; ++i) {
            randomSb.append(random.nextInt(10));
        }
        String code = randomSb.toString();*/
        String code = "1234";
        redisCache.setCacheObject(smsVerifyKey, code, Constants.SMS_CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        smsService.sendSms(code);

        ajax.put("smsUuid", smsUuid);
        return ajax;
    }
    /**
     * 校验短信验证码
     */
    @PostMapping("/checkSms")
    public AjaxResult checkSms(@RequestBody @Validated SmsRequest smsRequest) {
        AjaxResult ajax = AjaxResult.success();

        // check sms
        String verifyKey = CacheConstants.SMS_CODE_KEY + smsRequest.getUuid();
        String code = redisCache.getCacheObject(verifyKey);
        if (code == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(smsRequest.getCaptcha())) {
            throw new CaptchaException();
        }
        return ajax;
    }
}
