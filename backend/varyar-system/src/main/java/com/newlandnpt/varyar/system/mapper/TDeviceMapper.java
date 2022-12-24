package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TDevice;

/**
 * 设备Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface TDeviceMapper 
{
    /**
     * 查询设备
     * 
     * @param deviceId 设备主键
     * @return 设备
     */
    public TDevice selectTDeviceByDeviceId(Long deviceId);

    /**
     * 查询设备列表
     * 
     * @param tDevice 设备
     * @return 设备集合
     */
    public List<TDevice> selectTDeviceList(TDevice tDevice);

    /**
     * 新增设备
     * 
     * @param tDevice 设备
     * @return 结果
     */
    public int insertTDevice(TDevice tDevice);

    /**
     * 修改设备
     * 
     * @param tDevice 设备
     * @return 结果
     */
    public int updateTDevice(TDevice tDevice);

    /**
     * 删除设备
     * 
     * @param deviceId 设备主键
     * @return 结果
     */
    public int deleteTDeviceByDeviceId(Long deviceId);

    /**
     * 批量删除设备
     * 
     * @param deviceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTDeviceByDeviceIds(Long[] deviceIds);
}
