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
import com.newlandnpt.varyar.system.domain.TAdvise;
import com.newlandnpt.varyar.system.service.ITAdviseService;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;

/**
 * 建议Controller
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@RestController
@RequestMapping("/system/advise")
public class TAdviseController extends BaseController
{
    @Autowired
    private ITAdviseService tAdviseService;

    /**
     * 查询建议列表
     */
    @PreAuthorize("@ss.hasPermi('system:advise:list')")
    @GetMapping("/list")
    public TableDataInfo list(TAdvise tAdvise)
    {
        startPage();
        List<TAdvise> list = tAdviseService.selectTAdviseList(tAdvise);
        return getDataTable(list);
    }

    /**
     * 导出建议列表
     */
    @PreAuthorize("@ss.hasPermi('system:advise:export')")
    @Log(title = "建议", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAdvise tAdvise)
    {
        List<TAdvise> list = tAdviseService.selectTAdviseList(tAdvise);
        ExcelUtil<TAdvise> util = new ExcelUtil<TAdvise>(TAdvise.class);
        util.exportExcel(response, list, "建议数据");
    }

    /**
     * 获取建议详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:advise:query')")
    @GetMapping(value = "/{adviseId}")
    public AjaxResult getInfo(@PathVariable("adviseId") Long adviseId)
    {
        return success(tAdviseService.selectTAdviseByAdviseId(adviseId));
    }

    /**
     * 新增建议
     */
    @PreAuthorize("@ss.hasPermi('system:advise:add')")
    @Log(title = "建议", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TAdvise tAdvise)
    {
        return toAjax(tAdviseService.insertTAdvise(tAdvise));
    }

    /**
     * 修改建议
     */
    @PreAuthorize("@ss.hasPermi('system:advise:edit')")
    @Log(title = "建议", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TAdvise tAdvise)
    {
        return toAjax(tAdviseService.updateTAdvise(tAdvise));
    }

    /**
     * 删除建议
     */
    @PreAuthorize("@ss.hasPermi('system:advise:remove')")
    @Log(title = "建议", businessType = BusinessType.DELETE)
	@DeleteMapping("/{adviseIds}")
    public AjaxResult remove(@PathVariable Long[] adviseIds)
    {
        return toAjax(tAdviseService.deleteTAdviseByAdviseIds(adviseIds));
    }
}
