package com.newlandnpt.varyar.system.service;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TDeviceFence;

/**
 * 设备电子围栏Service接口
 *
 * @author chenxw
 * @date 2023-01-06
 */
public interface IDeviceFenceService
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
     * @param deviceNo
     * @return
     */
    public TDeviceFence selectTDeviceFenceByDeviceNo(String deviceNo);

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
     * 批量删除设备电子围栏
     *
     * @param deviceFenceIds 需要删除的设备电子围栏主键集合
     * @return 结果
     */
    public int deleteTDeviceFenceByDeviceFenceIds(Long[] deviceFenceIds);

    /**
     * 删除设备电子围栏信息
     *
     * @param deviceFenceId 设备电子围栏主键
     * @return 结果
     */
    public int deleteTDeviceFenceByDeviceFenceId(Long deviceFenceId);
}
