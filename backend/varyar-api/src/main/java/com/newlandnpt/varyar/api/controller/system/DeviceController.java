package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.DeviceRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.system.domain.Device;
import com.newlandnpt.varyar.system.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备管理
 *
 * @author newlandnpt
 */
@RestController
@RequestMapping("/api/device")
public class DeviceController extends BaseController {
    @Autowired
    private IDeviceService iDeviceService;
    /**
     * 获取设备列表
     * */
    @PostMapping("/list")
    public TableDataInfo list(
            @RequestBody @Validated DeviceRequest deviceRequest) {
        startPage();
        Long memberId = getLoginUser().getMemberId();
        Map<String ,Long> map =new HashMap<String ,Long>();
        map.put("memberId",memberId);
        map.put("familyId",Long.valueOf(deviceRequest.getFamilyId()));
        map.put("roomId",Long.valueOf(deviceRequest.getRoomId()));
        List<Device> list = iDeviceService.selectDeviceByMemberId(map);
        return getDataTable(list);
    }
    /**
     * 获取设备列表
     * */
    @PostMapping("/listState")
    public TableDataInfo listState(
            @RequestBody @Validated DeviceRequest deviceRequest) {
        startPage();
        Long memberId = getLoginUser().getMemberId();
        Map<String ,Long> map =new HashMap<String ,Long>();
        map.put("memberId",memberId);
        map.put("familyId",Long.valueOf(deviceRequest.getFamilyId()));
        map.put("roomId",Long.valueOf(deviceRequest.getRoomId()));
        List<Device> list = iDeviceService.selectDeviceByMemberId(map);
        return getDataTable(list);
    }
    /**
     * 创建设备
     * */
    @PostMapping("/creDevice")
    public AjaxResult createDevice(
            @RequestBody @Validated DeviceRequest deviceRequest){
        AjaxResult ajax = AjaxResult.success();

        return ajax;
    }

}
