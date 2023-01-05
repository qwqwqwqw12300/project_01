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
import com.newlandnpt.varyar.system.domain.TVersion;
import com.newlandnpt.varyar.system.service.IVersionService;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;

/**
 * 版本Controller
 * 
 * @author chenxw
 * @date 2022-12-24
 */
@RestController
@RequestMapping("/system/version")
public class VersionController extends BaseController
{
    @Autowired
    private IVersionService versionService;

    /**
     * 查询版本列表
     */
    @PreAuthorize("@ss.hasPermi('system:version:list')")
    @GetMapping("/list")
    public TableDataInfo list(TVersion tVersion)
    {
        startPage();
        List<TVersion> list = versionService.selectTVersionList(tVersion);
        return getDataTable(list);
    }

    /**
     * 导出版本列表
     */
    @PreAuthorize("@ss.hasPermi('system:version:export')")
    @Log(title = "版本", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TVersion tVersion)
    {
        List<TVersion> list = versionService.selectTVersionList(tVersion);
        ExcelUtil<TVersion> util = new ExcelUtil<TVersion>(TVersion.class);
        util.exportExcel(response, list, "版本数据");
    }

    /**
     * 获取版本详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:version:query')")
    @GetMapping(value = "/{versionId}")
    public AjaxResult getInfo(@PathVariable("versionId") Long versionId)
    {
        return success(versionService.selectTVersionByVersionId(versionId));
    }

    /**
     * 新增版本
     */
    @PreAuthorize("@ss.hasPermi('system:version:add')")
    @Log(title = "版本", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TVersion tVersion)
    {
        return toAjax(versionService.insertTVersion(tVersion));
    }

    /**
     * 修改版本
     */
    @PreAuthorize("@ss.hasPermi('system:version:edit')")
    @Log(title = "版本", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TVersion tVersion)
    {
        return toAjax(versionService.updateTVersion(tVersion));
    }

    /**
     * 删除版本
     */
    @PreAuthorize("@ss.hasPermi('system:version:remove')")
    @Log(title = "版本", businessType = BusinessType.DELETE)
	@DeleteMapping("/{versionIds}")
    public AjaxResult remove(@PathVariable Long[] versionIds)
    {
        return toAjax(versionService.deleteTVersionByVersionIds(versionIds));
    }
}
