package com.newlandnpt.varyar.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.List;

/**
 * 房间对象 t_room
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public class TRoom extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 房间id */
    private Long roomId;

    /** 房间名称 */
    @Excel(name = "房间名称")
    private String name;

    /** 房间类型 */
    @Excel(name = "房间类型")
    private String roomType;

    /** 家庭id */
    @Excel(name = "家庭id")
    private Long familyId;

    /** 机构id */
    private Long orgId;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 创建者id*/
    private String createById;
    /** 设备列表 */
    @Transient
    private List<TDevice> devices;

    public List<TDevice> getDevices() {
        return devices;
    }

    public void setDevices(List<TDevice> devices) {
        this.devices = devices;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }
    public void setRoomId(Long roomId) 
    {
        this.roomId = roomId;
    }

    public Long getRoomId() 
    {
        return roomId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setFamilyId(Long familyId) 
    {
        this.familyId = familyId;
    }

    public Long getFamilyId() 
    {
        return familyId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "TRoom{" +
                "roomId=" + roomId +
                ", name='" + name + '\'' +
                ", roomType='" + roomType + '\'' +
                ", familyId=" + familyId +
                ", orgId=" + orgId +
                ", delFlag='" + delFlag + '\'' +
                ", createById='" + createById + '\'' +
                '}';
    }
}
