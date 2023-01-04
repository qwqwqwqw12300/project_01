package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.annotation.Anonymous;
import com.newlandnpt.varyar.common.annotation.Log;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.common.enums.BusinessType;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import com.newlandnpt.varyar.system.domain.TAgreement;
import com.newlandnpt.varyar.system.service.ITAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 协议Controller
 * 
 * @author chenxw1
 * @date 2022-12-30
 */
@RestController
@RequestMapping("/api/agreement")
public class TAgreementController extends BaseController
{
    @Autowired
    private ITAgreementService tAgreementService;

    /**
     * 通过协议类型获取协议详细信息
     */
    @PostMapping(value = "/getAgreementInfo")
    public AjaxResult getInfo(@RequestParam("agreementType") String agreementType)
    {
        return success(tAgreementService.selectTAgreementByAgreementType(agreementType));
    }

}
