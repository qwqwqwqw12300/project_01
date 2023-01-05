package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.system.domain.Device;
import com.newlandnpt.varyar.system.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @GetMapping("/list")
    public TableDataInfo list(Device device) {
        startPage();
        Long memberId = getLoginUser().getMemberId();
        List<Device> list = iDeviceService.selectDeviceByMemberId(memberId);
        return getDataTable(list);
    }

}
