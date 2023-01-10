package com.newlandnpt.varyar.web.controller.eventAndMessage;

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
import com.newlandnpt.varyar.system.domain.TMsg;
import com.newlandnpt.varyar.system.service.IMsgService;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;

/**
 * 消息Controller
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@RestController
@RequestMapping("/msg")
public class MsgController extends BaseController
{
    @Autowired
    private IMsgService msgService;

    /**
     * 查询消息列表
     */
    @PreAuthorize("@ss.hasPermi('system:msg:list')")
    @GetMapping("/list")
    public TableDataInfo list(
            TMsg tMsg)
    {
        startPage();
        List<TMsg> list = msgService.selectTMsgList(tMsg);
        setType(list);
        return getDataTable(list);
    }
    private void setType(List<TMsg> list){
        for(TMsg item : list){
            item.setOperateFlag(item.getOperateFlag().equals("1")?"审核":"未审核");
        }
    }

    /**
     * 导出消息列表
     */
    @PreAuthorize("@ss.hasPermi('system:msg:export')")
    @Log(title = "消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMsg tMsg)
    {
        List<TMsg> list = msgService.selectTMsgList(tMsg);
        ExcelUtil<TMsg> util = new ExcelUtil<TMsg>(TMsg.class);
        util.exportExcel(response, list, "消息数据");
    }

    /**
     * 获取消息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:msg:query')")
    @GetMapping(value = "/{msgId}")
    public AjaxResult getInfo(@PathVariable("msgId") Long msgId)
    {
        return success(msgService.selectTMsgByMsgId(msgId));
    }

    /**
     * 新增消息
     */
    @PreAuthorize("@ss.hasPermi('system:msg:add')")
    @Log(title = "消息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMsg tMsg)
    {
        return toAjax(msgService.insertTMsg(tMsg));
    }

    /**
     * 修改消息
     */
    @PreAuthorize("@ss.hasPermi('system:msg:edit')")
    @Log(title = "消息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMsg tMsg)
    {
        tMsg.setOperateFlag("1");
        return toAjax(msgService.updateTMsg(tMsg));
    }

    /**
     * 删除消息
     */
    @PreAuthorize("@ss.hasPermi('system:msg:remove')")
    @Log(title = "消息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{msgIds}")
    public AjaxResult remove(@PathVariable Long[] msgIds)
    {
        return toAjax(msgService.deleteTMsgByMsgIds(msgIds));
    }
}
