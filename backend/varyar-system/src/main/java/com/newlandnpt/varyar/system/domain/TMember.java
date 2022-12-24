package com.newlandnpt.varyar.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;

/**
 * 会员对象 t_member
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public class TMember extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会员id */
    private Long memberId;

    /** 会员编号 */
    @Excel(name = "会员编号")
    private String no;

    /** 会员手机号 */
    @Excel(name = "会员手机号")
    private String phone;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 分配标志（0未分配 1已分配） */
    @Excel(name = "分配标志", readConverterExp = "0=未分配,1=已分配")
    private String distributeFlag;

    /** 运营者id */
    @Excel(name = "运营者id")
    private Long userId;

    /** 运营者姓名 */
    @Excel(name = "运营者姓名")
    private String userName;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setMemberId(Long memberId) 
    {
        this.memberId = memberId;
    }

    public Long getMemberId() 
    {
        return memberId;
    }
    public void setNo(String no) 
    {
        this.no = no;
    }

    public String getNo() 
    {
        return no;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setDistributeFlag(String distributeFlag) 
    {
        this.distributeFlag = distributeFlag;
    }

    public String getDistributeFlag() 
    {
        return distributeFlag;
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
            .append("memberId", getMemberId())
            .append("no", getNo())
            .append("phone", getPhone())
            .append("password", getPassword())
            .append("distributeFlag", getDistributeFlag())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
