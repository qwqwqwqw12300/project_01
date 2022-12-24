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
import com.newlandnpt.varyar.system.domain.TMembercontacts;
import com.newlandnpt.varyar.system.service.ITMembercontactsService;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;

/**
 * 会员联络人Controller
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@RestController
@RequestMapping("/system/membercontacts")
public class TMembercontactsController extends BaseController
{
    @Autowired
    private ITMembercontactsService tMembercontactsService;

    /**
     * 查询会员联络人列表
     */
    @PreAuthorize("@ss.hasPermi('system:membercontacts:list')")
    @GetMapping("/list")
    public TableDataInfo list(TMembercontacts tMembercontacts)
    {
        startPage();
        List<TMembercontacts> list = tMembercontactsService.selectTMembercontactsList(tMembercontacts);
        return getDataTable(list);
    }

    /**
     * 导出会员联络人列表
     */
    @PreAuthorize("@ss.hasPermi('system:membercontacts:export')")
    @Log(title = "会员联络人", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMembercontacts tMembercontacts)
    {
        List<TMembercontacts> list = tMembercontactsService.selectTMembercontactsList(tMembercontacts);
        ExcelUtil<TMembercontacts> util = new ExcelUtil<TMembercontacts>(TMembercontacts.class);
        util.exportExcel(response, list, "会员联络人数据");
    }

    /**
     * 获取会员联络人详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:membercontacts:query')")
    @GetMapping(value = "/{membercontactsId}")
    public AjaxResult getInfo(@PathVariable("membercontactsId") Long membercontactsId)
    {
        return success(tMembercontactsService.selectTMembercontactsByMembercontactsId(membercontactsId));
    }

    /**
     * 新增会员联络人
     */
    @PreAuthorize("@ss.hasPermi('system:membercontacts:add')")
    @Log(title = "会员联络人", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMembercontacts tMembercontacts)
    {
        return toAjax(tMembercontactsService.insertTMembercontacts(tMembercontacts));
    }

    /**
     * 修改会员联络人
     */
    @PreAuthorize("@ss.hasPermi('system:membercontacts:edit')")
    @Log(title = "会员联络人", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMembercontacts tMembercontacts)
    {
        return toAjax(tMembercontactsService.updateTMembercontacts(tMembercontacts));
    }

    /**
     * 删除会员联络人
     */
    @PreAuthorize("@ss.hasPermi('system:membercontacts:remove')")
    @Log(title = "会员联络人", businessType = BusinessType.DELETE)
	@DeleteMapping("/{membercontactsIds}")
    public AjaxResult remove(@PathVariable Long[] membercontactsIds)
    {
        return toAjax(tMembercontactsService.deleteTMembercontactsByMembercontactsIds(membercontactsIds));
    }
}
