package com.newlandnpt.varyar.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

import java.util.Date;

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

    /** 消息类型:1短信 2APP消息*/
    @Excel(name = "消息类型", readConverterExp = "1=短信,2=APP消息")
    private String msgType;

    /** 设备类型: 0雷达波  1监控设备 */
    //@Excel(name = "设备类型:0雷达波 1监控设备", readConverterExp = "0=雷达波,1=监控设备")
    private String deviceType;

    /** 事件等级:0:重要事件  1：普通事件 */
    //@Excel(name = "事件等级:0:重要事件 1：普通事件", readConverterExp = "0=重要事件,1=普通事件")
    private String eventLevel;

    /** 消息编号 */
    //@Excel(name = "消息编号")
    private String no;

    /** 内容 */
    @Excel(name = "消息内容")
    private String content;

    /** 操作人员 */
    @Excel(name = "操作人员")
    private String operator;

    /** 事件id */
    //@Excel(name = "事件id")
    private Long eventId;

    /** 设备id */
    //@Excel(name = "设备id")
    private Long deviceId;

    /** 家庭id */
    //@Excel(name = "家庭id")
    private Long familyId;

    /** 接收会员id */
    //@Excel(name = "处理人会员id")
    private Long memberId;

    /** 接收人员 */
    @Excel(name = "接收人员")
    private Long memberName;

    /** 机构id */
    //@Excel(name = "机构id")
    private Long orgId;

    /** 发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    /** 发送状态 */
    @Excel(name = "发送状态", readConverterExp = "0=未发送,1=已发送")
    private String sendStatus;

    /** 失败原因 */
    @Excel(name = "失败原因")
    private String reason;

    /** 已读未读标志（0未处理 1已处理） */
    //@Excel(name = "已读未读标志", readConverterExp = "0=未处理,1=已处理")
    private String operateFlag;

    /** 家庭名称*/
    @Transient
    private String familyName;

    /** 房间名称*/
    @Transient
    private String roomName;

    /** 设备名称*/
    @Transient
    private String deviceName;

    /** 开始时间 */
    @Transient
    private Date startDate;

    /** 结束时间 */
    @Transient
    private Date endDate;

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

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

    public Long getMemberName() {
        return memberName;
    }

    public void setMemberName(Long memberName) {
        this.memberName = memberName;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public void setOperateFlag(String operateFlag)
    {
        this.operateFlag = operateFlag;
    }

    public String getOperateFlag()
    {
        return operateFlag;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getEventLevel() {
        return eventLevel;
    }

    public void setEventLevel(String eventLevel) {
        this.eventLevel = eventLevel;
    }

    @Override
    public String toString() {
        return "TMsg{" +
                "msgId=" + msgId +
                ", msgType='" + msgType + '\'' +
                ", no='" + no + '\'' +
                ", content='" + content + '\'' +
                ", eventId=" + eventId +
                ", deviceId=" + deviceId +
                ", familyId=" + familyId +
                ", memberId=" + memberId +
                ", memberName=" + memberName +
                ", orgId=" + orgId + '\'' +
                ", operator='" + operator + '\'' +
                ", sendStatus='" + sendStatus + '\'' +
                ", reason='" + reason + '\'' +
                ", operateFlag='" + operateFlag + '\'' +
                ", sendTime=" + sendTime +
                '}';
    }
}
