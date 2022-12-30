package com.newlandnpt.varyar.api.controller.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.code.kaptcha.Producer;
import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.constant.Constants;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.utils.sign.Base64;
import com.newlandnpt.varyar.common.utils.uuid.IdUtils;

/**
 * 验证码操作处理
 * 
 * @author newlandnpt
 */
@RestController
@RequestMapping("/api")
public class CaptchaController {
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Autowired
    private RedisCache redisCache;

    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) throws IOException {
        AjaxResult ajax = AjaxResult.success();
        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        // 生成验证码
        String code = captchaProducer.createText();
        BufferedImage image = captchaProducer.createImage(code);

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return AjaxResult.error(e.getMessage());
        }
        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        return ajax;
    }
}
