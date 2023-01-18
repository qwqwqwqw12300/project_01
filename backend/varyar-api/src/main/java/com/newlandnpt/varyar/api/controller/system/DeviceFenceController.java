package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.DeviceFenceRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.domain.TDeviceFence;
import com.newlandnpt.varyar.system.service.IDeviceFenceService;
import com.newlandnpt.varyar.system.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private IDeviceService iDeviceService;
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
    @GetMapping(value = "/getDeFeInfo")
    public AjaxResult getInfo(@RequestBody @Validated DeviceFenceRequest deviceFenceRequest)
    {
        return success(deviceFenceService.selectTDeviceFenceByDeviceFenceId(Long.valueOf(deviceFenceRequest.getDeviceFenceId())));
    }

    /**
     * 新增设备电子围栏
     */
    @PostMapping("/addFence")
    public AjaxResult add(@RequestBody TDeviceFence tDeviceFence)
    {
        //获取设备id
        Long deviceFenceId  =  tDeviceFence.getDeviceFenceId();
        TDevice tdevice =  iDeviceService.selectDeviceByDeviceId(deviceFenceId);
        if (!tdevice.getMemberId().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            throw new ServiceException("无权限设置本设备！");
        }
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
