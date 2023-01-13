package com.newlandnpt.varyar.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;

/**
 * 公告对象 t_notice
 * 
 * @author ruoyi
 * @date 2023-01-12
 */
public class TNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 公告id */
    private Long noticeId;

    /** 系统公告id */
    @Excel(name = "系统公告id")
    private Long sysNoticeId;

    /** 会员id */
    @Excel(name = "会员id")
    private Long memberId;

    /** 阅读标志（0未读 1已读） */
    @Excel(name = "阅读标志", readConverterExp = "0=未读,1=已读")
    private String readFlag;

    public void setNoticeId(Long noticeId) 
    {
        this.noticeId = noticeId;
    }

    public Long getNoticeId() 
    {
        return noticeId;
    }
    public void setSysNoticeId(Long sysNoticeId) 
    {
        this.sysNoticeId = sysNoticeId;
    }

    public Long getSysNoticeId() 
    {
        return sysNoticeId;
    }
    public void setMemberId(Long memberId) 
    {
        this.memberId = memberId;
    }

    public Long getMemberId() 
    {
        return memberId;
    }
    public void setReadFlag(String readFlag) 
    {
        this.readFlag = readFlag;
    }

    public String getReadFlag() 
    {
        return readFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("noticeId", getNoticeId())
            .append("sysNoticeId", getSysNoticeId())
            .append("memberId", getMemberId())
            .append("readFlag", getReadFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
