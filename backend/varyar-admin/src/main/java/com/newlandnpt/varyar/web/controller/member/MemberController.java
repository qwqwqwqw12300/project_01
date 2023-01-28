package com.newlandnpt.varyar.web.controller.member;

import java.util.List;

import com.newlandnpt.varyar.common.annotation.Log;
import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.enums.BusinessType;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.domain.TFamily;
import com.newlandnpt.varyar.system.service.IDeviceService;
import com.newlandnpt.varyar.system.service.IFamilyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.service.IMemberService;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;

/**
 * 会员Controller
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@RestController
@RequestMapping("/member")
public class MemberController extends BaseController
{
    @Autowired
    private IMemberService memberService;
    @Autowired
    private IFamilyService tFamilyService;

    @Autowired
    private IDeviceService deviceService;

    /**
     * 查询会员列表
     */
    @GetMapping("/page")
    public TableDataInfo list(TMember member)
    {
        startPage();
        List<TMember> list = memberService.selectMemberList(member);
        return getDataTable(list);
    }

    /**
     * 获取会员详细信息
     */
    @GetMapping(value = "/{memberId}")
    public AjaxResult getInfo(@PathVariable("memberId") Long memberId)
    {
        return success(memberService.selectMemberByMemberId(memberId));
    }
    /**
     * 获取会员家庭分页
     */
    @GetMapping(value = "/{memberId}/family/page")
    public TableDataInfo familyList(@PathVariable("memberId") Long memberId)
    {
        startPage();
        List<TFamily> list = tFamilyService.selectMembersFamilyList(memberId);
        //List<TFamily> newlist = deviceService.loadingDeviceStauts(list);
        for (TFamily family : list)
        {
            family.setDevices(deviceService.loadingDeviceStauts(family.getDevices()));
        }

        return getDataTable(list);
    }

    /**
     * 给会员分配运营
     */
    @Log(title = "会员管理-分配运营", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('member:arrange')")
    @PutMapping(value = "/{memberId}/arrange/user/{userId}")
    public AjaxResult families(@PathVariable("memberId") Long memberId,
                               @PathVariable("userId") Long userId)
    {

        return success(memberService.arrangeUserToMember(memberId,userId));
    }
    /**
     * 给会员分配运营
     */
    @Log(title = "会员管理-分配运营", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('member:arrange')")
    @PutMapping(value = {"/arrange/user/{userId}","/arrange/user"})
    public AjaxResult families(@RequestBody Long[] memberIds,
                               @PathVariable(value = "userId",required = false) Long userId)
    {

        return success(memberService.arrangeMembers(memberIds,userId));
    }

}
