package com.newlandnpt.varyar.web.controller.device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.newlandnpt.varyar.common.core.domain.entity.DevicePhone;
import com.newlandnpt.varyar.common.core.domain.model.DevicePhoneRequest;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.system.service.IOrgService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
    @Autowired
    private IOrgService orgService;

    /**
     * 查询设备列表
     */
    @GetMapping("/page")
    public TableDataInfo page(TDevice device)
    {
        startPage();
        List<TDevice> list = tDeviceService.selectDeviceList(device);
        return getDataTable(list);
    }

    /**
     * 导出设备列表
     */
    @PreAuthorize("@ss.hasPermi('device:export')")
    @Log(title = "设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TDevice device)
    {
        List<TDevice> list = tDeviceService.selectDeviceList(device);
        ExcelUtil<TDevice> util = new ExcelUtil<TDevice>(TDevice.class);
        util.exportExcel(response, list, "设备数据");
    }

    /**
     * 获取设备详细信息
     */
    @GetMapping(value = "/{deviceId}")
    public AjaxResult getInfo(@PathVariable("deviceId") Long deviceId)
    {
        //return success(tDeviceService.selectDeviceByDeviceId(deviceId));
        TDevice device =tDeviceService.selectDeviceByDeviceId(deviceId);
        TDevice devices = tDeviceService.loadingDeviceStauts(device);
        return success(devices);
    }

    /**
     * 获取设备参数配置
     */
    @GetMapping(value = "/{deviceId}/settings")
    public AjaxResult getSettings(@PathVariable("deviceId") Long deviceId)
    {
        return success(tDeviceService.loadSettings(deviceId));
    }

    /**
     * 设备参数配置
     */
    @PreAuthorize("@ss.hasPermi('device:settings')")
    @Log(title = "设备参数", businessType = BusinessType.UPDATE)
    @PutMapping("/{deviceId}/settings")
    public AjaxResult setSettings(@PathVariable("deviceId") Long deviceId,@RequestBody TDevice.DeviceParameter deviceSettings)
    {
        return toAjax(tDeviceService.setSettings(deviceId,deviceSettings));
    }

    /**
     * 新增设备
     */
    @PreAuthorize("@ss.hasPermi('device:add')")
    @Log(title = "设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TDevice device)
    {//校验设备编号是否存在
        TDevice oldDevice = tDeviceService.selectByDeviceNo(device.getNo());
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
    @PutMapping({"{deviceId}/arrange/group/{deviceGroupId}","{deviceId}/arrange/group"})
    public AjaxResult arrangeDeviceToGroup(@PathVariable Long deviceId,@PathVariable(required = false) Long deviceGroupId)
    {

        TDevice device = tDeviceService.selectDeviceByDeviceId(deviceId);
        if(device!=null&&device.getOrgId()!=null){
            orgService.checkOrgDataScope(device.getOrgId());
        }
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
        TDevice device = tDeviceService.selectDeviceByDeviceId(deviceId);
        if(device!=null&&device.getOrgId()!=null){
            orgService.checkOrgDataScope(device.getOrgId());
        }
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
        TDevice device = tDeviceService.selectDeviceByDeviceId(deviceId);
        if(device!=null&&device.getOrgId()!=null){
            orgService.checkOrgDataScope(device.getOrgId());
        }
        return toAjax(tDeviceService.offline(deviceId));
    }

    /**
     * 设备配对
     */
    @PreAuthorize("@ss.hasPermi('device:associate')")
    @Log(title = "设备-设备配对", businessType = BusinessType.UPDATE)
    @PutMapping("associate")
    public AjaxResult associate(@RequestBody TDevice device)
    {
        //执行设备配对时校验数据权限
        if(device.getOrgId()!=null)
            orgService.checkOrgDataScope(device.getOrgId());
        return toAjax(tDeviceService.associate(device));
    }

    /**
     * 设置牵挂电子卡的电话列表
     * */
    @PostMapping("/setSOSDevicePhone")
    public AjaxResult setSOSDevicephone(
            @RequestBody @Validated DevicePhoneRequest devicePhoneRequest) {
        AjaxResult ajax = AjaxResult.success();
        if (devicePhoneRequest.getList() == null){
            return error("电话列表不能为空！");
        }
        if (devicePhoneRequest.getMapSet() == null){
            devicePhoneRequest.setMapSet(new HashMap<>());
        }
        //查找设备信息
        TDevice device = tDeviceService.selectDeviceByDeviceId(Long.valueOf(devicePhoneRequest.getDeviceId()));
        if (device == null){
            return error("设备信息不存在！");
        }
        if(!device.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            return  error("非创建者无权限操作！");
        }
        if(!device.getType().equals("1")){
            return error("该设备不是监控设备！");
        }
        TDevice.WatchSettings parameter = (TDevice.WatchSettings)device.getParameter();
        if(parameter==null){
            parameter = new TDevice.WatchSettings();
        }
        parameter.setList(devicePhoneRequest.getList());
        parameter.setMapSet(devicePhoneRequest.getMapSet());
        device.setParameter(parameter);
        try {
            tDeviceService.updateDevice(device);
        } catch (Exception e){
            error("设置电话失败！");
        }
        return ajax;
    }

    /**获取牵挂电子卡的电话列表*/
    @PostMapping("/getDevicePhone")
    public AjaxResult getDevicePhone(
            @RequestBody @Validated DevicePhoneRequest devicePhoneRequest) {
        AjaxResult ajax = AjaxResult.success();
        //查找设备信息
        TDevice device = tDeviceService.selectDeviceByDeviceId(Long.valueOf(devicePhoneRequest.getDeviceId()));
        if (device == null){
            throw new ServiceException("设备信息不存在!");
        }
        if(!device.getType().equals("1")){
            throw new ServiceException("该设备不是监控设备!");
        }
        if(!device.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            throw new ServiceException("无权限获取本设备通讯录!");
        }
        List<DevicePhone> list = new ArrayList<DevicePhone>();
        if(device.getParameter()==null){
            return AjaxResult.success(null);
        }
        return AjaxResult.success(device.getParameter());
    }
}
