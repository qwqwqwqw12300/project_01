package com.newlandnpt.varyar.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;

/**
 * 版本对象 t_version
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public class TVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 版本id */
    private Long versionId;

    /** 版本号 */
    @Excel(name = "版本号")
    private String content;

    /** 类型（0android 1ios） */
    @Excel(name = "类型", readConverterExp = "0=android,1=ios")
    private String type;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setVersionId(Long versionId) 
    {
        this.versionId = versionId;
    }

    public Long getVersionId() 
    {
        return versionId;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
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
            .append("versionId", getVersionId())
            .append("content", getContent())
            .append("type", getType())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
