package com.newlandnpt.varyar.system.mapper;

import com.newlandnpt.varyar.system.domain.DeviceGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 设备组Mapper接口
 *
 * @author ruoyi
 * @date 2022-12-24
 */
public interface DeviceGroupMapper {
    /**
     * 查询设备组
     *
     * @param deviceGroupId 设备组主键
     * @return 设备组
     */
    public DeviceGroup selectDeviceGroupByDeviceGroupId(Long deviceGroupId);

    /**
     * 查询设备组列表
     *
     * @param devicegroup 设备组
     * @return 设备组集合
     */
    public List<DeviceGroup> selectDeviceGroupList(DeviceGroup devicegroup);

    /**
     * 新增设备组
     *
     * @param devicegroup 设备组
     * @return 结果
     */
    public int insertDeviceGroup(DeviceGroup devicegroup);

    /**
     * 修改设备组
     *
     * @param devicegroup 设备组
     * @return 结果
     */
    public int updateDeviceGroup(DeviceGroup devicegroup);

    /**
     * 删除设备组
     *
     * @param deviceGroupId 设备组主键
     * @return 结果
     */
    public int deleteDeviceGroupByDeviceGroupId(Long deviceGroupId);

    /**
     * 批量删除设备组
     *
     * @param deviceGroupIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeviceGroupByDeviceGroupIds(Long[] deviceGroupIds);

    /**
     * 分配设备组给用户
     *
     * @param deviceGroupIds
     * @param userId
     * @param updateBy
     * @return
     */
    public int arrangeDeviceGroupsUser(@Param("deviceGroupIds")Long[] deviceGroupIds,@Param("userId") Long userId,@Param("updateBy") String updateBy);

    /**
     * 校验名称在机构内是否唯一
     *
     * @param name  设备组名称
     * @param orgId 机构ID
     * @return 结果
     */
    public DeviceGroup checkOrgNameUnique(@Param("name") String name, @Param("orgId") Long orgId);

    /**
     * 未分配设备组
     * @param deviceGroup
     * @return
     */
    @Select("select count(*) from t_devicegroup where org_id = #{orgId} and user_id in null")
    public long notArrangeCount(DeviceGroup deviceGroup);
}
