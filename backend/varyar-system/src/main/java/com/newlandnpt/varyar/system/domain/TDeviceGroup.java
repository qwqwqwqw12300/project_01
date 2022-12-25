package com.newlandnpt.varyar.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;

/**
 * 设备组对象 t_devicegroup
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public class TDeviceGroup extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备组id */
    private Long deviceGroupId;

    /** 机构id */
    private Long orgId;

    /** 设备组名称 */
    private String name;

    /** 设备组编号 */
    private String no;

    /** 运营者id */
    private Long userId;

    /** 运营者姓名 */
    private String userName;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setDeviceGroupId(Long deviceGroupId)
    {
        this.deviceGroupId = deviceGroupId;
    }

    public Long getDeviceGroupId()
    {
        return deviceGroupId;
    }
    public void setOrgId(Long orgId) 
    {
        this.orgId = orgId;
    }

    public Long getOrgId() 
    {
        return orgId;
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
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("devicegroupId", getDeviceGroupId())
            .append("orgId", getOrgId())
            .append("name", getName())
            .append("no", getNo())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
