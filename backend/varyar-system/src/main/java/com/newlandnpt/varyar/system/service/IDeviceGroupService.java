package com.newlandnpt.varyar.system.service;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TDeviceGroup;

/**
 * 设备组Service接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface IDeviceGroupService
{
    /**
     * 查询设备组
     * 
     * @param deviceGroupId 设备组主键
     * @return 设备组
     */
    public TDeviceGroup selectDeviceGroupByDeviceGroupId(Long deviceGroupId);

    /**
     * 查询设备组列表
     * 
     * @param devicegroup 设备组
     * @return 设备组集合
     */
    public List<TDeviceGroup> selectDeviceGroupList(TDeviceGroup devicegroup);

    /**
     * 新增设备组
     * 
     * @param devicegroup 设备组
     * @return 结果
     */
    public int insertDeviceGroup(TDeviceGroup devicegroup);

    /**
     * 修改设备组
     * 
     * @param devicegroup 设备组
     * @return 结果
     */
    public int updateDeviceGroup(TDeviceGroup devicegroup);

    /**
     * 给运营人员分配设备组
     * @param deviceGroupIds
     * @param userId
     * @return
     */
    public int arrangeDeviceGroups(Long[] deviceGroupIds, Long userId);


    /**
     * 批量删除设备组
     * 
     * @param deviceGroupIds 需要删除的设备组主键集合
     * @return 结果
     */
    public int deleteDeviceGroupByDeviceGroupIds(Long[] deviceGroupIds);

    /**
     * 删除设备组信息
     * 
     * @param deviceGroupId 设备组主键
     * @return 结果
     */
    public int deleteDeviceGroupByDeviceGroupId(Long deviceGroupId);


    /**
     * 校验名称在机构内是否唯一
     *
     * @param deviceGroup 设备组信息
     * @return 结果
     */
    public String checkOrgNameUnique(TDeviceGroup deviceGroup);

    /**
     * 未分配设备组
     * @param deviceGroup
     * @return
     */
    public long notArrangeCount(TDeviceGroup deviceGroup);
}
