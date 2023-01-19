package com.newlandnpt.varyar.system.service.impl;

import java.util.List;

import com.newlandnpt.varyar.common.annotation.DataScope;
import com.newlandnpt.varyar.common.constant.UserConstants;
import com.newlandnpt.varyar.common.core.domain.entity.SysUser;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TDeviceGroupMapper;
import com.newlandnpt.varyar.system.domain.TDeviceGroup;
import com.newlandnpt.varyar.system.service.IDeviceGroupService;

import static com.newlandnpt.varyar.common.utils.SecurityUtils.getLoginUserName;

/**
 * 设备组Service业务层处理
 *
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class DeviceGroupServiceImpl implements IDeviceGroupService {
    @Autowired
    private TDeviceGroupMapper devicegroupMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询设备组
     *
     * @param deviceGroupId 设备组主键
     * @return 设备组
     */
    @Override
    public TDeviceGroup selectDeviceGroupByDeviceGroupId(Long deviceGroupId) {
        return devicegroupMapper.selectDeviceGroupByDeviceGroupId(deviceGroupId);
    }

    /**
     * 查询设备组列表
     *
     * @param devicegroup 设备组
     * @return 设备组
     */
    @DataScope(orgAlias = "d")
    @Override
    public List<TDeviceGroup> selectDeviceGroupList(TDeviceGroup devicegroup) {
        return devicegroupMapper.selectDeviceGroupList(devicegroup);
    }

    /**
     * 新增设备组
     *
     * @param devicegroup 设备组
     * @return 结果
     */
    @Override
    public int insertDeviceGroup(TDeviceGroup devicegroup) {
        //创建人为当前登录人员
        devicegroup.autoSetCreateByLoginUser();
        return devicegroupMapper.insertDeviceGroup(devicegroup);
    }

    /**
     * 修改设备组
     *
     * @param devicegroup 设备组
     * @return 结果
     */
    @Override
    public int updateDeviceGroup(TDeviceGroup devicegroup) {
        // 运营者信息发生在分配设备组内修改，这里置null不做修改
        devicegroup.setUserId(null);
        devicegroup.setUserName(null);
        devicegroup.autoSetUpdateByLoginUser();
        return devicegroupMapper.updateDeviceGroup(devicegroup);
    }

    @Override
    public int arrangeDeviceGroups(Long[] deviceGroupIds, Long userId) {
        if(userId!=null){
            SysUser sysUser = sysUserMapper.selectUserById(userId);
            if (sysUser == null) {
                throw new ServiceException("运营人员不存在");
            }
        }
        return devicegroupMapper.arrangeDeviceGroupsUser(deviceGroupIds, userId, getLoginUserName());
    }

    /**
     * 批量删除设备组
     *
     * @param deviceGroupIds 需要删除的设备组主键
     * @return 结果
     */
    @Override
    public int deleteDeviceGroupByDeviceGroupIds(Long[] deviceGroupIds) {
        return devicegroupMapper.deleteDeviceGroupByDeviceGroupIds(deviceGroupIds);
    }

    /**
     * 删除设备组信息
     *
     * @param deviceGroupId 设备组主键
     * @return 结果
     */
    @Override
    public int deleteDeviceGroupByDeviceGroupId(Long deviceGroupId) {
        return devicegroupMapper.deleteDeviceGroupByDeviceGroupId(deviceGroupId);
    }


    /**
     * 校验名称是否唯一
     *
     * @param deviceGroup 设备组信息
     * @return 结果
     */
    @Override
    public String checkOrgNameUnique(TDeviceGroup deviceGroup) {
        TDeviceGroup info = devicegroupMapper.checkOrgNameUnique(deviceGroup.getName(), deviceGroup.getOrgId());
        if (StringUtils.isNotNull(info) && (deviceGroup.getDeviceGroupId() == null
                || info.getDeviceGroupId().longValue() != deviceGroup.getDeviceGroupId().longValue())) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public long notArrangeCount(TDeviceGroup deviceGroup) {
        return devicegroupMapper.notArrangeCount(deviceGroup);
    }
}
