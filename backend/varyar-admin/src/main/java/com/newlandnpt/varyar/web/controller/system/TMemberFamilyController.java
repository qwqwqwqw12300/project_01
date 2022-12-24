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
import com.newlandnpt.varyar.system.domain.TMemberFamily;
import com.newlandnpt.varyar.system.service.ITMemberFamilyService;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;

/**
 * 会员和家庭关联Controller
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@RestController
@RequestMapping("/system/memberFamily")
public class TMemberFamilyController extends BaseController
{
    @Autowired
    private ITMemberFamilyService tMemberFamilyService;

    /**
     * 查询会员和家庭关联列表
     */
    @PreAuthorize("@ss.hasPermi('system:memberFamily:list')")
    @GetMapping("/list")
    public TableDataInfo list(TMemberFamily tMemberFamily)
    {
        startPage();
        List<TMemberFamily> list = tMemberFamilyService.selectTMemberFamilyList(tMemberFamily);
        return getDataTable(list);
    }

    /**
     * 导出会员和家庭关联列表
     */
    @PreAuthorize("@ss.hasPermi('system:memberFamily:export')")
    @Log(title = "会员和家庭关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMemberFamily tMemberFamily)
    {
        List<TMemberFamily> list = tMemberFamilyService.selectTMemberFamilyList(tMemberFamily);
        ExcelUtil<TMemberFamily> util = new ExcelUtil<TMemberFamily>(TMemberFamily.class);
        util.exportExcel(response, list, "会员和家庭关联数据");
    }

    /**
     * 获取会员和家庭关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:memberFamily:query')")
    @GetMapping(value = "/{memberFamilyId}")
    public AjaxResult getInfo(@PathVariable("memberFamilyId") Long memberFamilyId)
    {
        return success(tMemberFamilyService.selectTMemberFamilyByMemberFamilyId(memberFamilyId));
    }

    /**
     * 新增会员和家庭关联
     */
    @PreAuthorize("@ss.hasPermi('system:memberFamily:add')")
    @Log(title = "会员和家庭关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMemberFamily tMemberFamily)
    {
        return toAjax(tMemberFamilyService.insertTMemberFamily(tMemberFamily));
    }

    /**
     * 修改会员和家庭关联
     */
    @PreAuthorize("@ss.hasPermi('system:memberFamily:edit')")
    @Log(title = "会员和家庭关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMemberFamily tMemberFamily)
    {
        return toAjax(tMemberFamilyService.updateTMemberFamily(tMemberFamily));
    }

    /**
     * 删除会员和家庭关联
     */
    @PreAuthorize("@ss.hasPermi('system:memberFamily:remove')")
    @Log(title = "会员和家庭关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{memberFamilyIds}")
    public AjaxResult remove(@PathVariable Long[] memberFamilyIds)
    {
        return toAjax(tMemberFamilyService.deleteTMemberFamilyByMemberFamilyIds(memberFamilyIds));
    }
}
