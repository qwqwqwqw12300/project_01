package com.newlandnpt.varyar.web.controller.system;

import java.util.List;

import com.newlandnpt.varyar.common.constant.UserConstants;
import com.newlandnpt.varyar.common.utils.SecurityUtils;
import com.newlandnpt.varyar.system.service.ITDeviceService;
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
import com.newlandnpt.varyar.system.domain.TDeviceGroup;
import com.newlandnpt.varyar.system.service.ITDeviceGroupService;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;

/**
 * 设备组Controller
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@RestController
@RequestMapping("/system/devicegroup")
public class TDeviceGroupController extends BaseController
{
    @Autowired
    private ITDeviceGroupService tDeviceGroupService;
    @Autowired
    private ITDeviceService tDeviceService;

    /**
     * 查询设备组列表
     */
    @PreAuthorize("@ss.hasPermi('system:devicegroup:list')")
    @GetMapping("/page")
    public TableDataInfo page(TDeviceGroup tDevicegroup)
    {
        startPage();
        List<TDeviceGroup> list = tDeviceGroupService.selectTDeviceGroupList(tDevicegroup);
        return getDataTable(list);
    }

    /**
     * 获取设备组详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:devicegroup:query')")
    @GetMapping(value = "/{devicegroupId}")
    public AjaxResult getInfo(@PathVariable("devicegroupId") Long devicegroupId)
    {
        return success(tDeviceGroupService.selectTDeviceGroupByDeviceGroupId(devicegroupId));
    }

    /**
     * 新增设备组
     */
    @PreAuthorize("@ss.hasPermi('system:devicegroup:add')")
    @Log(title = "设备组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TDeviceGroup tDevicegroup)
    {
        //默认是当前管理员所属机构
        tDevicegroup.setOrgId(SecurityUtils.getLoginUser().getUser().getOrgId());
        if (UserConstants.NOT_UNIQUE.equals(tDeviceGroupService.checkOrgNameUnique(tDevicegroup)))
        {
            return error("新增设备组'" + tDevicegroup.getName() + "'失败，设备组名称已存在");
        }
        return toAjax(tDeviceGroupService.insertTDeviceGroup(tDevicegroup));
    }

    /**
     * 修改设备组
     */
    @PreAuthorize("@ss.hasPermi('system:devicegroup:edit')")
    @Log(title = "设备组", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TDeviceGroup tDevicegroup)
    {
        if (UserConstants.NOT_UNIQUE.equals(tDeviceGroupService.checkOrgNameUnique(tDevicegroup)))
        {
            return error("修改设备组'" + tDevicegroup.getName() + "'失败，设备组名称已存在");
        }
        return toAjax(tDeviceGroupService.updateTDeviceGroup(tDevicegroup));
    }

    /**
     * 修改运营人员
     */
    @PreAuthorize("@ss.hasPermi('system:devicegroup:arrangeUser')")
    @Log(title = "设备组-运营人员", businessType = BusinessType.UPDATE)
    @PutMapping("/arrange/user/{userId}")
    public AjaxResult arrangeDeviceGroups(@RequestBody Long[] deviceGroupIds,@PathVariable Long userId)
    {
        return toAjax(tDeviceGroupService.arrangeDeviceGroups(deviceGroupIds,userId));
    }

    /**
     * 设备组分配设备
     */
    @PreAuthorize("@ss.hasPermi('system:devicegroup:deviceArrange')")
    @Log(title = "设备组-分配设备", businessType = BusinessType.UPDATE)
    @PutMapping("{deviceGroupId}/devices/arrange")
    public AjaxResult edit(@RequestBody Long[] deviceIds,@PathVariable Long deviceGroupId)
    {
        return toAjax(tDeviceService.arrangeDeviceToGroup(deviceIds,deviceGroupId));
    }

    /**
     * 删除设备组
     */
    @PreAuthorize("@ss.hasPermi('system:devicegroup:remove')")
    @Log(title = "设备组", businessType = BusinessType.DELETE)
	@DeleteMapping("/{devicegroupIds}")
    public AjaxResult remove(@PathVariable Long[] devicegroupIds)
    {
        return toAjax(tDeviceGroupService.deleteTDeviceGroupByDeviceGroupIds(devicegroupIds));
    }
}
