package com.newlandnpt.varyar.web.controller.member;

import java.util.List;

import com.newlandnpt.varyar.system.service.ITFamilyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.system.domain.Member;
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
    private ITFamilyService tFamilyService;

    /**
     * 查询会员列表
     */
    @PreAuthorize("@ss.hasPermi('system:member:list')")
    @GetMapping("/page")
    public TableDataInfo list(Member member)
    {
        startPage();
        List<Member> list = memberService.selectMemberList(member);
        return getDataTable(list);
    }

    /**
     * 获取会员详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:member:query')")
    @GetMapping(value = "/{memberId}")
    public AjaxResult getInfo(@PathVariable("memberId") Long memberId)
    {
        return success(memberService.selectMemberByMemberId(memberId));
    }

    /**
     * 给会员分配运营
     */
    @PreAuthorize("@ss.hasPermi('system:member:arrange')")
    @GetMapping(value = "/{memberId}/arrange/user/{userId}")
    public AjaxResult families(@PathVariable("memberId") Long memberId,
                               @PathVariable("userId") Long userId)
    {

        return success(memberService.arrangeUserToMember(memberId,userId));
    }


}
