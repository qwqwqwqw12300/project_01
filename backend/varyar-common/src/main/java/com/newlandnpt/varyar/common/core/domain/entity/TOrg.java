package com.newlandnpt.varyar.common.core.domain.entity;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

/**
 * 机构表 t_org
 * 
 * @author ruoyi
 */
public class TOrg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 机构ID */
    private Long orgId;

    /** 父机构ID */
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 机构名称 */
    private String orgName;

    /** 显示顺序 */
    private Integer orderNum;

    /** 负责人 */
    private String leader;

    /** 联系电话 */
    private String leaderPhone;

    /** 机构类型 */
    private String type;

    /** 机构地址 */
    private String address;

    /** 第一服务电话 */
    private String phone1;

    /** 第二服务电话 */
    private String phone2;

    /** 第三服务电话 */
    private String phone3;

    /** 机构状态:0开通,1关闭 */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 父机构名称 */
    @Transient
    private String parentName;
    
    /** 子机构 */
    @Transient
    private List<TOrg> children = new ArrayList<TOrg>();

    public Long getOrgId()
    {
        return orgId;
    }

    public void setOrgId(Long orgId)
    {
        this.orgId = orgId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public String getAncestors()
    {
        return ancestors;
    }

    public void setAncestors(String ancestors)
    {
        this.ancestors = ancestors;
    }

    @NotBlank(message = "机构名称不能为空")
    @Size(min = 0, max = 30, message = "机构名称长度不能超过30个字符")
    public String getOrgName()
    {
        return orgName;
    }

    public void setOrgName(String orgName)
    {
        this.orgName = orgName;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public String getLeader()
    {
        return leader;
    }

    public void setLeader(String leader)
    {
        this.leader = leader;
    }

    @Size(min = 0, max = 11, message = "联系电话长度不能超过11个字符")
    public String getLeaderPhone()
    {
        return leaderPhone;
    }

    public void setLeaderPhone(String leaderPhone)
    {
        this.leaderPhone = leaderPhone;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public List<TOrg> getChildren()
    {
        return children;
    }

    public void setChildren(List<TOrg> children)
    {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("orgId", orgId)
                .append("parentId", parentId)
                .append("ancestors", ancestors)
                .append("orgName", orgName)
                .append("orderNum", orderNum)
                .append("leader", leader)
                .append("leaderPhone", leaderPhone)
                .append("type", type)
                .append("address", address)
                .append("phone1", phone1)
                .append("phone2", phone2)
                .append("phone3", phone3)
                .append("status", status)
                .append("delFlag", delFlag)
                .append("parentName", parentName)
                .append("children", children)
                .toString();
    }
}
