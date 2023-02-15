package com.newlandnpt.varyar.api.controller.system;


import com.newlandnpt.varyar.common.annotation.Log;
import com.newlandnpt.varyar.common.config.VayyarConfig;
import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.constant.Constants;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.entity.MemberInfo;
import com.newlandnpt.varyar.common.enums.BusinessType;
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
import com.newlandnpt.varyar.common.utils.file.FileUploadUtils;
import com.newlandnpt.varyar.common.utils.file.MimeTypeUtils;
import com.newlandnpt.varyar.common.utils.uuid.IdUtils;
import com.newlandnpt.varyar.framework.manager.AsyncManager;
import com.newlandnpt.varyar.framework.manager.factory.AsyncFactory;
import com.newlandnpt.varyar.framework.web.service.TokenService;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.service.IMemberInfoService;
import com.newlandnpt.varyar.system.service.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

import static com.newlandnpt.varyar.common.constant.CacheConstants.PHONE_TOKEN_KEY;


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
     * 个人中心-更改手机号(短信验证接口)
     */
    @PostMapping("/updatePhoneBySms")
    public AjaxResult updatePhoneBySms(@RequestBody MemberInfoRequest memberInfoRequest)
    {
         AjaxResult ajax = AjaxResult.success();
        // 短信验证
        String verifyKey = CacheConstants.SMS_CODE_KEY + memberInfoRequest.getUuid();
        String code = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (code == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(memberInfoRequest.getOldCode())) {
            throw new CaptchaException();
        }
        try {
            LoginUser loginUser = getLoginUser();
            if(StringUtils.isEmpty(memberInfoRequest.getOldPhone())  ){
                return error("修改用户'" + memberInfoRequest.getOldPhone() + "'失败，手机号码不能为空！");
            }
            memberInfoRequest.setOldPhone(this.getLoginUser().getMemberPhone());
            String token = loginUser.getMemberId()+"_"+ IdUtils.fastUUID();
            //缓存到redis中，失效时间为5分钟
            String memberPhoneKey = PHONE_TOKEN_KEY + token;
            redisCache.setCacheObject(memberPhoneKey, "SUCCESS", 5, TimeUnit.MINUTES);
            //返回token
            ajax.put(Constants.TOKEN, token);
        } catch (Exception e){
            return error("修改手机号异常，请联系管理员");
        }
        return ajax;
    }

    /**
     * 个人中心-更改手机号(密码验证接口)
     */
    @PostMapping("/updatePhoneByPwd")
    public AjaxResult updatePhoneByPwd(@RequestBody MemberInfoRequest memberInfoRequest)
    {
        AjaxResult ajax = AjaxResult.success();
        //原密码解密
        try {
            memberInfoRequest.setPassword(RsaUtils.decryptByPrivateKey(privateKey,memberInfoRequest.getPassword()));
        } catch (Exception e) {
            return error("密钥解密失败！");
        }
        try {
            //获取当前登录用户信息比对
            LoginUser loginUser = getLoginUser();
            String password = loginUser.getMemberPassword();
            if (!SecurityUtils.matchesPassword(memberInfoRequest.getPassword(), password)){
                return error("用户密码错误,校验失败！");
            }
            String token = loginUser.getMemberId()+"_"+ IdUtils.fastUUID();
            //缓存到redis中，失效时间为5分钟
            String memberPhoneKey = PHONE_TOKEN_KEY + token;
            redisCache.setCacheObject(memberPhoneKey, "SUCCESS", 5, TimeUnit.MINUTES);
            //返回token
            ajax.put(Constants.TOKEN, token);
        } catch (Exception e){
            return error("修改手机号异常，请联系管理员");
        }
        return ajax;
    }

    /**
     * 个人中心-更改手机号通过token
     */
    @PostMapping("/updatePhoneByToken")
    public AjaxResult updatePhoneByToken(@RequestBody MemberInfoRequest memberInfoRequest)
    {
        String verifyTokenKey = PHONE_TOKEN_KEY + memberInfoRequest.getToken();
        //验证修改手机号的认证token是否失效
        if (redisCache.hasKey(verifyTokenKey)) {
            //验证手机号是否被其他用户使用
            TMember member =iMemberService.selectMemberByPhone(memberInfoRequest.getNewPhone());
            try {
                LoginUser loginUser = getLoginUser();
                String phone = loginUser.getMemberPhone();
                if (member != null){
                    return error("修改用户'" + member.getPhone() + "'失败，新手机号码已注册！");
                }
                if (memberInfoRequest.getNewPhone().equals(phone)){
                    return error("修改用户'" + loginUser.getUsername() + "'失败，手机号码与旧号码相同");
                }
                memberInfoRequest.setOldPhone(this.getLoginUser().getMemberPhone());
                memberInfoService.updatePhone(memberInfoRequest);
                //清除redis的token认证缓存
                redisCache.deleteObject(CacheConstants.PHONE_TOKEN_KEY + memberInfoRequest.getToken());
            } catch (Exception e){
                return error("修改手机号异常，请联系管理员");
            }
        }else{
            return error("修改手机号超时，认证已失效！");
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
        //增加会员昵称及会员头像
        memberInfo.setNickname(member.getNickname());
        memberInfo.setAvatar(member.getAvatar());
        if(member.getParameter() == null || member.getParameter().getPushMessage()==null){
            memberInfo.setState("0");
            return AjaxResult.success(memberInfo);
        }
        memberInfo.setState(member.getParameter().getPushMessage());
        return AjaxResult.success(memberInfo);
    }

    /**
     * 会员头像上传
     */
    @Log(title = "会员头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception
    {
        if (!file.isEmpty())
        {
            LoginUser loginUser = getLoginUser();
            String avatar = FileUploadUtils.upload(VayyarConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
            if (memberInfoService.updateMemberAvatar(loginUser.getMemberPhone(), avatar))
            {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                // 更新缓存会员头像
                loginUser.setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return error("上传图片异常，请联系管理员");
    }


    /**
     * 会员昵称修改
     */
    @Log(title = "会员昵称", businessType = BusinessType.UPDATE)
    @PostMapping("/updateMemInfo")
    public AjaxResult updateMemInfo(@RequestParam("nickname") String nickname)
    {

            LoginUser loginUser = getLoginUser();
            //获取会员id
            Long memberId = loginUser.getMemberId();
            String phone =  loginUser.getMemberPhone();
            if (StringUtils.isEmpty(nickname))
            {
                return error("修改会员'" + phone + "'失败，用户昵称不能为空！");
            }
            TMember member =new TMember();
            member.setMemberId(memberId);
            member.setNickname(nickname);

            if (memberInfoService.updateMemInfo(member) > 0)
            {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("nickname", nickname);
                // 更新缓存用户信息
                loginUser.setNickname(member.getNickname());

                tokenService.setLoginUser(loginUser);
                return ajax;
            }
         return error("修改昵称异常，请联系管理员");
    }
}