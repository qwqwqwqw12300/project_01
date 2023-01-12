package com.newlandnpt.varyar.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    /** 运营者 */
    private SysUserSimple user;

    // todo 移除
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

    @NotNull(message = "机构id不能为空")
    public Long getOrgId() 
    {
        return orgId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    @NotBlank(message = "设备组名称不能为空")
    @Size(min = 0, max = 50, message = "设备组名称长度不能超过50个字符")
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

    public SysUserSimple getUser() {
        return user;
    }

    public void setUser(SysUserSimple user) {
        this.user = user;
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

    /**
     * 运营人员简介信息
     */
    public static class SysUserSimple{
        /** 用户ID */
        private Long userId;

        /** 机构ID */
        private Long orgId;

        /** 用户账号 */
        private String userName;

        /** 用户姓名 */
        private String name;

        /** 手机号码 */
        private String mobilePhone;
        /** 机构名称**/
        private String orgName;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getOrgId() {
            return orgId;
        }

        public void setOrgId(Long orgId) {
            this.orgId = orgId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }
    }
}
