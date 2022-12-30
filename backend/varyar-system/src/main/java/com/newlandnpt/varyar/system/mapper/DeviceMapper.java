package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.Device;
import org.apache.ibatis.annotations.Param;

/**
 * 设备Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface DeviceMapper
{
    /**
     * 查询设备
     * 
     * @param deviceId 设备主键
     * @return 设备
     */
    public Device selectTDeviceByDeviceId(Long deviceId);

    /**
     * 查询设备列表
     * 
     * @param device 设备
     * @return 设备集合
     */
    public List<Device> selectTDeviceList(Device device);

    /**
     * 新增设备
     * 
     * @param device 设备
     * @return 结果
     */
    public int insertTDevice(Device device);

    /**
     * 修改设备
     * 
     * @param device 设备
     * @return 结果
     */
    public int updateTDevice(Device device);

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

    /**
     * 根据设备号查询设备
     * @param deviceNo
     * @return
     */
    public Device selectByDeviceNo(String deviceNo);

    /**
     * 将设备分配给设备组
     * @param deviceIds
     * @param deviceGroupId
     * @param updateBy
     * @return
     */
    public int arrangeDeviceToGroup(@Param("deviceIds")Long[] deviceIds,@Param("deviceGroupId") Long deviceGroupId,@Param("updateBy") String updateBy);
}
