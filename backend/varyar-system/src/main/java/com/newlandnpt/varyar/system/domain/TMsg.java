package com.newlandnpt.varyar.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;

/**
 * 消息对象 t_msg
 *
 * @author chenxw
 * @date 2023-01-05
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

    /** 设备id */
    @Excel(name = "设备id")
    private Long deviceId;

    /** 家庭id */
    @Excel(name = "家庭id")
    private Long familyId;

    /** 处理人会员id */
    @Excel(name = "处理人会员id")
    private Long memberId;

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
    public void setDeviceId(Long deviceId)
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId()
    {
        return deviceId;
    }
    public void setFamilyId(Long familyId)
    {
        this.familyId = familyId;
    }

    public Long getFamilyId()
    {
        return familyId;
    }
    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    public Long getMemberId()
    {
        return memberId;
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
                .append("deviceId", getDeviceId())
                .append("familyId", getFamilyId())
                .append("memberId", getMemberId())
                .append("operateFlag", getOperateFlag())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
