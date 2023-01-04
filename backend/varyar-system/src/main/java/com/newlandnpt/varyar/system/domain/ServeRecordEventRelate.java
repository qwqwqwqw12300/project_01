package com.newlandnpt.varyar.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;

/**
 * 服务记录和事件关联对象 t_serve_record_event_relate
 * 
 * @author lin.ju
 * @date 2023-01-04
 */
public class ServeRecordEventRelate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 服务记录id */
    @Excel(name = "服务记录id")
    private Long recordId;

    /** 事件id */
    @Excel(name = "事件id")
    private Long eventId;

    public void setRecordId(Long recordId) 
    {
        this.recordId = recordId;
    }

    public Long getRecordId() 
    {
        return recordId;
    }
    public void setEventId(Long eventId) 
    {
        this.eventId = eventId;
    }

    public Long getEventId() 
    {
        return eventId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("recordId", getRecordId())
            .append("eventId", getEventId())
            .toString();
    }
}
