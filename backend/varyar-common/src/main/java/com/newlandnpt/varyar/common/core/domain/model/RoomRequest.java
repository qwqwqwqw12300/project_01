package com.newlandnpt.varyar.common.core.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RoomRequest {

    /**
     *  家庭Id 唯一标识
     * */
    @Size(min = 0, max = 20, message = "家庭Id 标识不能超过20个字符")
    private String familyId;

    /**
     * 房间名称
    * */
    @Size(min = 0, max = 50, message = "房间名称不能超过50个字符")
    private String roomName;
    /**
     *  房间Id 唯一标识
     * */
    @Size(min = 0, max = 20, message = "房间Id标识不能超过20个字符")
    private String roomId;

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
