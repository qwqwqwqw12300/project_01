package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.DeviceFenceRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.domain.TDeviceFence;
import com.newlandnpt.varyar.system.service.IDeviceFenceService;
import com.newlandnpt.varyar.system.service.IDeviceService;
import com.newlandnpt.varyar.system.service.impl.CommonDeviceFenceServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * 设备电子围栏Controller
 *
 * @author chenxw
 * @date 2023-01-06
 */
@Api(tags = "设备电子围栏")
@RestController
@RequestMapping("/api/fence")
public class DeviceFenceController extends BaseController
{
 //   @Autowired
//    private IDeviceFenceService deviceFenceService;
    @Autowired
    private CommonDeviceFenceServiceImpl deviceFenceService;

    @Autowired
    private IDeviceService iDeviceService;

    @ApiOperation("查询设备电子围栏列表")
    @GetMapping("/list")
    public TableDataInfo<List<TDeviceFence>> list(TDeviceFence tDeviceFence)
    {
        startPage();
        List<TDeviceFence> list = deviceFenceService.selectTDeviceFenceList(tDeviceFence);
        return getDataTable(list);
    }

    @ApiOperation("获取设备电子围栏详细信息")
    @GetMapping(value = "/getDeFeInfo")
    public AjaxResult getInfo(@RequestBody @Validated DeviceFenceRequest deviceFenceRequest)
    {
        List<TDeviceFence> tDeviceFences =
                deviceFenceService.selectTDeviceFenceByDeviceId(Long.valueOf(deviceFenceRequest.getDeviceId()));
        if (tDeviceFences==null){
            tDeviceFences =new ArrayList<TDeviceFence>();
        }
        return success(tDeviceFences);
    }

    @ApiOperation("新增设备电子围栏")
    @PostMapping("/addFence")
    public AjaxResult add( @RequestBody @Validated TDeviceFence tDeviceFence)
    {
        AjaxResult ajax = AjaxResult.success();
        if(tDeviceFence.getDeviceId()==null||tDeviceFence.getDeviceId().equals("")){
            ajax = ajax.error("设备Id不能为空！");
            return ajax;
        }
        if(tDeviceFence.getDeviceNo()==null||tDeviceFence.getDeviceNo().equals("")){
            ajax = ajax.error("设备编号不能为空！");
            return ajax;
        }
        if(tDeviceFence.getAddress()==null||tDeviceFence.getAddress().equals("")){
            ajax = ajax.error("地址信息不能为空！");
            return ajax;
        }
        if(tDeviceFence.getLatitude()==null||tDeviceFence.getLatitude().equals("")){
            ajax = ajax.error("纬度信息不能为空！");
            return ajax;
        }
        if(tDeviceFence.getLongitude()==null||tDeviceFence.getLongitude().equals("")){
            ajax = ajax.error("经度信息不能为空！");
            return ajax;
        }
        if(tDeviceFence.getRadius()==null||tDeviceFence.getRadius().equals("")){
            ajax = ajax.error("半径不能为空！");
            return ajax;
        }
        //获取设备id
        TDevice tdevice =  iDeviceService.selectDeviceByDeviceId(Long.valueOf(tDeviceFence.getDeviceId()));
        if(tdevice==null){
            throw new ServiceException("无法找到设备信息！");
        }
        if (!tdevice.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            throw new ServiceException("无权限设置本设备！");
        }
        TDeviceFence cond = new TDeviceFence();
        cond.setDeviceId(Long.valueOf(tDeviceFence.getDeviceId()));
        cond.setFenceType(tDeviceFence.getFenceType());
        List<TDeviceFence> TDeviceFences = deviceFenceService.selectTDeviceFenceList(cond);
        if (TDeviceFences.size()==0){
            int di = deviceFenceService.insertTDeviceFence(tDeviceFence);
            if(di == 0){
                return error("设置电子围栏失败！");
            }
        }else if (TDeviceFences.size()>0){
            return edit(tDeviceFence);
        }

        return ajax;
    }

    @ApiOperation("修改设备电子围栏")
    @PostMapping("/editFence")
    public AjaxResult edit( @RequestBody @Validated TDeviceFence tDeviceFence){
        AjaxResult ajax = AjaxResult.success();
        if(tDeviceFence.getDeviceId()==null||tDeviceFence.getDeviceId().equals("")){
            ajax = ajax.error("设备Id不能为空！");
            return ajax;
        }
        //获取设备id
        TDevice tdevice =  iDeviceService.selectDeviceByDeviceId(Long.valueOf(tDeviceFence.getDeviceId()));
        if (!tdevice.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            throw new ServiceException("无权限修改本设备！");
        }
        List<TDeviceFence> tDeviceFences = deviceFenceService.selectTDeviceFenceByDeviceId(Long.valueOf(tDeviceFence.getDeviceId()));
        if (tDeviceFences.size()==0){
            return error("修改电子围栏信息失败！");
        }else if (tDeviceFences.size()>0){
            tDeviceFence.setGeoFenceId(tDeviceFences.get(0).getGeoFenceId());
            tDeviceFence.setDeviceNo(tDeviceFences.get(0).getDeviceNo());
            tDeviceFence.setFenceType(tDeviceFences.get(0).getFenceType());
            tDeviceFence.setDeviceFenceId(tDeviceFences.get(0).getDeviceFenceId());
        }
        try {
            int di = deviceFenceService.updateTDeviceFence(tDeviceFence);
            if(di == 0){
                return error("修改电子围栏失败！");
            }
        }catch (Exception e){
            return error("设备修改失败！");
        }
        return ajax ;
    }

    @ApiOperation("删除设备电子围栏")
    @PostMapping("/rem")
    public AjaxResult remove( @RequestBody @Validated TDeviceFence tDeviceFence){
        AjaxResult ajax = AjaxResult.success();
        if(tDeviceFence.getDeviceId()==null||tDeviceFence.getDeviceId().equals("")){
            ajax = ajax.error("设备Id不能为空！");
            return ajax;
        }
        //获取设备id
        TDevice tdevice =  iDeviceService.selectDeviceByDeviceId(Long.valueOf(tDeviceFence.getDeviceId()));
        if (!tdevice.getMemberId().toString().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            throw new ServiceException("无权限删除本设备！");
        }
        List<TDeviceFence> TDeviceFences = deviceFenceService.selectTDeviceFenceByDeviceId(Long.valueOf(tDeviceFence.getDeviceId()));
        if (TDeviceFences.size() == 0) {
            throw new ServiceException("无设备电子围栏数据！");
        }
        for (TDeviceFence item : TDeviceFences){
            try {
                deviceFenceService.deleteTDeviceFence(item);
            }catch (Exception e){
                ajax = ajax.error("设备删除失败！");
                return ajax;
            }
        }
        return ajax ;
    }


}
