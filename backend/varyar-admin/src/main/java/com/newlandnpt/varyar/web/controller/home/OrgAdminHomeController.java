package com.newlandnpt.varyar.web.controller.home;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.entity.TOrg;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.domain.TDeviceGroup;
import com.newlandnpt.varyar.system.service.IDeviceGroupService;
import com.newlandnpt.varyar.system.service.IDeviceService;
import com.newlandnpt.varyar.system.service.IOrgService;
import com.newlandnpt.varyar.system.service.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机构管理员首页
 * @author lin.ju
 * @date 2023/1/5
 */
@RestController
@RequestMapping("home/org/admin")
public class OrgAdminHomeController extends BaseController {

    @Autowired
    private IOrgService orgService;
    @Autowired
    private IDeviceGroupService deviceGroupService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IEventService eventService;

    /**
     * 获取分支机构列表(包含本身)
     */
    @GetMapping("/suborg/list")
    public AjaxResult list()
    {
        TOrg param = new TOrg();
//        Map<String,Object> params = new HashMap<>();
//        params.put("idNotEQ",getOrgId());
//        param.setParams(params);
        List<TOrg> orgs = orgService.selectOrgList(param);
        return success(orgs);
    }



    /**
     * 统计分支机构设备数
     */
    @GetMapping("/suborg/device/count/groupbyorg")
    public AjaxResult subOrgDeviceCount(){
        return success(deviceService.countGroupByOrgId(new TDevice()));
    }

    /**
     * 统计机构未处理消息（事件）数
     */
    @GetMapping("/org/{orgId}/event/unhandle/count")
    public AjaxResult orgUnHandleEventCount(@PathVariable("orgId") Long orgId){
        return success(eventService.unHandleEventCountInOrg(orgId));
    }

    /**
     * 未分配（配对）设备数量
     * @return
     */
    @GetMapping("device/notassociate/count")
    public AjaxResult notAssociateDeviceCount(){
        return success(deviceService.notAssociateDeviceCount(new TDevice()));
    }

    /**
     * 未分组设备数量
     * @return
     */
    @GetMapping("device/notarrange/count")
    public AjaxResult notArrangeDeviceCount(){
        return success(deviceService.notArrangeDeviceCount(new TDevice()));
    }



    /**
     * 未分配设备组数量
     * @return
     */
    @GetMapping("devicegroup/notarrange/count")
    public AjaxResult notArrangeDeviceGroupCount(){
        TDeviceGroup deviceGroup = new TDeviceGroup();
        deviceGroup.setOrgId(getOrgId());
        return success(deviceGroupService.notArrangeCount(deviceGroup));
    }




}
