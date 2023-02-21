package com.newlandnpt.varyar.common.core.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Size;
import java.util.Date;

public class EventRequest extends PageRequest {
    /**
     *  我的家庭
     * */
    @Size(min = 0, max = 20, message = "唯一标识不能超过20个字符")
    private String familyId;

    /**
     *  开始时间
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     *  结束时间
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     *  设备类型
     * */
    private String type;
    /**
     *  事件类型
     * */
    private String level;
    /**
     *  已读标志
     * */
    private String operateFlag;

    private Long memberId;

    private String memberPhone;

    private String memberName;


    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOperateFlag() {
        return operateFlag;
    }

    public void setOperateFlag(String operateFlag) {
        this.operateFlag = operateFlag;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @Override
    public String toString() {
        return "EventRequest{" +
                "familyId='" + familyId + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", type='" + type + '\'' +
                ", level='" + level + '\'' +
                ", operateFlag='" + operateFlag + '\'' +
                ", memberId='" + memberId + '\'' +
                ", memberPhone='" + memberPhone + '\'' +
                ", memberName='" + memberName + '\'' +
                '}';
    }
}
