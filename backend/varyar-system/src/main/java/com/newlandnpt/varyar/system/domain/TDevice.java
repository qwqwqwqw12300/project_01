package com.newlandnpt.varyar.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

/**
 * 设备对象 t_device
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public class TDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备id */
    private Long deviceId;

    /** 设备组id */
    private Long devicegroupId;

    /** 设备名称 */
    @Excel(name = "设备名称",type = Excel.Type.EXPORT)
    private String name;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String no;

    /** 状态（0未激活 1激活 2下线） */
    private String status;

    /** 类型（0雷达波 1监控设备） */
    @Excel(name = "设备类型",type = Excel.Type.EXPORT,readConverterExp = "0=雷达波,1=监控设备")
    private String type;

    /** 激活时间 */
    private Date registerTime;

    /** 设备位置 */
    @Excel(name = "设备位置",type = Excel.Type.EXPORT)
    private String location;

    /** 设备组名称 */
    @Transient
    @Excel(name = "设备分组",type = Excel.Type.EXPORT)
    private String deviceGroupName;

    /** 家庭id */
    private Long familyId;

    /** 房间id */
    private Long roomId;

    /** 分配标志（0未分配 1已分配） */
    private String distributeFlag;

    /** 机构id */
    private Long orgId;

    /** 机构名称 */
    @Transient
    @Excel(name = "机构名称",type = Excel.Type.EXPORT)
    private String orgName;

    /** 机构名称 */
    @Transient
    @Excel(name = "机构类型",type = Excel.Type.EXPORT,readConverterExp = "0=企业机构,1=个人机构")
    private String orgType;

    /** 机构名称 */
    @Transient
    @Excel(name = "机构编号",type = Excel.Type.EXPORT)
    private String orgNo;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }
    public void setDevicegroupId(Long devicegroupId) 
    {
        this.devicegroupId = devicegroupId;
    }

    public Long getDevicegroupId() 
    {
        return devicegroupId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setNo(String no) 
    {
        this.no = no;
    }

    public String getNo() 
    {
        return no;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setRegisterTime(Date registerTime) 
    {
        this.registerTime = registerTime;
    }

    public Date getRegisterTime() 
    {
        return registerTime;
    }
    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }
    public void setFamilyId(Long familyId) 
    {
        this.familyId = familyId;
    }

    public Long getFamilyId() 
    {
        return familyId;
    }
    public void setRoomId(Long roomId) 
    {
        this.roomId = roomId;
    }

    public Long getRoomId() 
    {
        return roomId;
    }
    public void setDistributeFlag(String distributeFlag) 
    {
        this.distributeFlag = distributeFlag;
    }

    public String getDistributeFlag() 
    {
        return distributeFlag;
    }
    public void setOrgId(Long orgId) 
    {
        this.orgId = orgId;
    }

    public Long getOrgId() 
    {
        return orgId;
    }
    public void setOrgName(String orgName) 
    {
        this.orgName = orgName;
    }

    public String getOrgName() 
    {
        return orgName;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public String getDeviceGroupName() {
        return deviceGroupName;
    }

    public void setDeviceGroupName(String deviceGroupName) {
        this.deviceGroupName = deviceGroupName;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("deviceId", deviceId)
                .append("devicegroupId", devicegroupId)
                .append("name", name)
                .append("no", no)
                .append("status", status)
                .append("type", type)
                .append("registerTime", registerTime)
                .append("location", location)
                .append("deviceGroupName", deviceGroupName)
                .append("familyId", familyId)
                .append("roomId", roomId)
                .append("distributeFlag", distributeFlag)
                .append("orgId", orgId)
                .append("orgName", orgName)
                .append("orgType", orgType)
                .append("orgNo", orgNo)
                .append("delFlag", delFlag)
                .toString();
    }
}
