package com.newlandnpt.varyar.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.newlandnpt.varyar.common.annotation.Excels;
import com.newlandnpt.varyar.common.core.domain.entity.SysUser;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;
import org.springframework.data.annotation.Transient;
import org.springframework.security.web.PortResolverImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 服务记录对象 t_serve_record
 * 
 * @author lin.ju
 * @date 2023-01-04
 */
public class ServeRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 服务记录id */
    private Long recordId;

    /** 服务人员id */
    private Long servedUserId;

    /** 服务人员快照 */
    private SysUser servedUserSnapshot;

    /** 服务人员信息 */
    @Excels({
            @Excel(name = "服务人员", targetAttr = "nickName", type = Excel.Type.EXPORT,sort = 2),
            @Excel(name = "服务人员手机号码", targetAttr = "phonenumber", type = Excel.Type.EXPORT,sort = 3),
            @Excel(name = "处理人归属机构", targetAttr = "orgName", type = Excel.Type.EXPORT,sort = 4)
    })
    @Transient
    private SysUserSimple servedUser;

    /** 服务类型（ 0拨打电话 ） */
    @Excel(name = "服务类型", readConverterExp = "0=拨打电话",sort = 0)
    private String servedType;

    /** 服务信息，根据服务类型而定，例：拨打电话记录电话号码 */
    private String servedInfo;

    /** 设备id */
    @NotBlank(message = "设备id不能为空")
    private Long deviceId;

    /**
     * 设备信息
     */
    @Transient
    @Excels({
            @Excel(name = "设备名称", targetAttr = "name", type = Excel.Type.EXPORT,sort = 6),
            @Excel(name = "设备编号", targetAttr = "no", type = Excel.Type.EXPORT,sort = 7),
            @Excel(name = "设备分组+位置", targetAttr = "deviceGroupName+location", separator = " ",type = Excel.Type.EXPORT,sort = 9)
    })
    private Device device;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 创建时间 */
    @Excel(name = "处理时间",sort = 1,dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    @Excel(name = "处理人服务备注",sort = 5)
    private String remark;

    /**
     * 服务事件列表
     */
    @NotEmpty(message = "服务事件列表不能为空")
    @Valid
    @Excel(name = "关联消息编号", targetAttr = "no", type = Excel.Type.EXPORT,sort = 8,useSubClassAnn = false)
    private List<ServeEventSimple> serveEvents;

    public void setRecordId(Long recordId)
    {
        this.recordId = recordId;
    }

    public Long getRecordId() 
    {
        return recordId;
    }
    public void setServedUserId(Long servedUserId) 
    {
        this.servedUserId = servedUserId;
    }

    public Long getServedUserId() 
    {
        return servedUserId;
    }

    public SysUser getServedUserSnapshot() {
        return servedUserSnapshot;
    }

    public void setServedUserSnapshot(SysUser servedUserSnapshot) {
        this.servedUserSnapshot = servedUserSnapshot;
    }

    public void setServedType(String servedType)
    {
        this.servedType = servedType;
    }

    public String getServedType() 
    {
        return servedType;
    }
    public void setServedInfo(String servedInfo) 
    {
        this.servedInfo = servedInfo;
    }

    public String getServedInfo() 
    {
        return servedInfo;
    }
    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public List<ServeEventSimple> getServeEvents() {
        return serveEvents;
    }

    public void setServeEvents(List<ServeEventSimple> serveEvents) {
        this.serveEvents = serveEvents;
    }

    public SysUserSimple getServedUser() {
        return servedUser;
    }

    public void setServedUser(SysUserSimple servedUser) {
        this.servedUser = servedUser;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("recordId", getRecordId())
            .append("servedUserId", getServedUserId())
            .append("servedUserSnapshot", getServedUserSnapshot())
            .append("servedType", getServedType())
            .append("servedInfo", getServedInfo())
            .append("deviceId", getDeviceId())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }

    /**
     * 服务记录对应事件简单信息
     */
    public static class ServeEventSimple {
        /** 事件id */
        @NotBlank(message = "事件id不能为空")
        private Long eventId;

        /** 事件编号 */
        private String no;

        public Long getEventId() {
            return eventId;
        }

        public void setEventId(Long eventId) {
            this.eventId = eventId;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }
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

        /** 用户昵称 */
        private String nickName;

        /** 手机号码 */
        private String phonenumber;
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

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPhonenumber() {
            return phonenumber;
        }

        public void setPhonenumber(String phonenumber) {
            this.phonenumber = phonenumber;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }
    }
}
