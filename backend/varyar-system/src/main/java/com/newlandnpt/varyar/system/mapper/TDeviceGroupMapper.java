package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TDeviceGroup;

/**
 * 设备组Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface TDeviceGroupMapper
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
     * 删除设备组
     * 
     * @param deviceGroupId 设备组主键
     * @return 结果
     */
    public int deleteTDeviceGroupByDeviceGroupId(Long deviceGroupId);

    /**
     * 批量删除设备组
     * 
     * @param deviceGroupIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTDeviceGroupByDeviceGroupIds(Long[] deviceGroupIds);

    /**
     * 分配设备组给用户
     * @param deviceGroupIds
     * @param userId
     * @param updateBy
     * @return
     */
    public int arrangeTDeviceGroupsUser(Long[] deviceGroupIds,Long userId,String updateBy );
}
