package com.newlandnpt.varyar.common.core.domain.model;

import javax.validation.constraints.Size;

public class DeviceRequest {
    /**
     *  家庭Id 唯一标识
     * */
    @Size(min = 0, max = 20, message = "唯一标识不能超过20个字符")
    private String familyId;

    /**
     *  房间Id 唯一标识
     * */
    @Size(min = 0, max = 20, message = "房间Id标识不能超过20个字符")
    private String roomId;

    /**
     *  设备名称
     * */
    @Size(min = 0, max = 20, message = "房间Id标识不能超过20个字符")
    private String deviceName;

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
