package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.AdviseRequest;
import com.newlandnpt.varyar.common.core.domain.model.LoginUser;
import com.newlandnpt.varyar.common.exception.user.CaptchaException;
import com.newlandnpt.varyar.common.exception.user.CaptchaExpireException;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.system.domain.TAdvise;
import com.newlandnpt.varyar.system.service.IAdviseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.newlandnpt.varyar.common.core.redis.RedisCache;

/**
 * 建议Controller
 * 
 * @author chenxw
 * @date 2023-01-05
 */
@RestController
@RequestMapping("/api/advise")
public class AdviseController extends BaseController
{
    @Autowired
    private IAdviseService adviseService;
    @Autowired
    private RedisCache redisCache;
    /**
     * 新增建议/问题内容
     */
    @PostMapping("/addAdvise")
    public AjaxResult addAdvise(@RequestBody AdviseRequest advise)
    {
        // check captcha
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + advise.getUuid();
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!captcha.equalsIgnoreCase(advise.getCaptcha())) {
            throw new CaptchaException();
        }
        if(StringUtils.isEmpty(advise.getContent()))
        {
            return error("提交失败，建议内容不能为空！");
        }
        //获取当前用户的id
        LoginUser loginUser = getLoginUser();
        Long memberId = loginUser.getMemberId();
        TAdvise tAdvise = new TAdvise();
        tAdvise.setMemberId(memberId);
        tAdvise.setContent(advise.getContent());
        return toAjax(adviseService.insertTAdvise(tAdvise));
    }

}
