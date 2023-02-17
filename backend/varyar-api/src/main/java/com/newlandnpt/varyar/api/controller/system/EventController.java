package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.EventRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.system.domain.TEvent;
import com.newlandnpt.varyar.system.domain.TEventList;
import com.newlandnpt.varyar.system.service.IEventService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 事件表Controller
 * 
 * @author chenxw
 * @date 2023-01-05
 */
//@Api(tags = "事件") 事件相关没用到
@RestController
@RequestMapping("/api/event")
public class EventController extends BaseController
{
    @Autowired
    private IEventService tEventService;

    /**
     * 查询事件列表
     */

    @GetMapping("/list")
    public TableDataInfo list(TEvent tEvent)
    {
        startPage();
        List<TEvent> list = tEventService.selectTEventList(tEvent);
        return getDataTable(list);
    }
    /**
     * 查询事件列表
     */
    @PostMapping("/selectEventInfo")
    public TableDataInfo selectEventInfo(@RequestBody @Validated EventRequest eventRequest)
    {
        //获取当前的登录会员信息
        Long memberId = getLoginUser().getMemberId();
        String memberPhone = getLoginUser().getMemberPhone();
        String memberUsername = getLoginUser().getUsername();
        eventRequest.setMemberId(memberId);
        eventRequest.setMemberPhone(memberPhone);
        eventRequest.setMemberName(memberUsername);

        startPage(10000);
        List<TEventList> list = tEventService.selectEventInfo(eventRequest);
        return getDataTable(list);
    }

    /**
     * 查询消息
     */
    @PostMapping("/selectEventCountByFlag")
    public AjaxResult selectEventCountByFlag(@RequestParam("operateFlag") String operateFlag)
    {
        AjaxResult ajax = AjaxResult.success();
        int count = tEventService.selectEventCountByFlag(operateFlag);
        ajax.put("count",count);
        return ajax;
    }

    /**
     * 查询消息总数
     */
    @PostMapping("/selectEventCount")
    public AjaxResult selectEventCount(@RequestParam("operateFlag") String operateFlag)
    {
        AjaxResult ajax = AjaxResult.success();

        int count = tEventService.selectEventCount(operateFlag);
        ajax.put("count",count);

        return ajax;
    }

}
