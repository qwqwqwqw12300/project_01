package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import java.util.Map;

import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.domain.dto.org.OrgDeviceCountDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
     * 查询设备
     *
     * @param memberId 会员id
     * @return 设备
     */
    public List<TDevice> selectTDeviceByMember(Long memberId);

    /**
     * 查询设备列表
     * 
     * @param device 设备
     * @return 设备集合
     */
    public List<TDevice> selectTDeviceList(TDevice device);
    /**
     * 查询设备列表
     *
     * @param device 设备
     * @return 设备集合
     */
    public List<TDevice>  selectByMemberId(Map map);
    /**
     * 新增设备
     * 
     * @param device 设备
     * @return 结果
     */
    public int insertTDevice(TDevice device);

    /**
     * 修改设备
     * 
     * @param device 设备
     * @return 结果
     */
    public int updateTDevice(TDevice device);
    /**
     * 解绑设备
     *
     * @param device 设备
     * @return 结果
     */
    public int  relievTDevice(TDevice device);
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
    public TDevice selectByDeviceNo(String deviceNo);

    /**
     * 将设备分配给设备组
     * @param deviceIds
     * @param deviceGroupId
     * @param updateBy
     * @return
     */
    public int arrangeDeviceToGroup(@Param("deviceIds")Long[] deviceIds,@Param("deviceGroupId") Long deviceGroupId,@Param("updateBy") String updateBy);

    /**
     * 清空设备的设备组
     * @param deviceGroupId
     * @param updateBy
     * @return
     */
    public int clearDeviceGroup(@Param("deviceGroupId") Long deviceGroupId,@Param("updateBy") String updateBy);

    /**
     * 统计设备数量（激活后即使设备下线也包括在内）
     * @return
     * @param device
     */
    @Select("select count(*) from t_device where 1=1 ${params.dataScope} and del_flag = '0' ")
    public long total(TDevice device);

    /**
     * 统计未分配激活设备数量
     * @return
     * @param device
     */
    @Select("select count(*) from t_device d where status in ('0','1','2') and status = '0' ${params.dataScope} and del_flag = '0'")
    public long notActiveDeviceCount(TDevice device);

    /**
     * 统计未分配激活设备数量
     * @return
     * @param device
     */
    @Select("select count(*) from t_device d where status in ('0','1','2') and distribute_flag = '0' ${params.dataScope} and del_flag = '0'")
    public long notAssociateDeviceCount(TDevice device);
    /**
     * 统计未分组激活设备数量
     * @return
     * @param device
     */
    @Select("select count(*) from t_device d where status in ('0','1','2') and devicegroup_id is null ${params.dataScope} and del_flag = '0'")
    public long notArrangeDeviceCount(TDevice device);

    /**
     * 根据机构id分组统计设备数
     * @return
     * @param device
     */
    @Select("select org_id as orgId,count(*) as count from t_device d where status in ('0','1','2') ${params.dataScope} and del_flag = '0'" +
            "group by org_id")
    public List<OrgDeviceCountDto> countGroupByOrgId(TDevice device);

    /**
     * 查询设备列表
     *
     * @param userId 用户id
     * @return 设备集合
     */
    public List<TDevice> selectBizCareDeviceList(Long userId);
}
