package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TDevicegroup;

/**
 * 设备组Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface TDevicegroupMapper 
{
    /**
     * 查询设备组
     * 
     * @param devicegroupId 设备组主键
     * @return 设备组
     */
    public TDevicegroup selectTDevicegroupByDevicegroupId(Long devicegroupId);

    /**
     * 查询设备组列表
     * 
     * @param tDevicegroup 设备组
     * @return 设备组集合
     */
    public List<TDevicegroup> selectTDevicegroupList(TDevicegroup tDevicegroup);

    /**
     * 新增设备组
     * 
     * @param tDevicegroup 设备组
     * @return 结果
     */
    public int insertTDevicegroup(TDevicegroup tDevicegroup);

    /**
     * 修改设备组
     * 
     * @param tDevicegroup 设备组
     * @return 结果
     */
    public int updateTDevicegroup(TDevicegroup tDevicegroup);

    /**
     * 删除设备组
     * 
     * @param devicegroupId 设备组主键
     * @return 结果
     */
    public int deleteTDevicegroupByDevicegroupId(Long devicegroupId);

    /**
     * 批量删除设备组
     * 
     * @param devicegroupIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTDevicegroupByDevicegroupIds(Long[] devicegroupIds);
}
