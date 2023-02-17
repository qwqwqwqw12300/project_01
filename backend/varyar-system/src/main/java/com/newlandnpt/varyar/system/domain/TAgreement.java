package com.newlandnpt.varyar.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;

/**
 * 协议对象 t_agreement
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@ApiModel(description = "协议")
public class TAgreement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("协议id")
    private Long agreementId;

    @ApiModelProperty("协议内容")
    @Excel(name = "协议内容")
    private String content;

    @ApiModelProperty("协议版本")
    @Excel(name = "协议版本")
    private String ver;

    @ApiModelProperty("协议类型（0隐私协议 1app协议）")
    @Excel(name = "协议类型", readConverterExp = "0=隐私协议,1=app协议")
    private String type;

    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;

    public void setAgreementId(Long agreementId) 
    {
        this.agreementId = agreementId;
    }

    public Long getAgreementId() 
    {
        return agreementId;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setVer(String ver) 
    {
        this.ver = ver;
    }

    public String getVer() 
    {
        return ver;
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
            .append("agreementId", getAgreementId())
            .append("content", getContent())
            .append("ver", getVer())
            .append("type", getType())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
