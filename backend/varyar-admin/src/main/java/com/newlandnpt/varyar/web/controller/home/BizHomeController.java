package com.newlandnpt.varyar.web.controller.home;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.system.service.IDeviceService;
import com.newlandnpt.varyar.system.service.ITEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 运营首页
 * @author lin.ju
 * @date 2023/1/5
 */
@RestController
@RequestMapping("home/biz")
public class BizHomeController extends BaseController {

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private ITEventService eventService;

    /**
     * 运营关心的设备列表
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('home:biz:list')")
    @GetMapping("/device/care/list")
    public AjaxResult careDeviceList() {
        return success(deviceService.selectBizCareDeviceList(getUserId()));
    }

    /**
     * 查询设备的事件数 根据紧急级别分组
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('home:biz:list')")
    @GetMapping("/device/{deviceId}/event/unhandle/count/groupbylevel")
    public AjaxResult countUnHandleByDeviceGroupByLevel(@PathVariable Long deviceId) {
        return success(eventService.countUnHandleByDeviceGroupByLevel(deviceId));
    }


}
