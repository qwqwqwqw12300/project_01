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
import com.newlandnpt.varyar.system.domain.TRoom;
import com.newlandnpt.varyar.system.service.IRoomService;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;

/**
 * 房间Controller
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@RestController
@RequestMapping("/system/room")
public class TRoomController extends BaseController
{
    @Autowired
    private IRoomService tRoomService;

    /**
     * 查询房间列表
     */
    @PreAuthorize("@ss.hasPermi('system:room:list')")
    @GetMapping("/list")
    public TableDataInfo list(TRoom tRoom)
    {
        startPage();
        List<TRoom> list = tRoomService.selectTRoomList(tRoom);
        return getDataTable(list);
    }

    /**
     * 导出房间列表
     */
    @PreAuthorize("@ss.hasPermi('system:room:export')")
    @Log(title = "房间", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TRoom tRoom)
    {
        List<TRoom> list = tRoomService.selectTRoomList(tRoom);
        ExcelUtil<TRoom> util = new ExcelUtil<TRoom>(TRoom.class);
        util.exportExcel(response, list, "房间数据");
    }

    /**
     * 获取房间详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:room:query')")
    @GetMapping(value = "/{roomId}")
    public AjaxResult getInfo(@PathVariable("roomId") Long roomId)
    {
        return success(tRoomService.selectTRoomByRoomId(roomId));
    }

    /**
     * 新增房间
     */
    @PreAuthorize("@ss.hasPermi('system:room:add')")
    @Log(title = "房间", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TRoom tRoom)
    {
        return toAjax(tRoomService.insertTRoom(tRoom));
    }

    /**
     * 修改房间
     */
    @PreAuthorize("@ss.hasPermi('system:room:edit')")
    @Log(title = "房间", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TRoom tRoom)
    {
        return toAjax(tRoomService.updateTRoom(tRoom));
    }

    /**
     * 删除房间
     */
    @PreAuthorize("@ss.hasPermi('system:room:remove')")
    @Log(title = "房间", businessType = BusinessType.DELETE)
	@DeleteMapping("/{roomIds}")
    public AjaxResult remove(@PathVariable Long[] roomIds)
    {
        return toAjax(tRoomService.deleteTRoomByRoomIds(roomIds));
    }
}
