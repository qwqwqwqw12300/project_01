package com.newlandnpt.varyar.system.service;

import java.util.List;
import java.util.Map;

import com.newlandnpt.varyar.common.core.domain.vo.ExtraVo;
import com.newlandnpt.varyar.common.core.domain.vo.TrackerTargetVo;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.domain.dto.org.OrgDeviceCountDto;

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
    public TDevice selectDeviceByDeviceId(Long deviceId);

    /**
     * 查询设备列表
     * 
     * @param device 设备
     * @return 设备集合
     */
    public List<TDevice> selectDeviceList(TDevice device);
    /**
     * 查询设备列表
     *
     * @param memberId 设备
     * @return 设备集合
     */
    public List<TDevice> selectDeviceByMember(Long memberId);
    /**
     * 查询设备列表
     *
     * @param map 设备
     * @return 设备集合
     */
    public List<TDevice> selectDeviceByMemberId(Map map);
    /**
     * 新增设备
     * 
     * @param device 设备
     * @return 结果
     */
    public int insertDevice(TDevice device);

    /**
     * 修改设备
     * 
     * @param device 设备
     * @return 结果
     */
    public int updateDevice(TDevice device);
    public int setDevice(TDevice device);
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
    public TDevice selectByDeviceNo(String deviceNo);

    /**
     * 设备配对
     * @param device
     * @return
     */
    public int associate(TDevice device);

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
    public String importDevice(List<TDevice> devices, Long orgId);

    /**
     * 统计设备数量
     * @return
     * @param device
     */
    public long total(TDevice device);

    /**
     * 统计未激活设备数量
     * @return
     * @param device
     */
    public long notActiveDeviceCount(TDevice device);

    /**
     * 统计未分配(配对)设备数量
     * @return
     * @param device
     */
    public long notAssociateDeviceCount(TDevice device);

    /**
     * 统计未分组设备数量
     * @return
     * @param device
     */
    public long notArrangeDeviceCount(TDevice device);

    /**
     * 根据机构id分组统计设备数
     * @return
     * @param device
     */
    public List<OrgDeviceCountDto> countGroupByOrgId(TDevice device);

    /**
     * 获取运营关心的设备列表
     * @param userId
     * @return
     */
    public List<TDevice> selectBizCareDeviceList(Long userId);

    /**
     * 加载设备缓存数据
     */
    public void loadingDeviceCache();

    /**
     * 根据设备号从缓存中加载设备信息
     * @param deviceNo
     * @return
     */
    public TDevice loadDeviceFromCacheByNo(String deviceNo);

    /**
     * 加载设备参数配置
     * @param deviceId
     * @return
     */
    public TDevice.DeviceParameter loadSettings(Long deviceId);

    /**
     * 设置设备参数
     *
     * @param deviceId
     * @param settings
     * @return
     */
    public int setSettings(Long deviceId, TDevice.DeviceParameter settings);
    /**
     * 加载设备组状态
     * @param
     * @return
     */
    public List<TDevice> loadingDeviceStauts(List<TDevice> devices);
    /**
     * 加载单设备状态
     * @param
     * @return
     */
    public TDevice loadingDeviceStauts(TDevice devices);

    /**
     * 激活设备实时位置监控
     * @param deviceId
     */
    public void activeRealLocationMonitor(Long deviceId);

    /**
     * 取消设备实时位置监控
     * @param deviceId
     */
    public void cancelRealLocationMonitor(Long deviceId);

    /**
     * 根据设备号获取设备位置实时监控信息
     * @param deviceNo
     * @return
     */
    public List<TrackerTargetVo> getRealLocationMonitorByDeviceNo(String deviceNo);
    /**
     * 根据设备号获取设备位置实时监控跌倒信息
     * @param deviceNo
     * @return
     */
    public List<ExtraVo> getRealLocationExtraByDeviceNo(String deviceNo);

}
