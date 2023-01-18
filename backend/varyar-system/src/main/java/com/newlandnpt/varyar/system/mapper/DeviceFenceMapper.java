package com.newlandnpt.varyar.system.mapper;

import com.newlandnpt.varyar.system.domain.TDeviceFence;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 设备电子围栏Mapper接口
 *
 * @author chenxw
 * @date 2023-01-06
 */
@Repository
public interface DeviceFenceMapper
{
    /**
     * 查询设备电子围栏
     *
     * @param deviceFenceId 设备电子围栏主键
     * @return 设备电子围栏
     */
    public TDeviceFence selectTDeviceFenceByDeviceFenceId(Long deviceFenceId);

    /**
     * 根据设备号查询设备
     * @param deviceNo 设备号
     * @return 设备电子围栏
     */
    public TDeviceFence selectTDeviceFenceByDeviceNo(String deviceNo);
    public List<TDeviceFence>  selectTDeviceFenceByDeviceId(Long deviceId);
    /**
     * 查询设备电子围栏列表
     *
     * @param tDeviceFence 设备电子围栏
     * @return 设备电子围栏集合
     */
    public List<TDeviceFence> selectTDeviceFenceList(TDeviceFence tDeviceFence);

    /**
     * 新增设备电子围栏
     *
     * @param tDeviceFence 设备电子围栏
     * @return 结果
     */
    public int insertTDeviceFence(TDeviceFence tDeviceFence);

    /**
     * 修改设备电子围栏
     *
     * @param tDeviceFence 设备电子围栏
     * @return 结果
     */
    public int updateTDeviceFence(TDeviceFence tDeviceFence);

    /**
     * 删除设备电子围栏
     *
     * @param deviceFenceId 设备电子围栏主键
     * @return 结果
     */
    public int deleteTDeviceFenceByDeviceFenceId(Long deviceFenceId);

    /**
     * 批量删除设备电子围栏
     *
     * @param deviceFenceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTDeviceFenceByDeviceFenceIds(Long[] deviceFenceIds);
}
