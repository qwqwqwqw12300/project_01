package com.newlandnpt.varyar.system.service;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TDeviceGroup;

/**
 * 设备组Service接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface ITDeviceGroupService
{
    /**
     * 查询设备组
     * 
     * @param deviceGroupId 设备组主键
     * @return 设备组
     */
    public TDeviceGroup selectTDeviceGroupByDeviceGroupId(Long deviceGroupId);

    /**
     * 查询设备组列表
     * 
     * @param tDevicegroup 设备组
     * @return 设备组集合
     */
    public List<TDeviceGroup> selectTDeviceGroupList(TDeviceGroup tDevicegroup);

    /**
     * 新增设备组
     * 
     * @param tDevicegroup 设备组
     * @return 结果
     */
    public int insertTDeviceGroup(TDeviceGroup tDevicegroup);

    /**
     * 修改设备组
     * 
     * @param tDevicegroup 设备组
     * @return 结果
     */
    public int updateTDeviceGroup(TDeviceGroup tDevicegroup);

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
    public int deleteTDeviceGroupByDeviceGroupIds(Long[] deviceGroupIds);

    /**
     * 删除设备组信息
     * 
     * @param deviceGroupId 设备组主键
     * @return 结果
     */
    public int deleteTDeviceGroupByDeviceGroupId(Long deviceGroupId);


    /**
     * 校验名称在机构内是否唯一
     *
     * @param deviceGroup 设备组信息
     * @return 结果
     */
    public String checkOrgNameUnique(TDeviceGroup deviceGroup);
}
