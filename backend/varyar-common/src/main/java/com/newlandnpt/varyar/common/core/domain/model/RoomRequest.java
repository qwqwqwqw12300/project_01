package com.newlandnpt.varyar.common.core.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;
@ApiModel(description = "家庭房间信息")
public class RoomRequest extends PageRequest {

    /**
     *  家庭Id 唯一标识
     * */
    @ApiModelProperty("家庭Id")
    @Size(min = 0, max = 20, message = "家庭Id 标识不能超过20个字符")
    private String familyId;

    /**
     * 房间名称
    * */
    @ApiModelProperty("房间名称")
    @Size(min = 0, max = 50, message = "房间名称不能超过50个字符")
    private String name;
    /**
     *  房间Id 唯一标识
     * */
    @ApiModelProperty("房间Id")
    @Size(min = 0, max = 20, message = "房间Id标识不能超过20个字符")
    private String roomId;

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
