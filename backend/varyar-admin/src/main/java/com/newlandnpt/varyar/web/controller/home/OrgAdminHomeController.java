package com.newlandnpt.varyar.web.controller.home;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.entity.Org;
import com.newlandnpt.varyar.system.domain.Device;
import com.newlandnpt.varyar.system.domain.DeviceGroup;
import com.newlandnpt.varyar.system.service.IDeviceGroupService;
import com.newlandnpt.varyar.system.service.IDeviceService;
import com.newlandnpt.varyar.system.service.IOrgService;
import com.newlandnpt.varyar.system.service.ITEventService;
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
    private ITEventService eventService;

    /**
     * 获取分支机构列表
     */
    @PreAuthorize("@ss.hasPermi('home:orgAdmin:list')")
    @GetMapping("/suborg/list")
    public AjaxResult list()
    {
        Org param = new Org();
        Map<String,Object> params = new HashMap<>();
        params.put("idNotEQ",getOrgId());
        param.setParams(params);
        List<Org> orgs = orgService.selectOrgList(param);
        return success(orgs);
    }

    /**
     * 统计分支机构设备数
     */
    @PreAuthorize("@ss.hasPermi('home:orgAdmin:list')")
    @GetMapping("/suborg/device/count/groupbyorg")
    public AjaxResult subOrgDeviceCount(){
        return success(deviceService.countGroupByOrgId(new Device()));
    }

    /**
     * 统计机构未处理消息（事件）数
     */
    @PreAuthorize("@ss.hasPermi('home:orgAdmin:list')")
    @GetMapping("/org/{orgId}/event/unhandle/count")
    public AjaxResult orgUnHandleEventCount(@PathVariable("orgId") Long orgId){
        return success(eventService.unHandleEventCountInOrg(orgId));
    }

    /**
     * 未分配（配对）设备数量
     * @return
     */
    @PreAuthorize("@ss.hasPermi('home:platformAdmin:list')")
    @GetMapping("device/notassociate/count")
    public AjaxResult notAssociateDeviceCount(){
        return success(deviceService.notAssociateDeviceCount(new Device()));
    }

    /**
     * 未分组设备数量
     * @return
     */
    @PreAuthorize("@ss.hasPermi('home:orgAdmin:list')")
    @GetMapping("device/notarrange/count")
    public AjaxResult notArrangeDeviceCount(){
        return success(deviceService.notArrangeDeviceCount(new Device()));
    }



    /**
     * 未分配设备组数量
     * @return
     */
    @PreAuthorize("@ss.hasPermi('home:orgAdmin:list')")
    @GetMapping("devicegroup/notarrange/count")
    public AjaxResult notArrangeDeviceGroupCount(){
        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setOrgId(getOrgId());
        return success(deviceGroupService.notArrangeCount(deviceGroup));
    }




}
