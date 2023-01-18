package com.newlandnpt.varyar.web.controller.device;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.newlandnpt.varyar.common.annotation.Log;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.common.enums.BusinessType;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.newlandnpt.varyar.system.domain.TServeRecord;
import com.newlandnpt.varyar.system.service.IServeRecordService;

/**
 * 服务记录Controller
 * 
 * @author lin.ju
 * @date 2023-01-04
 */
@RestController
@RequestMapping("/device/serve/record")
public class ServeRecordController extends BaseController
{
    @Autowired
    private IServeRecordService serveRecordService;

    /**
     * 查询服务记录列表
     */
    @GetMapping("/page")
    public TableDataInfo list(TServeRecord serveRecord)
    {
        startPage();
        List<TServeRecord> list = serveRecordService.selectServeRecordList(serveRecord);
        return getDataTable(list);
    }

    /**
     * 导出服务记录列表
     */
    @PreAuthorize("@ss.hasPermi('device:serveRecord:export')")
    @Log(title = "服务记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TServeRecord serveRecord)
    {
        List<TServeRecord> list = serveRecordService.selectServeRecordList(serveRecord);
        ExcelUtil<TServeRecord> util = new ExcelUtil<TServeRecord>(TServeRecord.class);
        util.exportExcel(response, list, "服务记录数据");
    }

    /**
     * 获取服务记录详细信息
     */
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return success(serveRecordService.selectServeRecordByRecordId(recordId));
    }

    /**
     * 新增服务记录
     */
    @PreAuthorize("@ss.hasPermi('device:serveRecord:add')")
    @Log(title = "服务记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TServeRecord serveRecord)
    {
        // 服务人员为当前登录人员
        serveRecord.setServedUserId(getUserId());
        return toAjax(serveRecordService.insertServeRecord(serveRecord));
    }

}
