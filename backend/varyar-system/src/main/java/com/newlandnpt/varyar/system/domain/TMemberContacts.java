package com.newlandnpt.varyar.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("会员紧急联系人")
public class TMemberContacts extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("会员联络人id")
    private Long memberContactsId;

    @ApiModelProperty("会员id")
    @Excel(name = "会员id")
    private Long memberId;

    @ApiModelProperty("联络人姓名")
    @Excel(name = "联络人姓名")
    private String name;

    @ApiModelProperty("联络人手机号")
    @Excel(name = "联络人手机号")
    private String phone;

    @ApiModelProperty("显示顺序")
    @Excel(name = "显示顺序")
    private Long orderNum;

    @ApiModelProperty("删除标志（0代表存在 2代表删除")
    private String delFlag;

    public void setMemberContactsId(Long memberContactsId)
    {
        this.memberContactsId = memberContactsId;
    }

    public Long getMemberContactsId()
    {
        return memberContactsId;
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
            .append("memberContactsId", getMemberContactsId())
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
