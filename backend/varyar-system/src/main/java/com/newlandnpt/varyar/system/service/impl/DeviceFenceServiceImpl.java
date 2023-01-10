package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.system.domain.TDeviceFence;
import com.newlandnpt.varyar.system.mapper.DeviceFenceMapper;
import com.newlandnpt.varyar.system.service.IDeviceFenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备电子围栏Service业务层处理
 *
 * @author ruoyi
 * @date 2023-01-06
 */
@Service
public class DeviceFenceServiceImpl implements IDeviceFenceService
{
    @Autowired
    private DeviceFenceMapper deviceFenceMapper;

    /**
     * 查询设备电子围栏
     *
     * @param deviceFenceId 设备电子围栏主键
     * @return 设备电子围栏
     */
    @Override
    public TDeviceFence selectTDeviceFenceByDeviceFenceId(Long deviceFenceId)
    {
        return deviceFenceMapper.selectTDeviceFenceByDeviceFenceId(deviceFenceId);
    }

    @Override
    public TDeviceFence selectTDeviceFenceByDeviceNo(String deviceNo) {
        return deviceFenceMapper.selectTDeviceFenceByDeviceNo(deviceNo);
    }

    /**
     * 查询设备电子围栏列表
     *
     * @param tDeviceFence 设备电子围栏
     * @return 设备电子围栏
     */
    @Override
    public List<TDeviceFence> selectTDeviceFenceList(TDeviceFence tDeviceFence)
    {
        return deviceFenceMapper.selectTDeviceFenceList(tDeviceFence);
    }

    /**
     * 新增设备电子围栏
     *
     * @param tDeviceFence 设备电子围栏
     * @return 结果
     */
    @Override
    public int insertTDeviceFence(TDeviceFence tDeviceFence)
    {
        //获取设备id,存在则修改原记录
        Long DeviceFenceId = tDeviceFence.getDeviceFenceId();
        TDeviceFence tDeviceFenceQuery = deviceFenceMapper.selectTDeviceFenceByDeviceFenceId(DeviceFenceId);
        if (tDeviceFenceQuery != null) {
            tDeviceFence.setUpdateTime(DateUtils.getNowDate());
            return deviceFenceMapper.updateTDeviceFence(tDeviceFence);
        }

        tDeviceFence.setCreateTime(DateUtils.getNowDate());
        return deviceFenceMapper.insertTDeviceFence(tDeviceFence);
    }

    /**
     * 修改设备电子围栏
     *
     * @param tDeviceFence 设备电子围栏
     * @return 结果
     */
    @Override
    public int updateTDeviceFence(TDeviceFence tDeviceFence)
    {
        tDeviceFence.setUpdateTime(DateUtils.getNowDate());
        return deviceFenceMapper.updateTDeviceFence(tDeviceFence);
    }

    /**
     * 批量删除设备电子围栏
     *
     * @param deviceFenceIds 需要删除的设备电子围栏主键
     * @return 结果
     */
    @Override
    public int deleteTDeviceFenceByDeviceFenceIds(Long[] deviceFenceIds)
    {
        return deviceFenceMapper.deleteTDeviceFenceByDeviceFenceIds(deviceFenceIds);
    }

    /**
     * 删除设备电子围栏信息
     *
     * @param deviceFenceId 设备电子围栏主键
     * @return 结果
     */
    @Override
    public int deleteTDeviceFenceByDeviceFenceId(Long deviceFenceId)
    {
        return deviceFenceMapper.deleteTDeviceFenceByDeviceFenceId(deviceFenceId);
    }
}
