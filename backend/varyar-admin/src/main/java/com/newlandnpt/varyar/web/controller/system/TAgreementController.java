package com.newlandnpt.varyar.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.newlandnpt.varyar.common.annotation.Log;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.enums.BusinessType;
import com.newlandnpt.varyar.system.domain.TAgreement;
import com.newlandnpt.varyar.system.service.ITAgreementService;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;

/**
 * 协议Controller
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@RestController
@RequestMapping("/system/agreement")
public class TAgreementController extends BaseController
{
    @Autowired
    private ITAgreementService tAgreementService;

    /**
     * 查询协议列表
     */
    @PreAuthorize("@ss.hasPermi('system:agreement:list')")
    @GetMapping("/list")
    public TableDataInfo list(TAgreement tAgreement)
    {
        startPage();
        List<TAgreement> list = tAgreementService.selectTAgreementList(tAgreement);
        setType(list);
        return getDataTable(list);
    }
    private void setType(List<TAgreement> list){
        for(TAgreement item : list){
            item.setType(item.getType().equals("0")?"隐私协议":"APP协议");
        }
    }

    /**
     * 导出协议列表
     */
    @PreAuthorize("@ss.hasPermi('system:agreement:export')")
    @Log(title = "协议", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAgreement tAgreement)
    {
        List<TAgreement> list = tAgreementService.selectTAgreementList(tAgreement);
        ExcelUtil<TAgreement> util = new ExcelUtil<TAgreement>(TAgreement.class);
        util.exportExcel(response, list, "协议数据");
    }

    /**
     * 获取协议详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:agreement:query')")
    @GetMapping(value = "/{agreementId}")
    public AjaxResult getInfo(@PathVariable("agreementId") Long agreementId)
    {
        return success(tAgreementService.selectTAgreementByAgreementId(agreementId));
    }

    /**
     * 新增协议
     */
    @PreAuthorize("@ss.hasPermi('system:agreement:add')")
    @Log(title = "协议", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TAgreement tAgreement)
    {
        return toAjax(tAgreementService.insertTAgreement(tAgreement));
    }

    /**
     * 修改协议
     */
    @PreAuthorize("@ss.hasPermi('system:agreement:edit')")
    @Log(title = "协议", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TAgreement tAgreement)
    {
        return toAjax(tAgreementService.updateTAgreement(tAgreement));
    }

    /**
     * 删除协议
     */
    @PreAuthorize("@ss.hasPermi('system:agreement:remove')")
    @Log(title = "协议", businessType = BusinessType.DELETE)
	@DeleteMapping("/{agreementIds}")
    public AjaxResult remove(@PathVariable Long[] agreementIds)
    {
        return toAjax(tAgreementService.deleteTAgreementByAgreementIds(agreementIds));
    }
}
