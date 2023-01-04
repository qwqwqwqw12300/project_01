package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.TfamilyRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.system.domain.TFamily;
import com.newlandnpt.varyar.system.domain.TMemberFamily;
import com.newlandnpt.varyar.system.service.ITFamilyService;
import com.newlandnpt.varyar.system.service.ITMemberFamilyService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 家庭
 * 
 * @author newlandnpt
 */
@RestController
@RequestMapping("/api/family")
public class TFamilyController extends BaseController {

    @Autowired
    private ITFamilyService tFamilyService;
    @Autowired
    private ITMemberFamilyService itMemberFamilyService;

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
            @RequestBody @Validated TfamilyRequest tfamilyRequest){
        AjaxResult ajax = AjaxResult.success();
        //获取但当前登录会员id
        Long memberId = getLoginUser().getMemberId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String familyNo = "F"+sdf.format(new Date());
        try {
            //添加 家庭信息
            TFamily tFamily = new TFamily();
            tFamily.setNo(familyNo);
            tFamily.setName(tfamilyRequest.getFamilyName());
            tFamily.setAddress(tfamilyRequest.getAddress());
            tFamily.setDelFlag("0");
            tFamilyService.insertTFamily(tFamily);
            //添加 会员和家庭关联信息
            TMemberFamily tMemberFamily = new TMemberFamily();
            tMemberFamily.setFamilyId(tFamily.getFamilyId());
            tMemberFamily.setMemberId(memberId);
            tMemberFamily.setCreateMemberId(memberId);
            tMemberFamily.setDelFlag("0");
            itMemberFamilyService.insertTMemberFamily(tMemberFamily);
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
            @RequestBody @Validated TfamilyRequest tfamilyRequest){
        AjaxResult ajax = AjaxResult.success();
        if (tfamilyRequest.getFamilyId().equals("")||tfamilyRequest.getFamilyId()==null){
            ajax = AjaxResult.error("修改我的家庭家庭Id不能为空！");
            return ajax;
        }
        //查询我的家庭（需要修改的）
        TFamily tFamily =  tFamilyService.selectTFamilyByFamilyId(Long.valueOf(tfamilyRequest.getFamilyId()));
        if( tFamily == null){
            ajax = AjaxResult.error("无法查到需要修改的记录！");
            return ajax;
        }
        try {
            tFamily.setName(tfamilyRequest.getFamilyName());
            tFamily.setAddress(tfamilyRequest.getAddress());
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
            @RequestBody @Validated TfamilyRequest tfamilyRequest){
        AjaxResult ajax = AjaxResult.success();
        //查询我的家庭（需要修改的）
        TFamily tFamily =  tFamilyService.selectTFamilyByFamilyId(Long.valueOf(tfamilyRequest.getFamilyId()));
        if( tFamily == null){
            ajax = AjaxResult.error("无法查到需要修改的记录！");
            return ajax;
        }
        try {
            tFamily.setDelFlag("2");
            tFamilyService.updateTFamily(tFamily);

        } catch (Exception e){
            ajax = AjaxResult.error("删除我的家庭失败！");
            return ajax;
        }
        return ajax;
    }
}
