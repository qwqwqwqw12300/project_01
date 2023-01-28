package com.newlandnpt.varyar.web.controller.home;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.IDeviceService;
import com.newlandnpt.varyar.system.service.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    private IEventService eventService;


    /**
     * 运营关心的设备列表
     *
     * @return
     */
    @GetMapping("/device/care/list")
    public AjaxResult careDeviceList() {
        List<TDevice> list = deviceService.selectBizCareDeviceList(getUserId());
        //return success(deviceService.selectBizCareDeviceList(getUserId()));
        List<TDevice> newList =deviceService.loadingDeviceStauts(list); ;
        return success(newList);
    }

    /**
     * 查询设备的事件数 根据紧急级别分组
     *
     * @return
     */
    @GetMapping("/device/{deviceId}/event/unhandle/count/groupbylevel")
    public AjaxResult countUnHandleByDeviceGroupByLevel(@PathVariable Long deviceId) {
        return success(eventService.countUnHandleByDeviceGroupByLevel(deviceId));
    }


}
