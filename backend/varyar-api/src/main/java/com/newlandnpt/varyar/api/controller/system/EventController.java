package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.model.EventRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.system.domain.TEvent;
import com.newlandnpt.varyar.system.service.ITEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件表Controller
 * 
 * @author chenxw
 * @date 2023-01-05
 */
@RestController
@RequestMapping("/api/event")
public class EventController extends BaseController
{
    @Autowired
    private ITEventService tEventService;

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
    @PostMapping("/selectEvent")
    public TableDataInfo selectEvent(@RequestBody @Validated EventRequest eventRequest)
    {
        startPage();
        //List<TEvent> list = tEventService.selectTEventList(eventRequest);
        System.out.println(eventRequest.toString());
        List<String> list =new ArrayList<>();
        list.add(0,"XX设备开机");
        list.add(1,"设备静止不动");

        list.add(2,"人员进入卫生间");
        list.add(3,"人员进入客厅走动");
        list.add(4,"人员离开卧室");



        return getDataTable(list);
    }
}
