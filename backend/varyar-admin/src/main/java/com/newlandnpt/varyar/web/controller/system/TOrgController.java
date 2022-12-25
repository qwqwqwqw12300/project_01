package com.newlandnpt.varyar.web.controller.system;

import java.util.List;

import com.newlandnpt.varyar.common.core.domain.entity.SysUser;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.common.utils.poi.ExcelUtil;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.ITDeviceService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.newlandnpt.varyar.common.annotation.Log;
import com.newlandnpt.varyar.common.constant.UserConstants;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.entity.TOrg;
import com.newlandnpt.varyar.common.enums.BusinessType;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.system.service.ITOrgService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 机构信息
 * 
 * @author newlandnpt
 */
@RestController
@RequestMapping("/system/org")
public class TOrgController extends BaseController
{
    @Autowired
    private ITOrgService orgService;
    @Autowired
    private ITDeviceService deviceService;

    /**
     * 获取机构列表
     */
    @PreAuthorize("@ss.hasPermi('system:org:list')")
    @GetMapping("/list")
    public AjaxResult list(TOrg org)
    {
        List<TOrg> orgs = orgService.selectOrgList(org);
        return success(orgs);
    }
    /**
     * 获取机构分页
     */
    @PreAuthorize("@ss.hasPermi('system:org:list')")
    @GetMapping("/page")
    public TableDataInfo page(TOrg org)
    {
        startPage();
        List<TOrg> orgs = orgService.selectOrgList(org);
        return getDataTable(orgs);
    }

    /**
     * 查询机构列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('system:org:list')")
    @GetMapping("/list/exclude/{orgId}")
    public AjaxResult excludeChild(@PathVariable(value = "orgId", required = false) Long orgId)
    {
        List<TOrg> orgs = orgService.selectOrgList(new TOrg());
        orgs.removeIf(d -> d.getOrgId().intValue() == orgId || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), orgId + ""));
        return success(orgs);
    }

    /**
     * 根据机构编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:org:query')")
    @GetMapping(value = "/{orgId}")
    public AjaxResult getInfo(@PathVariable Long orgId)
    {
        orgService.checkOrgDataScope(orgId);
        return success(orgService.selectOrgById(orgId));
    }

    /**
     * 新增机构
     */
    @PreAuthorize("@ss.hasPermi('system:org:add')")
    @Log(title = "机构管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TOrg org)
    {
        //todo 机构编号生成
        if (UserConstants.NOT_UNIQUE.equals(orgService.checkOrgNameUnique(org)))
        {
            return error("新增机构'" + org.getOrgName() + "'失败，机构名称已存在");
        }
        org.setCreateBy(getUsername());
        return toAjax(orgService.insertOrg(org));
    }

    /**
     * 修改机构
     */
    @PreAuthorize("@ss.hasPermi('system:org:edit')")
    @Log(title = "机构管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TOrg org)
    {
        Long orgId = org.getOrgId();
        orgService.checkOrgDataScope(orgId);
        if (UserConstants.NOT_UNIQUE.equals(orgService.checkOrgNameUnique(org)))
        {
            return error("修改机构'" + org.getOrgName() + "'失败，机构名称已存在");
        }
        else if (org.getParentId().equals(orgId))
        {
            return error("修改机构'" + org.getOrgName() + "'失败，上级机构不能是自己");
        }
        org.setUpdateBy(getUsername());
        return toAjax(orgService.updateOrg(org));
    }

    /**
     * 删除机构
     */
    @PreAuthorize("@ss.hasPermi('system:org:remove')")
    @Log(title = "机构管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{orgId}")
    public AjaxResult remove(@PathVariable Long orgId)
    {
        if (orgService.hasChildByOrgId(orgId))
        {
            return warn("存在下级机构,不允许删除");
        }
        if (orgService.checkOrgExistUser(orgId))
        {
            return warn("机构存在用户,不允许删除");
        }
        orgService.checkOrgDataScope(orgId);
        return toAjax(orgService.deleteOrgById(orgId));
    }


    /**
     * 导入设备模板
     * @param response
     */
    @PostMapping("/devices/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<TDevice> util = new ExcelUtil<TDevice>(TDevice.class);
        util.importTemplateExcel(response, "设备数据");
    }


    @Log(title = "机构管理-导入设备", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:org-device:import')")
    @PostMapping("/devices/importData")
    public AjaxResult importData(MultipartFile file, Long orgId) throws Exception
    {
        ExcelUtil<TDevice> util = new ExcelUtil<TDevice>(TDevice.class);
        List<TDevice> userList = util.importExcel(file.getInputStream());
        String message = deviceService.importDevice(userList, orgId);
        return success(message);
    }

}
