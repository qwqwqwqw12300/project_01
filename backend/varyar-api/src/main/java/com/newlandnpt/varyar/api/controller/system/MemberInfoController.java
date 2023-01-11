package com.newlandnpt.varyar.api.controller.system;


import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.entity.RsaUtils;
import com.newlandnpt.varyar.common.core.domain.model.LoginUser;
import com.newlandnpt.varyar.common.core.domain.model.MemberInfoRequest;
import com.newlandnpt.varyar.common.core.domain.model.PasswordRequest;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.user.CaptchaException;
import com.newlandnpt.varyar.common.exception.user.CaptchaExpireException;
import com.newlandnpt.varyar.common.utils.SecurityUtils;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.framework.web.service.TokenService;
import com.newlandnpt.varyar.system.service.IMemberInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



/**
 * 个人中心Controller
 *
 * @author chenxiangwei
 * @date 2022-12-24
 */
@RestController
@RequestMapping("/api/memberInfo")
public class MemberInfoController extends BaseController
{
    @Autowired
    private IMemberInfoService memberInfoService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisCache redisCache;

    @Value("${location.gaode.privateKey}")
    private String privateKey;

    /**
     * 个人中心-重置密码
     */
    @PostMapping("/updatePwd")
    public AjaxResult updatePwd(@RequestBody @Validated PasswordRequest passwordRequest)
    {
        LoginUser loginUser = getLoginUser();
        String oldPassword = passwordRequest.getOldPassword();
        String newPassword = passwordRequest.getNewPassword();
        //解密
        try {
            oldPassword = RsaUtils.decryptByPrivateKey(privateKey,oldPassword);
            newPassword = RsaUtils.decryptByPrivateKey(privateKey,newPassword);
        } catch (Exception e) {
            error("密钥解密失败！");
        }
        //获取当前登录用户手机号及密码信息
        String Phone = loginUser.getMemberPhone();
        String password = loginUser.getMemberPassword();
       if (!SecurityUtils.matchesPassword(oldPassword, password))
        {
            return error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password))
        {
            return error("新密码不能与旧密码相同");
        }
        if (memberInfoService.resetUserPwd(Phone, SecurityUtils.encryptPassword(newPassword)) > 0)
        {
            // 更新缓存app用户密码
            loginUser.setMemberPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("修改密码异常，请联系管理员");

    }
    /**
     * 个人中心-更改手机号
     */
    @PostMapping("/updatePhone")
    public AjaxResult updatePhone(@RequestBody MemberInfoRequest memberInfoRequest)
    {
        // 原手机短信验证
        String verifyKey = CacheConstants.SMS_CODE_KEY + memberInfoRequest.getUuid();
        String code = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (code == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(memberInfoRequest.getOldCode())) {
            throw new CaptchaException();
        }

        // 新手机短信验证
        String newVerifyKey = CacheConstants.SMS_CODE_KEY + memberInfoRequest.getNewuuid();
        String newCode = redisCache.getCacheObject(newVerifyKey);
        redisCache.deleteObject(newVerifyKey);
        if (newCode == null) {
            throw new CaptchaExpireException();
        }
        if (!newCode.equalsIgnoreCase(memberInfoRequest.getNewCode())) {
            throw new CaptchaException();
        }

        try {
            //获取当前登录用户信息比对
            LoginUser loginUser = getLoginUser();
            String phone = loginUser.getMemberPhone();
            String password = loginUser.getMemberPassword();

            if(StringUtils.isEmpty(memberInfoRequest.getNewPhone())
                    && StringUtils.isEmpty(memberInfoRequest.getOldPhone())  )
            {
                return error("修改用户'" + loginUser.getUsername() + "'失败，手机号码不能为空！");

            }
            if (memberInfoRequest.getNewPhone().equals(phone))
            {
                return error("修改用户'" + loginUser.getUsername() + "'失败，手机号码与旧号码相同");
            }

            if (!SecurityUtils.matchesPassword(memberInfoRequest.getPassword(), password))
            {
                return error("用户密码不匹配");
            }

             memberInfoService.updatePhone(memberInfoRequest);
             // 更新缓存用户信息
             loginUser.setMemberPhone(memberInfoRequest.getNewPhone());
             tokenService.setLoginUser(loginUser);
             return success();


        } catch (Exception e){
            return error("修改手机号异常，请联系管理员");
        }

    }

}