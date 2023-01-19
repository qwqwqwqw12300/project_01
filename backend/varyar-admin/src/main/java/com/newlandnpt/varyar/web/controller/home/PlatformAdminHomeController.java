package com.newlandnpt.varyar.web.controller.home;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.service.IDeviceService;
import com.newlandnpt.varyar.system.service.IMemberService;
import com.newlandnpt.varyar.system.service.IOrgService;
import com.newlandnpt.varyar.system.service.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台管理员-首页
 *
 * @author lin.ju
 * @date 2023/1/4
 */
@RestController
@RequestMapping("home/platform/admin")
public class PlatformAdminHomeController extends BaseController {

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IMemberService memberService;
    @Autowired
    private IOrgService orgService;
    @Autowired
    private IEventService eventService;


    /**
     * 设备数量
     *
     * @return
     */
    @GetMapping("/device/count")
    public AjaxResult deviceCount() {
        //统计机构下已激活设备数量
        return success(deviceService.total(new TDevice()));
    }

    /**
     * 未分配（配对）设备数量
     *
     * @return
     */
    @GetMapping("/device/notassociate/count")
    public AjaxResult notAssociateDeviceCount() {
        return success(deviceService.notAssociateDeviceCount(new TDevice()));
    }


    /**
     * 会员总数
     *
     * @return
     */
    @GetMapping("/member/count")
    public AjaxResult memberCount() {
        //会员数
        return success(memberService.total(new TMember()));
    }

    /**
     * 未分配会员总数
     *
     * @return
     */
    @GetMapping("/member/notarrange/count")
    public AjaxResult notArrangeMemberCount() {
        //会员数
        return success(memberService.notArrangeMemberCount(new TMember()));
    }

    /**
     * 机构总数
     *
     * @return
     */
    @GetMapping("org/count")
    public AjaxResult orgCount() {
        return success(orgService.total());
    }

    /**
     * 未处理消息（事件）数
     *
     * @return
     */
    @GetMapping("event/unhandle/count")
    public AjaxResult unHandleEventCount() {
        return success(eventService.unHandleEventCount());
    }

    /**
     * 未处理会员消息（事件）数
     *
     * @return
     */
    @GetMapping("/event/member/unhandle/count")
    public AjaxResult unHandleMemberEventCount() {
        return success(eventService.unHandleMemberEventCount());
    }

}
