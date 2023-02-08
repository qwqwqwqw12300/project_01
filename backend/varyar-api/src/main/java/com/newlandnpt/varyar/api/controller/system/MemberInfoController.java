package com.newlandnpt.varyar.api.controller.system;


import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.constant.Constants;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.entity.MemberInfo;
import com.newlandnpt.varyar.common.exception.user.UserPasswordRetryLimitExceedException;
import com.newlandnpt.varyar.common.utils.MessageUtils;
import com.newlandnpt.varyar.common.utils.RSA.RsaUtils;
import com.newlandnpt.varyar.common.core.domain.model.LoginUser;
import com.newlandnpt.varyar.common.core.domain.model.MemberInfoRequest;
import com.newlandnpt.varyar.common.core.domain.model.PasswordRequest;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.exception.user.CaptchaException;
import com.newlandnpt.varyar.common.exception.user.CaptchaExpireException;
import com.newlandnpt.varyar.common.utils.SecurityUtils;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.framework.manager.AsyncManager;
import com.newlandnpt.varyar.framework.manager.factory.AsyncFactory;
import com.newlandnpt.varyar.framework.web.service.SysPasswordService;
import com.newlandnpt.varyar.framework.web.service.TokenService;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.service.IMemberInfoService;
import com.newlandnpt.varyar.system.service.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;


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

    @Value("${location.privateKey}")
    private String privateKey;

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    @Autowired
    private IMemberService iMemberService;
    /**
     * 个人中心-重置密码
     */
    @PostMapping("/updatePwd")
    public AjaxResult updatePwd(@RequestBody @Validated PasswordRequest passwordRequest)
    {
        LoginUser loginUser = getLoginUser();
        //校验密码验证次数
        Integer retryCount = redisCache.getCacheObject(CacheConstants.PWD_ERR_CNT_KEY+loginUser.getMemberPhone());
        if (retryCount == null){
            retryCount = 0;
        }
        if (retryCount >= Integer.valueOf(maxRetryCount).intValue()){
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginUser.getMemberPhone(), Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.retry.limit.exceed", maxRetryCount, lockTime)));
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }
        String oldPassword = passwordRequest.getOldPassword();
        String newPassword = passwordRequest.getNewPassword();
        //解密
        try {
            oldPassword = RsaUtils.decryptByPrivateKey(privateKey,oldPassword);
            newPassword = RsaUtils.decryptByPrivateKey(privateKey,newPassword);
        } catch (Exception e) {
            return error("修改密码失败！");
        }
        //获取当前登录用户手机号及密码信息
        String Phone = loginUser.getMemberPhone();
        String password = loginUser.getMemberPassword();
       if (!SecurityUtils.matchesPassword(oldPassword, password)){
            retryCount = retryCount + 1;
            redisCache.setCacheObject(CacheConstants.PWD_ERR_CNT_KEY+loginUser.getMemberPhone(),
                    retryCount, lockTime, TimeUnit.MINUTES);
            return error(MessageUtils.message("user.password.retry.limit.count", retryCount));
        }
        if (SecurityUtils.matchesPassword(newPassword, password)){
            return error("新密码不能与旧密码相同");
        }
        if (memberInfoService.resetUserPwd(Phone, SecurityUtils.encryptPassword(newPassword)) > 0){
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
        // 短信验证
        String verifyKey = CacheConstants.SMS_CODE_KEY + memberInfoRequest.getUuid();
        String code = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (code == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(memberInfoRequest.getNewCode())) {
            throw new CaptchaException();
        }
        //解密
        try {
            memberInfoRequest.setPassword(RsaUtils.decryptByPrivateKey(privateKey,memberInfoRequest.getPassword()));
        } catch (Exception e) {
            return error("密钥解密失败！");
        }
        TMember member =iMemberService.selectMemberByPhone(memberInfoRequest.getNewPhone());
        try {
            //获取当前登录用户信息比对
            LoginUser loginUser = getLoginUser();
            String phone = loginUser.getMemberPhone();
            String password = loginUser.getMemberPassword();
            if(StringUtils.isEmpty(memberInfoRequest.getNewPhone())
                    && StringUtils.isEmpty(memberInfoRequest.getOldPhone())  ){
                return error("修改用户'" + loginUser.getUsername() + "'失败，手机号码不能为空！");
            }
            if (memberInfoRequest.getNewPhone().equals(phone)){
                return error("修改用户'" + loginUser.getUsername() + "'失败，手机号码与旧号码相同");
            }
            if (member != null){
                return error("修改用户'" + loginUser.getUsername() + "'失败，新手机号码已注册！");
            }
            if (!SecurityUtils.matchesPassword(memberInfoRequest.getPassword(), password)){
                return error("用户密码不匹配");
            }
            memberInfoRequest.setOldPhone(this.getLoginUser().getMemberPhone());
            memberInfoService.updatePhone(memberInfoRequest);
        } catch (Exception e){
            return error("修改手机号异常，请联系管理员");
        }
        return success();
    }
    /**
     * 获取用户信息
     */
    @GetMapping("/getMemInfo")
    public AjaxResult  getMemberInfo(){
        AjaxResult ajax = AjaxResult.success();
        Long memberId = getLoginUser().getMemberId();
        TMember member = new TMember();
        try {
            member = iMemberService.selectMemberByMemberId(memberId);
        }  catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setMemberId(member.getMemberId());
        memberInfo.setPhone(member.getPhone());
        if(member.getParameter() == null || member.getParameter().getPushMessage()==null){
            memberInfo.setState("0");
            return AjaxResult.success(memberInfo);
        }
        memberInfo.setState(member.getParameter().getPushMessage());
        return AjaxResult.success(memberInfo);
    }

}