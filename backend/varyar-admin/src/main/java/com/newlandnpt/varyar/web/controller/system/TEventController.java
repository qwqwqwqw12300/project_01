package com.newlandnpt.varyar.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.newlandnpt.varyar.system.domain.TEvent;
import com.newlandnpt.varyar.system.service.ITEventService;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;

/**
 * 事件Controller
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@RestController
@RequestMapping("/system/event")
public class TEventController extends BaseController
{
    @Autowired
    private ITEventService tEventService;

    /**
     * 查询事件列表
     */
    @PreAuthorize("@ss.hasPermi('system:event:list')")
    @GetMapping("/list")
    public TableDataInfo list(TEvent tEvent)
    {
        startPage();
        List<TEvent> list = tEventService.selectTEventList(tEvent);
        return getDataTable(list);
    }

    /**
     * 导出事件列表
     */
    @PreAuthorize("@ss.hasPermi('system:event:export')")
    @Log(title = "事件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TEvent tEvent)
    {
        List<TEvent> list = tEventService.selectTEventList(tEvent);
        ExcelUtil<TEvent> util = new ExcelUtil<TEvent>(TEvent.class);
        util.exportExcel(response, list, "事件数据");
    }

    /**
     * 获取事件详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:event:query')")
    @GetMapping(value = "/{eventId}")
    public AjaxResult getInfo(@PathVariable("eventId") Long eventId)
    {
        return success(tEventService.selectTEventByEventId(eventId));
    }

    /**
     * 新增事件
     */
    @PreAuthorize("@ss.hasPermi('system:event:add')")
    @Log(title = "事件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TEvent tEvent)
    {
        return toAjax(tEventService.insertTEvent(tEvent));
    }

    /**
     * 修改事件
     */
    @PreAuthorize("@ss.hasPermi('system:event:edit')")
    @Log(title = "事件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TEvent tEvent)
    {
        return toAjax(tEventService.updateTEvent(tEvent));
    }

    /**
     * 删除事件
     */
    @PreAuthorize("@ss.hasPermi('system:event:remove')")
    @Log(title = "事件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{eventIds}")
    public AjaxResult remove(@PathVariable Long[] eventIds)
    {
        return toAjax(tEventService.deleteTEventByEventIds(eventIds));
    }
}
