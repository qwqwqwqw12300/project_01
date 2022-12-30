package com.newlandnpt.varyar.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

import java.util.List;

/**
 * 家庭对象 t_family
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public class TFamily extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 家庭id */
    private Long familyId;

    /** 家庭编号 */
    @Excel(name = "家庭编号")
    private String no;

    /** 家庭名称 */
    @Excel(name = "家庭名称")
    private String name;

    /** 家庭地址 */
    @Excel(name = "家庭地址")
    private String address;

    /** 设备列表 */
    @Transient
    private List<Device> devices;

    /** 共享标志（0代表是 2代表非） */
    @Transient
    private String shareFlag;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setFamilyId(Long familyId) 
    {
        this.familyId = familyId;
    }

    public Long getFamilyId() 
    {
        return familyId;
    }
    public void setNo(String no) 
    {
        this.no = no;
    }

    public String getNo() 
    {
        return no;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public String getShareFlag() {
        return shareFlag;
    }

    public void setShareFlag(String shareFlag) {
        this.shareFlag = shareFlag;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("familyId", getFamilyId())
            .append("no", getNo())
            .append("name", getName())
            .append("address", getAddress())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
