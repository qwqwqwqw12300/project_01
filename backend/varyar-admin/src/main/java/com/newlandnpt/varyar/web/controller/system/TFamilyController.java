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
import com.newlandnpt.varyar.system.domain.TFamily;
import com.newlandnpt.varyar.system.service.ITFamilyService;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;

/**
 * 家庭Controller
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@RestController
@RequestMapping("/system/family")
public class TFamilyController extends BaseController
{
    @Autowired
    private ITFamilyService tFamilyService;

    /**
     * 查询家庭列表
     */
    @PreAuthorize("@ss.hasPermi('system:family:list')")
    @GetMapping("/list")
    public TableDataInfo list(TFamily tFamily)
    {
        startPage();
        List<TFamily> list = tFamilyService.selectTFamilyList(tFamily);
        return getDataTable(list);
    }

    /**
     * 导出家庭列表
     */
    @PreAuthorize("@ss.hasPermi('system:family:export')")
    @Log(title = "家庭", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TFamily tFamily)
    {
        List<TFamily> list = tFamilyService.selectTFamilyList(tFamily);
        ExcelUtil<TFamily> util = new ExcelUtil<TFamily>(TFamily.class);
        util.exportExcel(response, list, "家庭数据");
    }

    /**
     * 获取家庭详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:family:query')")
    @GetMapping(value = "/{familyId}")
    public AjaxResult getInfo(@PathVariable("familyId") Long familyId)
    {
        return success(tFamilyService.selectTFamilyByFamilyId(familyId));
    }

    /**
     * 新增家庭
     */
    @PreAuthorize("@ss.hasPermi('system:family:add')")
    @Log(title = "家庭", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TFamily tFamily)
    {
//        return toAjax(tFamilyService.insertTFamily(tFamily));
        return null;
    }

    /**
     * 修改家庭
     */
    @PreAuthorize("@ss.hasPermi('system:family:edit')")
    @Log(title = "家庭", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TFamily tFamily)
    {
        return toAjax(tFamilyService.updateTFamily(tFamily));
    }

    /**
     * 删除家庭
     */
    @PreAuthorize("@ss.hasPermi('system:family:remove')")
    @Log(title = "家庭", businessType = BusinessType.DELETE)
	@DeleteMapping("/{familyIds}")
    public AjaxResult remove(@PathVariable Long[] familyIds)
    {
        return toAjax(tFamilyService.deleteTFamilyByFamilyIds(familyIds));
    }
}
