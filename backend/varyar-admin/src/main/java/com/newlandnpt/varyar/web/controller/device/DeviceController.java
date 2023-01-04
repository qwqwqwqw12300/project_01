package com.newlandnpt.varyar.web.controller.device;

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
import com.newlandnpt.varyar.system.domain.Device;
import com.newlandnpt.varyar.system.service.IDeviceService;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;

/**
 * 设备Controller
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@RestController
@RequestMapping("device")
public class DeviceController extends BaseController
{
    @Autowired
    private IDeviceService tDeviceService;

    /**
     * 查询设备列表
     */
    @PreAuthorize("@ss.hasPermi('device:list')")
    @GetMapping("/page")
    public TableDataInfo page(Device device)
    {
        startPage();
        List<Device> list = tDeviceService.selectDeviceList(device);
        return getDataTable(list);
    }

    /**
     * 导出设备列表
     */
    @PreAuthorize("@ss.hasPermi('device:export')")
    @Log(title = "设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Device device)
    {
        List<Device> list = tDeviceService.selectDeviceList(device);
        ExcelUtil<Device> util = new ExcelUtil<Device>(Device.class);
        util.exportExcel(response, list, "设备数据");
    }

    /**
     * 获取设备详细信息
     */
    @PreAuthorize("@ss.hasAnyPermi('device:query,member:query')")
    @GetMapping(value = "/{deviceId}")
    public AjaxResult getInfo(@PathVariable("deviceId") Long deviceId)
    {
        return success(tDeviceService.selectDeviceByDeviceId(deviceId));
    }

    /**
     * 新增设备
     */
    @PreAuthorize("@ss.hasPermi('device:add')")
    @Log(title = "设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Device device)
    {//校验设备编号是否存在
        Device oldDevice = tDeviceService.selectByDeviceNo(device.getNo());
        if (oldDevice != null) {
            return error("新增设备'" + device.getNo() + "'失败，设备编号已存在");
        }
        return toAjax(tDeviceService.insertDevice(device));
    }

    /**
     * 分配设备组
     */
    @PreAuthorize("@ss.hasPermi('device:groupArrange')")
    @Log(title = "设备-分配设备组", businessType = BusinessType.UPDATE)
    @PutMapping("{deviceId}/arrange/group/{deviceGroupId}")
    public AjaxResult arrangeDeviceToGroup(@PathVariable Long deviceId,@PathVariable Long deviceGroupId)
    {
        return toAjax(tDeviceService.arrangeDeviceToGroup(Arrays.asList(deviceId).toArray(new Long[1]),deviceGroupId));
    }
    /**
     * 设备激活
     */
    @PreAuthorize("@ss.hasPermi('device:activeOrOffline')")
    @Log(title = "设备-激活", businessType = BusinessType.UPDATE)
    @PutMapping("/{deviceId}/status/active")
    public AjaxResult statusActive(@PathVariable Long deviceId)
    {
        return toAjax(tDeviceService.active(deviceId));
    }

    /**
     * 设备下线
     */
    @PreAuthorize("@ss.hasPermi('device:activeOrOffline')")
    @Log(title = "设备-下线", businessType = BusinessType.UPDATE)
    @PutMapping("/{deviceId}/status/offline")
    public AjaxResult statusOffline(@PathVariable Long deviceId)
    {
        return toAjax(tDeviceService.offline(deviceId));
    }


    /**
     * 设备配对
     */
    @PreAuthorize("@ss.hasPermi('device:associate')")
    @Log(title = "设备-设备配对", businessType = BusinessType.UPDATE)
    @PutMapping("associate")
    public AjaxResult associate(@RequestBody Device device)
    {
        return toAjax(tDeviceService.associate(device));
    }



    /**
     * 删除设备
     */
    @PreAuthorize("@ss.hasPermi('device:remove')")
    @Log(title = "设备", businessType = BusinessType.DELETE)
	@DeleteMapping("/{deviceIds}")
    public AjaxResult remove(@PathVariable Long[] deviceIds)
    {
        return toAjax(tDeviceService.deleteDeviceByDeviceIds(deviceIds));
    }
}