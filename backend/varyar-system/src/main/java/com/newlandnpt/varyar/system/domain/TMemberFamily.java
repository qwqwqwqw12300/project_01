package com.newlandnpt.varyar.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

/**
 * 会员和家庭关联对象 t_member_family
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public class TMemberFamily extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会员和家庭关联id */
    private Long memberFamilyId;

    /** 会员id */
    @Excel(name = "会员id")
    private Long memberId;

    /** 家庭id */
    @Excel(name = "家庭id")
    private Long familyId;

    /** 创建会员id */
    @Excel(name = "创建会员id")
    private Long createMemberId;

    /** 共享标志（0代表是 2代表非） */
    @Excel(name = "共享标志", readConverterExp = "0=代表是,2=代表非")
    private String shareFlag;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 手机号*/
    private String phone;

    @Transient
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMemberFamilyId(Long memberFamilyId)
    {
        this.memberFamilyId = memberFamilyId;
    }

    public Long getMemberFamilyId() 
    {
        return memberFamilyId;
    }
    public void setMemberId(Long memberId) 
    {
        this.memberId = memberId;
    }

    public Long getMemberId() 
    {
        return memberId;
    }
    public void setFamilyId(Long familyId) 
    {
        this.familyId = familyId;
    }

    public Long getFamilyId() 
    {
        return familyId;
    }
    public void setCreateMemberId(Long createMemberId) 
    {
        this.createMemberId = createMemberId;
    }

    public Long getCreateMemberId() 
    {
        return createMemberId;
    }
    public void setShareFlag(String shareFlag) 
    {
        this.shareFlag = shareFlag;
    }

    public String getShareFlag() 
    {
        return shareFlag;
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
            .append("memberFamilyId", getMemberFamilyId())
            .append("memberId", getMemberId())
            .append("familyId", getFamilyId())
            .append("createMemberId", getCreateMemberId())
            .append("shareFlag", getShareFlag())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("phone", getPhone())
            .toString();
    }
}
