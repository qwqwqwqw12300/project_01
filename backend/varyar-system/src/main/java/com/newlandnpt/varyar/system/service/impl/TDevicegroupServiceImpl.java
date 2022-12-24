package com.newlandnpt.varyar.system.service.impl;

import java.util.List;
import com.newlandnpt.varyar.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TDevicegroupMapper;
import com.newlandnpt.varyar.system.domain.TDevicegroup;
import com.newlandnpt.varyar.system.service.ITDevicegroupService;

/**
 * 设备组Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class TDevicegroupServiceImpl implements ITDevicegroupService 
{
    @Autowired
    private TDevicegroupMapper tDevicegroupMapper;

    /**
     * 查询设备组
     * 
     * @param devicegroupId 设备组主键
     * @return 设备组
     */
    @Override
    public TDevicegroup selectTDevicegroupByDevicegroupId(Long devicegroupId)
    {
        return tDevicegroupMapper.selectTDevicegroupByDevicegroupId(devicegroupId);
    }

    /**
     * 查询设备组列表
     * 
     * @param tDevicegroup 设备组
     * @return 设备组
     */
    @Override
    public List<TDevicegroup> selectTDevicegroupList(TDevicegroup tDevicegroup)
    {
        return tDevicegroupMapper.selectTDevicegroupList(tDevicegroup);
    }

    /**
     * 新增设备组
     * 
     * @param tDevicegroup 设备组
     * @return 结果
     */
    @Override
    public int insertTDevicegroup(TDevicegroup tDevicegroup)
    {
        tDevicegroup.setCreateTime(DateUtils.getNowDate());
        return tDevicegroupMapper.insertTDevicegroup(tDevicegroup);
    }

    /**
     * 修改设备组
     * 
     * @param tDevicegroup 设备组
     * @return 结果
     */
    @Override
    public int updateTDevicegroup(TDevicegroup tDevicegroup)
    {
        tDevicegroup.setUpdateTime(DateUtils.getNowDate());
        return tDevicegroupMapper.updateTDevicegroup(tDevicegroup);
    }

    /**
     * 批量删除设备组
     * 
     * @param devicegroupIds 需要删除的设备组主键
     * @return 结果
     */
    @Override
    public int deleteTDevicegroupByDevicegroupIds(Long[] devicegroupIds)
    {
        return tDevicegroupMapper.deleteTDevicegroupByDevicegroupIds(devicegroupIds);
    }

    /**
     * 删除设备组信息
     * 
     * @param devicegroupId 设备组主键
     * @return 结果
     */
    @Override
    public int deleteTDevicegroupByDevicegroupId(Long devicegroupId)
    {
        return tDevicegroupMapper.deleteTDevicegroupByDevicegroupId(devicegroupId);
    }
}
