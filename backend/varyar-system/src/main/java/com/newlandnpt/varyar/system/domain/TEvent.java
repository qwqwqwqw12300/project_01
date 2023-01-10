package com.newlandnpt.varyar.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;

/**
 * 事件对象 t_event
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public class TEvent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 事件id */
    private Long eventId;

    /** 事件编号 */
    @Excel(name = "事件编号")
    private String no;

    /** 级别 */
    @Excel(name = "级别")
    private String level;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 操作类型（0会员操作 1后台人工） */
    @Excel(name = "操作类型", readConverterExp = "0=会员操作,1=后台人工")
    private String operateType;

    /** 会员id */
    @Excel(name = "会员id")
    private Long memberId;

    /** 会员手机号 */
    @Excel(name = "会员手机号")
    private String memberPhone;

    /** 会员姓名 */
    @Excel(name = "会员姓名")
    private String memberName;

    /** 运营者id */
    @Excel(name = "运营者id")
    private Long userId;

    /** 运营者姓名 */
    @Excel(name = "运营者姓名")
    private String userName;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;

    /** 操作标志（0未处理 1已处理） */
    @Excel(name = "操作标志", readConverterExp = "0=未处理,1=已处理")
    private String operateFlag;

    public void setEventId(Long eventId) 
    {
        this.eventId = eventId;
    }

    public Long getEventId() 
    {
        return eventId;
    }
    public void setNo(String no) 
    {
        this.no = no;
    }

    public String getNo() 
    {
        return no;
    }
    public void setLevel(String level) 
    {
        this.level = level;
    }

    public String getLevel() 
    {
        return level;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setOperateType(String operateType) 
    {
        this.operateType = operateType;
    }

    public String getOperateType() 
    {
        return operateType;
    }
    public void setMemberId(Long memberId) 
    {
        this.memberId = memberId;
    }

    public Long getMemberId() 
    {
        return memberId;
    }
    public void setMemberPhone(String memberPhone) 
    {
        this.memberPhone = memberPhone;
    }

    public String getMemberPhone() 
    {
        return memberPhone;
    }
    public void setMemberName(String memberName) 
    {
        this.memberName = memberName;
    }

    public String getMemberName() 
    {
        return memberName;
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
    public void setOperateTime(Date operateTime) 
    {
        this.operateTime = operateTime;
    }

    public Date getOperateTime() 
    {
        return operateTime;
    }
    public void setOperateFlag(String operateFlag) 
    {
        this.operateFlag = operateFlag;
    }

    public String getOperateFlag() 
    {
        return operateFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("eventId", getEventId())
            .append("no", getNo())
            .append("level", getLevel())
            .append("content", getContent())
            .append("operateType", getOperateType())
            .append("memberId", getMemberId())
            .append("memberPhone", getMemberPhone())
            .append("memberName", getMemberName())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("operateTime", getOperateTime())
            .append("operateFlag", getOperateFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
