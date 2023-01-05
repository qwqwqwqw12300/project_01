package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.FamilyRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.system.domain.TFamily;
import com.newlandnpt.varyar.system.service.IFamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
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

    @GetMapping("/list")
    public TableDataInfo list( TFamily tFamily) {
        startPage();
        String memberPhone = getLoginUser().getMemberPhone();
        Long memberId = getLoginUser().getMemberId();
        List<TFamily> list = tFamilyService.selectMembersFamilyList(memberId);
        return getDataTable(list);
    }
/*
* 创建我的家庭
* */
    @PostMapping("/creFamily")
    public AjaxResult createFamily(
            @RequestBody @Validated FamilyRequest familyRequest){
        AjaxResult ajax = AjaxResult.success();
        if (familyRequest.getFamilyName().equals("")|| familyRequest.getFamilyName()==null){
            ajax = AjaxResult.error("家庭名称不能为空！");
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
            tFamilyService.insertTFamily(tFamily,memberId);
        } catch (Exception e){
            ajax = AjaxResult.error("新增我的家庭失败！");
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
            ajax = AjaxResult.error("家庭Id不能为空！");
            return ajax;
        }
        //查询我的家庭（需要修改的）
        TFamily tFamily =  tFamilyService.selectTFamilyByFamilyId(Long.valueOf(familyRequest.getFamilyId()));
        if( tFamily == null){
            ajax = AjaxResult.error("无法查到需要修改的记录！");
            return ajax;
        }
        try {
            tFamily.setName(familyRequest.getFamilyName());
            tFamily.setAddress(familyRequest.getAddress());
            tFamilyService.updateTFamily(tFamily);
        } catch (Exception e){
            ajax = AjaxResult.error("修改我的家庭失败！");
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
            ajax = AjaxResult.error("家庭Id不能为空！");
            return ajax;
        }
        //查询我的家庭（需要修改的）
        TFamily tFamily =  tFamilyService.selectTFamilyByFamilyId(Long.valueOf(familyRequest.getFamilyId()));
        if( tFamily == null){
            ajax = AjaxResult.error("无法查到需要修改的记录！");
            return ajax;
        }
        try {
            tFamilyService.deleteTFamilyByFamilyId(tFamily.getFamilyId());
        } catch (Exception e){
            ajax = AjaxResult.error("删除我的家庭失败！");
            return ajax;
        }
        return ajax;
    }
}
