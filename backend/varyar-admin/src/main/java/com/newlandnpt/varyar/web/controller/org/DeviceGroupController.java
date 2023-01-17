package com.newlandnpt.varyar.web.controller.org;

import java.util.List;

import com.newlandnpt.varyar.common.constant.UserConstants;
import com.newlandnpt.varyar.common.core.domain.entity.SysUser;
import com.newlandnpt.varyar.common.utils.uuid.IdUtils;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.IDeviceService;
import com.newlandnpt.varyar.system.service.IOrgService;
import com.newlandnpt.varyar.system.service.ISysUserService;
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
import com.newlandnpt.varyar.system.service.IDeviceGroupService;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;

/**
 * 设备组Controller
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@RestController
@RequestMapping("/org/devicegroup")
public class DeviceGroupController extends BaseController
{
    @Autowired
    private IDeviceGroupService deviceGroupService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IOrgService orgService;
    @Autowired
    private ISysUserService userService;

    /**
     * 查询设备组列表
     */
    @PreAuthorize("@ss.hasAnyPermi('org:deviceGroup:list,device:groupArrange')")
    @GetMapping("/page")
    public TableDataInfo page(TDeviceGroup devicegroup)
    {
        startPage();
        List<TDeviceGroup> list = deviceGroupService.selectDeviceGroupList(devicegroup);
        return getDataTable(list);
    }

    /**
     * 获取设备组详细信息
     */
    @PreAuthorize("@ss.hasPermi('org:deviceGroup:query')")
    @GetMapping(value = "/{devicegroupId}")
    public AjaxResult getInfo(@PathVariable("devicegroupId") Long devicegroupId)
    {
        return success(deviceGroupService.selectDeviceGroupByDeviceGroupId(devicegroupId));
    }

    /**
     * 新增设备组
     */
    @PreAuthorize("@ss.hasPermi('org:deviceGroup:add')")
    @Log(title = "设备组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TDeviceGroup devicegroup)
    {
        //默认是当前管理员所属机构
//        devicegroup.setOrgId(SecurityUtils.getLoginUser().getUser().getOrgId());
        devicegroup.setNo(IdUtils.fastSimpleUUID());
        if (UserConstants.NOT_UNIQUE.equals(deviceGroupService.checkOrgNameUnique(devicegroup)))
        {
            return error("新增设备组'" + devicegroup.getName() + "'失败，设备组名称已存在");
        }
        return toAjax(deviceGroupService.insertDeviceGroup(devicegroup));
    }

    /**
     * 修改设备组
     */
    @PreAuthorize("@ss.hasPermi('org:deviceGroup:edit')")
    @Log(title = "设备组", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TDeviceGroup devicegroup)
    {
        orgService.checkOrgDataScope(devicegroup.getOrgId());
        if (UserConstants.NOT_UNIQUE.equals(deviceGroupService.checkOrgNameUnique(devicegroup)))
        {
            return error("修改设备组'" + devicegroup.getName() + "'失败，设备组名称已存在");
        }
        return toAjax(deviceGroupService.updateDeviceGroup(devicegroup));
    }

    /**
     * 修改运营人员
     */
    @PreAuthorize("@ss.hasPermi('org:deviceGroup:arrangeUser')")
    @Log(title = "设备组-运营人员", businessType = BusinessType.UPDATE)
    @PutMapping("/arrange/user/{userId}")
    public AjaxResult arrangeDeviceGroups(@RequestBody Long[] deviceGroupIds,@PathVariable Long userId)
    {
        for(Long deviceGroupId:deviceGroupIds){
            TDeviceGroup deviceGroup = deviceGroupService.selectDeviceGroupByDeviceGroupId(deviceGroupId);
            if(deviceGroup!=null){
                orgService.checkOrgDataScope(deviceGroup.getOrgId());
            }
        }
        SysUser user = userService.selectUserById(userId);
        if(user!=null){
            orgService.checkOrgDataScope(user.getOrgId());
        }

        return toAjax(deviceGroupService.arrangeDeviceGroups(deviceGroupIds,userId));
    }

    /**
     * 设备组分配设备
     */
    @PreAuthorize("@ss.hasPermi('org:deviceGroup:deviceArrange')")
    @Log(title = "设备组-分配设备", businessType = BusinessType.UPDATE)
    @PutMapping({"{deviceGroupId}/devices/arrange","/devices/arrange"})
    public AjaxResult edit(@RequestBody Long[] deviceIds,@PathVariable(required = false) Long deviceGroupId)
    {

        TDeviceGroup deviceGroup = deviceGroupService.selectDeviceGroupByDeviceGroupId(deviceGroupId);
        if(deviceGroup!=null){
            orgService.checkOrgDataScope(deviceGroup.getOrgId());
        }
        return toAjax(deviceService.arrangeDeviceToGroup(deviceIds,deviceGroupId));
    }

    /**
     * 删除设备组
     */
    @PreAuthorize("@ss.hasPermi('org:deviceGroup:remove')")
    @Log(title = "设备组", businessType = BusinessType.DELETE)
	@DeleteMapping("/{devicegroupIds}")
    public AjaxResult remove(@PathVariable Long[] devicegroupIds)
    {
        for(Long deviceGroupId:devicegroupIds){
            TDeviceGroup deviceGroup = deviceGroupService.selectDeviceGroupByDeviceGroupId(deviceGroupId);
            if(deviceGroup!=null){
                orgService.checkOrgDataScope(deviceGroup.getOrgId());
            }
        }
        return toAjax(deviceGroupService.deleteDeviceGroupByDeviceGroupIds(devicegroupIds));
    }
}
