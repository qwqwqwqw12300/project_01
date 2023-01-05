package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import java.util.Map;

import com.newlandnpt.varyar.system.domain.Device;
import com.newlandnpt.varyar.system.domain.dto.org.OrgDeviceCountDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
     * 查询设备列表
     *
     * @param device 设备
     * @return 设备集合
     */
    public List<Device>  selectByMemberId(Map map);
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

    /**
     * 统计已激活设备数量（激活后即使设备下线也包括在内）
     * @return
     * @param device
     */
    @Select("select count(*) from t_device where status in ('1','2') ${params.dataScope} and del_flag = '0' ")
    public long total(Device device);

    /**
     * 统计未分配激活设备数量
     * @return
     * @param device
     */
    @Select("select count(*) from t_device where status in ('1','2') and distribute_flag = '0' ${params.dataScope} and del_flag = '0'")
    public long notAssociateDeviceCount(Device device);
    /**
     * 统计未分组激活设备数量
     * @return
     * @param device
     */
    @Select("select count(*) from t_device where status in ('1','2') and devicegroup_id is null ${params.dataScope} and del_flag = '0'")
    public long notArrangeDeviceCount(Device device);

    /**
     * 根据机构id分组统计设备数
     * @return
     * @param device
     */
    @Select("select org_id as orgId,count(*) as count from t_device where status in ('1','2') ${params.dataScope} and del_flag = '0'" +
            "group by org_id")
    public List<OrgDeviceCountDto> countGroupByOrgId(Device device);

    /**
     * 查询设备列表
     *
     * @param userId 用户id
     * @return 设备集合
     */
    public List<Device> selectBizCareDeviceList(Long userId);
}
