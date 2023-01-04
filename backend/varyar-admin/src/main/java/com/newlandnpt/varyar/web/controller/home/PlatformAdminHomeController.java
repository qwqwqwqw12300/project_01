package com.newlandnpt.varyar.web.controller.home;

import com.newlandnpt.varyar.system.service.IDeviceService;
import com.newlandnpt.varyar.system.service.IMemberService;
import com.newlandnpt.varyar.system.service.IOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台管理员-首页
 * @author lin.ju
 * @date 2023/1/4
 */
@RestController
@RequestMapping("platformAdminHome")
public class PlatformAdminHomeController {

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IMemberService memberService;
    @Autowired
    private IOrgService orgService;

    @PreAuthorize("@ss.hasPermi('home:platformAdmin:list')")
    @GetMapping("device/count")
    private Long deviceCount(){
        //统计机构下已激活设备数量
        return deviceService.total();
    }

    @PreAuthorize("@ss.hasPermi('home:platformAdmin:list')")
    @GetMapping("member/count")
    private Long memberCount(){
        //统计机构下服务会员数
        return memberService.total();
    }

    @PreAuthorize("@ss.hasPermi('home:platformAdmin:list')")
    @GetMapping("org/count")
    private Long orgCount(){
        return orgService.total();
    }

}
