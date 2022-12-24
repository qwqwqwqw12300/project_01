package com.newlandnpt.varyar.system.domain;

import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 消息对象 t_msg
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public class TMsg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 消息id */
    private Long msgId;

    /** 消息编号 */
    @Excel(name = "消息编号")
    private String no;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 事件id */
    @Excel(name = "事件id")
    private Long eventId;

    /** 已读未读标志（0未处理 1已处理） */
    @Excel(name = "已读未读标志", readConverterExp = "0=未处理,1=已处理")
    private String operateFlag;

    public void setMsgId(Long msgId) 
    {
        this.msgId = msgId;
    }

    public Long getMsgId() 
    {
        return msgId;
    }
    public void setNo(String no) 
    {
        this.no = no;
    }

    public String getNo() 
    {
        return no;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setEventId(Long eventId) 
    {
        this.eventId = eventId;
    }

    public Long getEventId() 
    {
        return eventId;
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
            .append("msgId", getMsgId())
            .append("no", getNo())
            .append("content", getContent())
            .append("eventId", getEventId())
            .append("operateFlag", getOperateFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
