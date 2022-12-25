package com.newlandnpt.varyar.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.newlandnpt.varyar.system.domain.TFamily;
import com.newlandnpt.varyar.system.service.ITFamilyService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.newlandnpt.varyar.common.annotation.Log;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.enums.BusinessType;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.service.ITMemberService;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;

/**
 * 会员Controller
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@RestController
@RequestMapping("/system/member")
public class TMemberController extends BaseController
{
    @Autowired
    private ITMemberService tMemberService;
    @Autowired
    private ITFamilyService tFamilyService;

    /**
     * 查询会员列表
     */
    @PreAuthorize("@ss.hasPermi('system:member:list')")
    @GetMapping("/list")
    public TableDataInfo list(TMember tMember)
    {
        startPage();
        List<TMember> list = tMemberService.selectTMemberList(tMember);
        return getDataTable(list);
    }

    /**
     * 获取会员详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:member:query')")
    @GetMapping(value = "/{memberId}")
    public AjaxResult getInfo(@PathVariable("memberId") Long memberId)
    {
        return success(tMemberService.selectTMemberByMemberId(memberId));
    }


    /**
     * 获取会员家庭信息
     */
    @PreAuthorize("@ss.hasPermi('system:member:query')")
    @GetMapping(value = "/{memberId}/families")
    public AjaxResult families(@PathVariable("memberId") Long memberId)
    {

        return success(tFamilyService.selectMembersFamilyList(memberId));
    }
    /**
     * 给会员分配运营
     */
    @PreAuthorize("@ss.hasPermi('system:member:arrange')")
    @GetMapping(value = "/{memberId}/arrange/user/{userId}")
    public AjaxResult families(@PathVariable("memberId") Long memberId,
                               @PathVariable("userId") Long userId)
    {

        return success(tMemberService.arrangeUserToMember(memberId,userId));
    }


}
