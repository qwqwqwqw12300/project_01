package com.newlandnpt.varyar.system.service.impl;

import java.util.List;
import java.util.Optional;

import com.newlandnpt.varyar.common.annotation.DataScope;
import com.newlandnpt.varyar.common.core.domain.entity.SysUser;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.SecurityUtils;
import com.newlandnpt.varyar.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TDeviceGroupMapper;
import com.newlandnpt.varyar.system.domain.TDeviceGroup;
import com.newlandnpt.varyar.system.service.ITDeviceGroupService;

import static com.newlandnpt.varyar.common.utils.SecurityUtils.getLoginUserName;

/**
 * 设备组Service业务层处理
 *
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class TDeviceGroupServiceImpl implements ITDeviceGroupService {
    @Autowired
    private TDeviceGroupMapper tDevicegroupMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询设备组
     *
     * @param deviceGroupId 设备组主键
     * @return 设备组
     */
    @Override
    public TDeviceGroup selectTDeviceGroupByDeviceGroupId(Long deviceGroupId) {
        return tDevicegroupMapper.selectTDeviceGroupByDeviceGroupId(deviceGroupId);
    }

    /**
     * 查询设备组列表
     *
     * @param tDevicegroup 设备组
     * @return 设备组
     */
    @DataScope(orgAlias = "d")
    @Override
    public List<TDeviceGroup> selectTDeviceGroupList(TDeviceGroup tDevicegroup) {
        return tDevicegroupMapper.selectTDeviceGroupList(tDevicegroup);
    }

    /**
     * 新增设备组
     *
     * @param tDevicegroup 设备组
     * @return 结果
     */
    @Override
    public int insertTDeviceGroup(TDeviceGroup tDevicegroup) {
        //todo 生成设备组编号
        //默认是当前管理员所属机构
        tDevicegroup.setOrgId(SecurityUtils.getLoginUser().getUser().getOrgId());
        //创建人为当前登录人员
        tDevicegroup.autoSetCreateByLoginUser();
        return tDevicegroupMapper.insertTDeviceGroup(tDevicegroup);
    }

    /**
     * 修改设备组
     *
     * @param tDevicegroup 设备组
     * @return 结果
     */
    @Override
    public int updateTDeviceGroup(TDeviceGroup tDevicegroup) {
        // 运营者信息发生在分配设备组内修改，这里置null不做修改
        tDevicegroup.setUserId(null);
        tDevicegroup.setUserName(null);
        tDevicegroup.autoSetUpdateByLoginUser();
        return tDevicegroupMapper.updateTDeviceGroup(tDevicegroup);
    }

    @Override
    public int arrangeDeviceGroups(Long[] deviceGroupIds, Long userId) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        if (sysUser == null) {
            throw new ServiceException("运营人员不存在");
        }
        return tDevicegroupMapper.arrangeTDeviceGroupsUser(deviceGroupIds, userId,getLoginUserName());
    }

    /**
     * 批量删除设备组
     *
     * @param deviceGroupIds 需要删除的设备组主键
     * @return 结果
     */
    @Override
    public int deleteTDeviceGroupByDeviceGroupIds(Long[] deviceGroupIds) {
        return tDevicegroupMapper.deleteTDeviceGroupByDeviceGroupIds(deviceGroupIds);
    }

    /**
     * 删除设备组信息
     *
     * @param deviceGroupId 设备组主键
     * @return 结果
     */
    @Override
    public int deleteTDeviceGroupByDeviceGroupId(Long deviceGroupId) {
        return tDevicegroupMapper.deleteTDeviceGroupByDeviceGroupId(deviceGroupId);
    }
}
