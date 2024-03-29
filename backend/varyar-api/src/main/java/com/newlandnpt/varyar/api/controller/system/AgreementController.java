package com.newlandnpt.varyar.api.controller.system;


import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.system.domain.TAgreement;
import com.newlandnpt.varyar.system.service.IAgreementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 协议管理Controller
 * 
 * @author chenxw1
 * @date 2023-01-04
 */
@Api(tags = "协议")
@RestController
@RequestMapping("/api/agreement")
public class AgreementController extends BaseController
{
    @Autowired
    private IAgreementService agreementService;

    @ApiOperation("通过协议类型获取协议详细信息:0隐私协议 1app协议")
    @PostMapping(value = "/getAgreementInfo")
    public AjaxResult getInfo(@RequestBody @Validated TAgreement tAgreement){
        if (tAgreement.getType() ==null || tAgreement.getType().equals("")){
            return error("协议类型不能为空！");
        }
        tAgreement =agreementService.selectTAgreementByAgreementType(tAgreement.getType());
        if (tAgreement ==null){
            tAgreement = new TAgreement();
        }
        return success(tAgreement);
    }

    @ApiOperation("匿名访问获取用户协议:1app协议")
    @PostMapping(value = "/selectAgreement")
    public AjaxResult selectAgreement()
    {
        //用户app接入协议类型
        String agreementType="1";
        TAgreement tAgreement = agreementService.selectTAgreementByAgreementType(agreementType);
        return AjaxResult.success(tAgreement);
    }
}
