package com.newlandnpt.varyar.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;

/**
 * 会员联络人对象 t_membercontacts
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public class TMembercontacts extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会员联络人id */
    private Long membercontactsId;

    /** 会员id */
    @Excel(name = "会员id")
    private Long memberId;

    /** 联络人姓名 */
    @Excel(name = "联络人姓名")
    private String name;

    /** 联络人手机号 */
    @Excel(name = "联络人手机号")
    private String phone;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setMembercontactsId(Long membercontactsId) 
    {
        this.membercontactsId = membercontactsId;
    }

    public Long getMembercontactsId() 
    {
        return membercontactsId;
    }
    public void setMemberId(Long memberId) 
    {
        this.memberId = memberId;
    }

    public Long getMemberId() 
    {
        return memberId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
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
            .append("membercontactsId", getMembercontactsId())
            .append("memberId", getMemberId())
            .append("name", getName())
            .append("phone", getPhone())
            .append("orderNum", getOrderNum())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
