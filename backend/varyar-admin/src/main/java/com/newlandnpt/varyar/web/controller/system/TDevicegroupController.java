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
import com.newlandnpt.varyar.system.domain.TDevicegroup;
import com.newlandnpt.varyar.system.service.ITDevicegroupService;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;

/**
 * 设备组Controller
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@RestController
@RequestMapping("/system/devicegroup")
public class TDevicegroupController extends BaseController
{
    @Autowired
    private ITDevicegroupService tDevicegroupService;

    /**
     * 查询设备组列表
     */
    @PreAuthorize("@ss.hasPermi('system:devicegroup:list')")
    @GetMapping("/list")
    public TableDataInfo list(TDevicegroup tDevicegroup)
    {
        startPage();
        List<TDevicegroup> list = tDevicegroupService.selectTDevicegroupList(tDevicegroup);
        return getDataTable(list);
    }

    /**
     * 导出设备组列表
     */
    @PreAuthorize("@ss.hasPermi('system:devicegroup:export')")
    @Log(title = "设备组", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TDevicegroup tDevicegroup)
    {
        List<TDevicegroup> list = tDevicegroupService.selectTDevicegroupList(tDevicegroup);
        ExcelUtil<TDevicegroup> util = new ExcelUtil<TDevicegroup>(TDevicegroup.class);
        util.exportExcel(response, list, "设备组数据");
    }

    /**
     * 获取设备组详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:devicegroup:query')")
    @GetMapping(value = "/{devicegroupId}")
    public AjaxResult getInfo(@PathVariable("devicegroupId") Long devicegroupId)
    {
        return success(tDevicegroupService.selectTDevicegroupByDevicegroupId(devicegroupId));
    }

    /**
     * 新增设备组
     */
    @PreAuthorize("@ss.hasPermi('system:devicegroup:add')")
    @Log(title = "设备组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TDevicegroup tDevicegroup)
    {
        return toAjax(tDevicegroupService.insertTDevicegroup(tDevicegroup));
    }

    /**
     * 修改设备组
     */
    @PreAuthorize("@ss.hasPermi('system:devicegroup:edit')")
    @Log(title = "设备组", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TDevicegroup tDevicegroup)
    {
        return toAjax(tDevicegroupService.updateTDevicegroup(tDevicegroup));
    }

    /**
     * 删除设备组
     */
    @PreAuthorize("@ss.hasPermi('system:devicegroup:remove')")
    @Log(title = "设备组", businessType = BusinessType.DELETE)
	@DeleteMapping("/{devicegroupIds}")
    public AjaxResult remove(@PathVariable Long[] devicegroupIds)
    {
        return toAjax(tDevicegroupService.deleteTDevicegroupByDevicegroupIds(devicegroupIds));
    }
}
