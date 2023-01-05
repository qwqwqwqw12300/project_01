package com.newlandnpt.varyar.api.controller.system;


import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.system.service.IAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 协议管理Controller
 * 
 * @author chenxw1
 * @date 2023-01-04
 */
@RestController
@RequestMapping("/api/agreement")
public class AgreementController extends BaseController
{
    @Autowired
    private IAgreementService agreementService;

    /**
     * 通过协议类型获取协议详细信息:0隐私协议 1app协议
     */
    @PostMapping(value = "/getAgreementInfo")
    public AjaxResult getInfo(@RequestParam("agreementType") String agreementType)
    {
        return success(agreementService.selectTAgreementByAgreementType(agreementType));
    }

    /**
     * 匿名访问获取用户协议:1app协议
     */
    @PostMapping(value = "/selectAgreement")
    public AjaxResult selectAgreement()
    {
        //用户app接入协议类型
        String agreementType="1";
        return success(agreementService.selectTAgreementByAgreementType(agreementType));
    }
}