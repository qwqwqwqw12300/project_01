package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.FamilyRequest;
import com.newlandnpt.varyar.common.core.domain.model.ShareFamilyRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.user.CaptchaException;
import com.newlandnpt.varyar.common.exception.user.CaptchaExpireException;
import com.newlandnpt.varyar.system.domain.TFamily;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.domain.TMemberFamily;
import com.newlandnpt.varyar.system.service.IFamilyService;
import com.newlandnpt.varyar.system.service.IMemberFamilyService;
import com.newlandnpt.varyar.system.service.IMemberInfoService;
import com.newlandnpt.varyar.system.service.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 家庭
 * 
 * @author newlandnpt
 */
@RestController
@RequestMapping("/api/family")
public class FamilyController extends BaseController {

    @Autowired
    private IFamilyService tFamilyService;

    @Autowired
    private IMemberFamilyService iMemberFamilyService;

    @Autowired
    private IMemberService iMemberService;

    @Autowired
    private RedisCache redisCache;
    /**
    * 获取家庭列表
    * */
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        Long memberId = getLoginUser().getMemberId();
        List<TFamily> list = tFamilyService.selectMembersFamilyList(memberId);
        return getDataTable(list);
    }
    /**
     * 创建我的家庭
     * */
    @PostMapping("/creFamily")
    public AjaxResult createFamily(
            @RequestBody @Validated FamilyRequest familyRequest){
        AjaxResult ajax = AjaxResult.success();
        if (familyRequest.getFamilyName().equals("")|| familyRequest.getFamilyName()==null){
            ajax = ajax.error("家庭名称不能为空！");
            return ajax;
        }
        //获取但当前登录会员id
        Long memberId = getLoginUser().getMemberId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String familyNo = "F"+sdf.format(new Date());
        try {
            //添加 家庭信息
            TFamily tFamily = new TFamily();
            tFamily.setNo(familyNo);
            tFamily.setName(familyRequest.getFamilyName());
            tFamily.setAddress(familyRequest.getAddress());
            tFamily.setDelFlag("0");
            tFamily.setShareFlag("2");
            tFamily.setCreateById(String.valueOf(memberId));
            tFamilyService.insertTFamily(tFamily,memberId);
        } catch (Exception e){
            ajax = ajax.error("新增我的家庭失败！");
            return ajax;
        }
        return ajax;
    }
    /*
    * 修改我的家庭
    * */
    @PostMapping("/editFamily")
    public AjaxResult editFamily(
            @RequestBody @Validated FamilyRequest familyRequest){
        AjaxResult ajax = AjaxResult.success();
        if (familyRequest.getFamilyId().equals("")|| familyRequest.getFamilyId()==null){
            ajax = ajax.error("家庭Id不能为空！");
            return ajax;
        }
        //查询我的家庭（需要修改的）
        TFamily tFamily =  tFamilyService.selectTFamilyByFamilyId(Long.valueOf(familyRequest.getFamilyId()));
        if( tFamily == null){
            ajax = ajax.error("无法查到需要修改的记录！");
            return ajax;
        }
        if(!tFamily.getCreateById().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            ajax = ajax.error("非创建者无权限修改！");
            return ajax;
        }
        try {
            tFamily.setName(familyRequest.getFamilyName());
            tFamily.setAddress(familyRequest.getAddress());
            tFamilyService.updateTFamily(tFamily);
        } catch (Exception e){
            ajax = ajax.error("修改我的家庭失败！");
            return ajax;
        }
        return ajax;
    }
    /*
     * 删除我的家庭
     * */
    @PostMapping("/remFamily")
    public AjaxResult removeFamily(
            @RequestBody @Validated FamilyRequest familyRequest){
        AjaxResult ajax = AjaxResult.success();
        if (familyRequest.getFamilyId().equals("")|| familyRequest.getFamilyId()==null){
            ajax = ajax.error("家庭Id不能为空！");
            return ajax;
        }
        //查询我的家庭（需要修改的）
        TFamily tFamily =  tFamilyService.selectTFamilyByFamilyId(Long.valueOf(familyRequest.getFamilyId()));
        if( tFamily == null){
            ajax = ajax.error("无法查到需要修改的记录！");
            return ajax;
        }
        if(!tFamily.getCreateById().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            ajax = ajax.error("非创建者无权限删除！");
            return ajax;
        }
        try {
            tFamilyService.deleteTFamilyByFamilyId(tFamily.getFamilyId());
        } catch (Exception e){
            ajax = ajax.error("删除我的家庭失败！");
            return ajax;
        }
        return ajax;
    }
    /**
     * 创建家庭分享
     * */
    @PostMapping("/shareFamily")
    public AjaxResult shareFamily(
            @RequestBody @Validated ShareFamilyRequest shareFamilyRequest){
        AjaxResult ajax = AjaxResult.success();
        // check sms
        String verifyKey = CacheConstants.SMS_CODE_KEY + shareFamilyRequest.getSmsUuid();
        String code = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (code == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(shareFamilyRequest.getCode())) {
            throw new CaptchaException();
        }
        //查询我的家庭（需要修改的）
        TFamily tFamily =  tFamilyService.selectTFamilyByFamilyId(Long.valueOf(shareFamilyRequest.getFamilyId()));
        if( tFamily == null){
            ajax = ajax.error("无法查到家庭的记录！");
            return ajax;
        }
        if(!tFamily.getCreateById().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            ajax = ajax.error("非创建者无权分享！");
            return ajax;
        }
        //用手机号查询是否有此用户
        TMember member = iMemberService.selectMemberByPhone(shareFamilyRequest.getPhone());
        TMemberFamily tMemberFamily = new TMemberFamily();
        if(member == null){
            tMemberFamily.setPhone(shareFamilyRequest.getPhone());
            tMemberFamily.setMemberId(Long.valueOf(shareFamilyRequest.getPhone()));
        }else{
            tMemberFamily.setMemberId(member.getMemberId());
        }
        Long memberId = this.getLoginUser().getMemberId();
        tMemberFamily.setCreateMemberId(memberId);
        tMemberFamily.setShareFlag("0");
        tMemberFamily.setDelFlag("0");
        tMemberFamily.setFamilyId(Long.valueOf(shareFamilyRequest.getFamilyId()));
        try {
            iMemberFamilyService.insertTMemberFamily(tMemberFamily);
        } catch (Exception e){
            ajax = ajax.error("分享我的家庭失败！");
            return ajax;
        }
        return ajax;
    }
    /**
     * 删除家庭分享
     * */
    @PostMapping("/remShareFamily")
    public AjaxResult removeshareFamily(
            @RequestBody @Validated FamilyRequest familyRequest){
        AjaxResult ajax = AjaxResult.success();
        if (familyRequest.getFamilyId().equals("")|| familyRequest.getFamilyId()==null){
            ajax = ajax.error("家庭Id不能为空！");
            return ajax;
        }
        //查询我的家庭（需要修改的）
        TFamily tFamily =  tFamilyService.selectTFamilyByFamilyId(Long.valueOf(familyRequest.getFamilyId()));
        if( tFamily == null){
            ajax = ajax.error("无法查到需要修改的记录！");
            return ajax;
        }
        if(!tFamily.getCreateById().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            ajax = ajax.error("非创建者无权删除！");
            return ajax;
        }
        TMemberFamily tMemberFamily = new TMemberFamily();
        tMemberFamily.setFamilyId(Long.valueOf(familyRequest.getFamilyId()));
        tMemberFamily.setCreateMemberId(Long.valueOf(this.getLoginUser().getMemberId()));
        List<TMemberFamily> tMemberFamilys = iMemberFamilyService.selectTMemberFamilyList(tMemberFamily);
        if (tMemberFamilys==null||tMemberFamilys.size()==0){
            ajax = ajax.error("无分享记录！");
            return ajax;
        }
        for (TMemberFamily item:tMemberFamilys){
            if (!String.valueOf(item.getCreateMemberId()).equals(String.valueOf(item.getMemberId()))){
                try {
                    iMemberFamilyService.deleteTMemberFamilyByMemberFamilyId(item.getMemberFamilyId());
                } catch (Exception e){
                    ajax = ajax.error("删除分享我的家庭失败！");
                    return ajax;
                }
            }
        }
        return ajax;
    }

    /**
     * 获取家庭列表-查询已分享家庭列表
     * */
    @PostMapping("/sharelist")
    public AjaxResult shareList(@RequestBody @Validated FamilyRequest familyRequest) {
        AjaxResult ajax = AjaxResult.success();
        if (familyRequest.getFamilyId().equals("")|| familyRequest.getFamilyId()==null){
            ajax = ajax.error("家庭Id不能为空！");
            return ajax;
        }
        Long memberId = getLoginUser().getMemberId();
        TMemberFamily tMemberFamily = new TMemberFamily();
        tMemberFamily.setFamilyId(Long.valueOf(familyRequest.getFamilyId()));
        tMemberFamily.setCreateMemberId(memberId);
        tMemberFamily.setShareFlag("0");
        List<TMemberFamily> list = iMemberFamilyService.selectTMemberFamilyList(tMemberFamily);
        for (TMemberFamily item : list ){
            if(item.getUserName()==null||item.getUserName().equals("")){
                item.setUserName(item.getPhone());
            }
        }
        return AjaxResult.success(list);
    }
}
