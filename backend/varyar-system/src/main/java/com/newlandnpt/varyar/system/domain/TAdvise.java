package com.newlandnpt.varyar.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;

/**
 * 建议对象 t_advise
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public class TAdvise extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 建议id */
    private Long adviseId;

    /** 建议内容 */
    @Excel(name = "建议内容")
    private String content;

    /** 会员id */
    @Excel(name = "会员id")
    private Long memberId;

    /** 已读标志（0未读 1已读） */
    @Excel(name = "已读标志", readConverterExp = "0=未读,1=已读")
    private String readFlag;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setAdviseId(Long adviseId) 
    {
        this.adviseId = adviseId;
    }

    public Long getAdviseId() 
    {
        return adviseId;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
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
            .append("adviseId", getAdviseId())
            .append("content", getContent())
            .append("memberId", getMemberId())
            .append("readFlag", getReadFlag())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
