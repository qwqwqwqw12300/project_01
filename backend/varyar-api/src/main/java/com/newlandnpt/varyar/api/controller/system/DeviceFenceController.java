package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import com.newlandnpt.varyar.system.domain.TDeviceFence;
import com.newlandnpt.varyar.system.service.IDeviceFenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 设备电子围栏Controller
 *
 * @author chenxw
 * @date 2023-01-06
 */
@RestController
    @RequestMapping("/api/fence")
public class DeviceFenceController extends BaseController
{
    @Autowired
    private IDeviceFenceService deviceFenceService;

    /**
     * 查询设备电子围栏列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TDeviceFence tDeviceFence)
    {
        startPage();
        List<TDeviceFence> list = deviceFenceService.selectTDeviceFenceList(tDeviceFence);
        return getDataTable(list);
    }



    /**
     * 获取设备电子围栏详细信息
     */
    @GetMapping(value = "/{deviceFenceId}")
    public AjaxResult getInfo(@PathVariable("deviceFenceId") Long deviceFenceId)
    {
        return success(deviceFenceService.selectTDeviceFenceByDeviceFenceId(deviceFenceId));
    }

    /**
     * 新增设备电子围栏
     */
    @PostMapping("/addFence")
    public AjaxResult add(@RequestBody TDeviceFence tDeviceFence)
    {
        //获取设备号
        Long deviceFenceId  =  tDeviceFence.getDeviceFenceId();
        return toAjax(deviceFenceService.insertTDeviceFence(tDeviceFence));
    }

    /**
     * 修改设备电子围栏
     */
    @PutMapping
    public AjaxResult edit(@RequestBody TDeviceFence tDeviceFence)
    {
        return toAjax(deviceFenceService.updateTDeviceFence(tDeviceFence));
    }

    /**
     * 删除设备电子围栏
     */
    @DeleteMapping("/{deviceFenceIds}")
    public AjaxResult remove(@PathVariable Long[] deviceFenceIds)
    {
        return toAjax(deviceFenceService.deleteTDeviceFenceByDeviceFenceIds(deviceFenceIds));
    }
}
