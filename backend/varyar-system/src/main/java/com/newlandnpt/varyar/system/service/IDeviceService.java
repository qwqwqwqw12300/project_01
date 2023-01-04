package com.newlandnpt.varyar.system.service;

import java.util.List;
import com.newlandnpt.varyar.system.domain.Device;

/**
 * 设备Service接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface IDeviceService
{
    /**
     * 查询设备
     * 
     * @param deviceId 设备主键
     * @return 设备
     */
    public Device selectDeviceByDeviceId(Long deviceId);

    /**
     * 查询设备列表
     * 
     * @param device 设备
     * @return 设备集合
     */
    public List<Device> selectDeviceList(Device device);
    /**
     * 查询设备列表
     *
     * @param device 设备
     * @return 设备集合
     */
    public List<Device> selectDeviceByMemberId(Long memberId);
    /**
     * 新增设备
     * 
     * @param device 设备
     * @return 结果
     */
    public int insertDevice(Device device);

    /**
     * 修改设备
     * 
     * @param device 设备
     * @return 结果
     */
    public int updateDevice(Device device);

    /**
     * 批量删除设备
     * 
     * @param deviceIds 需要删除的设备主键集合
     * @return 结果
     */
    public int deleteDeviceByDeviceIds(Long[] deviceIds);

    /**
     * 删除设备信息
     * 
     * @param deviceId 设备主键
     * @return 结果
     */
    public int deleteDeviceByDeviceId(Long deviceId);

    /**
     * 分配设备给设备组
     * @param deviceIds
     * @param deviceGroupId
     * @return
     */
    public int arrangeDeviceToGroup(Long[] deviceIds, Long deviceGroupId);


    /**
     * 根据设备号查询设备
     * @param deviceNo
     * @return
     */
    public Device selectByDeviceNo(String deviceNo);

    /**
     * 设备配对
     * @param device
     * @return
     */
    public int associate(Device device);

    /**
     * 激活设备
     * @param deviceId
     * @return
     */
    public int active(Long deviceId);

    /**
     * 设备下线
     * @param deviceId
     * @return
     */
    public int offline(Long deviceId);

    /**
     * 导入设备
     * @param devices 设备列表
     * @param orgId 机构id
     * @return
     */
    public String importDevice(List<Device> devices, Long orgId);

    /**
     * 统计设备数量
     * @return
     */
    public long total();

}
