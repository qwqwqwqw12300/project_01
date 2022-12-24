package com.newlandnpt.varyar.system.service.impl;

import java.util.List;
import com.newlandnpt.varyar.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TDeviceMapper;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.ITDeviceService;

/**
 * 设备Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class TDeviceServiceImpl implements ITDeviceService 
{
    @Autowired
    private TDeviceMapper tDeviceMapper;

    /**
     * 查询设备
     * 
     * @param deviceId 设备主键
     * @return 设备
     */
    @Override
    public TDevice selectTDeviceByDeviceId(Long deviceId)
    {
        return tDeviceMapper.selectTDeviceByDeviceId(deviceId);
    }

    /**
     * 查询设备列表
     * 
     * @param tDevice 设备
     * @return 设备
     */
    @Override
    public List<TDevice> selectTDeviceList(TDevice tDevice)
    {
        return tDeviceMapper.selectTDeviceList(tDevice);
    }

    /**
     * 新增设备
     * 
     * @param tDevice 设备
     * @return 结果
     */
    @Override
    public int insertTDevice(TDevice tDevice)
    {
        tDevice.setCreateTime(DateUtils.getNowDate());
        return tDeviceMapper.insertTDevice(tDevice);
    }

    /**
     * 修改设备
     * 
     * @param tDevice 设备
     * @return 结果
     */
    @Override
    public int updateTDevice(TDevice tDevice)
    {
        tDevice.setUpdateTime(DateUtils.getNowDate());
        return tDeviceMapper.updateTDevice(tDevice);
    }

    /**
     * 批量删除设备
     * 
     * @param deviceIds 需要删除的设备主键
     * @return 结果
     */
    @Override
    public int deleteTDeviceByDeviceIds(Long[] deviceIds)
    {
        return tDeviceMapper.deleteTDeviceByDeviceIds(deviceIds);
    }

    /**
     * 删除设备信息
     * 
     * @param deviceId 设备主键
     * @return 结果
     */
    @Override
    public int deleteTDeviceByDeviceId(Long deviceId)
    {
        return tDeviceMapper.deleteTDeviceByDeviceId(deviceId);
    }
}
