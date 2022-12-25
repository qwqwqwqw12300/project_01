package com.newlandnpt.varyar.web.controller.system;

import java.util.Arrays;
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
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.ITDeviceService;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;

/**
 * 设备Controller
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@RestController
@RequestMapping("/system/device")
public class TDeviceController extends BaseController
{
    @Autowired
    private ITDeviceService tDeviceService;

    /**
     * 查询设备列表
     */
    @PreAuthorize("@ss.hasPermi('system:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(TDevice tDevice)
    {
        startPage();
        List<TDevice> list = tDeviceService.selectTDeviceList(tDevice);
        return getDataTable(list);
    }

    /**
     * 导出设备列表
     */
    @PreAuthorize("@ss.hasPermi('system:device:export')")
    @Log(title = "设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TDevice tDevice)
    {
        List<TDevice> list = tDeviceService.selectTDeviceList(tDevice);
        ExcelUtil<TDevice> util = new ExcelUtil<TDevice>(TDevice.class);
        util.exportExcel(response, list, "设备数据");
    }

    /**
     * 获取设备详细信息
     */
    @PreAuthorize("@ss.hasAnyPermi('system:device:query,system:member:query')")
    @GetMapping(value = "/{deviceId}")
    public AjaxResult getInfo(@PathVariable("deviceId") Long deviceId)
    {
        return success(tDeviceService.selectTDeviceByDeviceId(deviceId));
    }

    /**
     * 新增设备
     */
    @PreAuthorize("@ss.hasPermi('system:device:add')")
    @Log(title = "设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TDevice tDevice)
    {
        return toAjax(tDeviceService.insertTDevice(tDevice));
    }

    /**
     * 分配设备组
     */
    @PreAuthorize("@ss.hasPermi('system:devicegroup:deviceArrange')")
    @Log(title = "设备-分配设备组", businessType = BusinessType.UPDATE)
    @PutMapping("{}/arrange/group/{deviceGroupId}")
    public AjaxResult arrangeDeviceToGroup(@PathVariable Long deviceId,@PathVariable Long deviceGroupId)
    {
        return toAjax(tDeviceService.arrangeDeviceToGroup(Arrays.asList(deviceId).toArray(new Long[1]),deviceGroupId));
    }


    /**
     * 设备配对
     */
    @PreAuthorize("@ss.hasPermi('system:devicegroup:deviceArrange')")
    @Log(title = "设备-分配设备组", businessType = BusinessType.UPDATE)
    @PutMapping("associate")
    public AjaxResult associate(@RequestBody TDevice tDevice)
    {
        return toAjax(tDeviceService.associate(tDevice));
    }



    /**
     * 删除设备
     */
    @PreAuthorize("@ss.hasPermi('system:device:remove')")
    @Log(title = "设备", businessType = BusinessType.DELETE)
	@DeleteMapping("/{deviceIds}")
    public AjaxResult remove(@PathVariable Long[] deviceIds)
    {
        return toAjax(tDeviceService.deleteTDeviceByDeviceIds(deviceIds));
    }
}
