package com.newlandnpt.varyar.common.core.domain.model;

import com.newlandnpt.varyar.common.core.domain.entity.BatchMessage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class MessageQueryRequest {

    /**
     *  未读标识 0:未读 1:已读
     * */
    @Size(min = 0, max = 1, message = "未读标识不能超过1个字符")
    private String readFlag;
    /**
     *  家庭id
     * */
    @NotBlank(message = "家庭id不能为空")
    @Size(min = 0, max = 20, message = "家庭id不能超过20个字符")
    private String familyId;

    /** 设备类型 （0雷达波 1监控设备）*/
    @NotBlank(message = "设备类型不能为空")
    @Size(min = 0, max = 1, message = "设备类型不能超过1个字符")
    private String deviceType;

    /** 事件类型 （0:重要事件  1：普通事件）*/
    @Size(min = 0, max = 50, message = "事件类型不能超过50个字符")
    private String eventlevel;

    /** 设备id */
    @Size(min = 0, max = 20, message = "设备id不能超过20个字符")
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(String readFlag) {
        this.readFlag = readFlag;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getEventlevel() {
        return eventlevel;
    }

    public void setEventlevel(String eventlevel) {
        this.eventlevel = eventlevel;
    }
}
