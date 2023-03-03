package com.newlandnpt.varyar.common.core.domain.entity;

import java.math.BigDecimal;

public class DeviceLocationTop {


    /** 前距离 */
    private BigDecimal roomFront = new BigDecimal(2);

    /** 后距离 */
    private BigDecimal roomBehind = new BigDecimal(2);

    /** 左侧 */
    private BigDecimal roomLeft = new BigDecimal(2);

    /** 右侧 */
    private BigDecimal roomRight = new BigDecimal(2);

    /** 高度 */
    private BigDecimal roomHeight = new BigDecimal(4);

    public BigDecimal getRoomFront() {
        return roomFront;
    }

    public void setRoomFront(BigDecimal roomFront) {
        this.roomFront = roomFront;
    }


    public BigDecimal getRoomBehind() {
        return roomBehind;
    }

    public void setRoomBehind(BigDecimal roomBehind) {
        this.roomBehind = roomBehind;
    }
    public BigDecimal getRoomLeft() {
        return roomLeft;
    }

    public void setRoomLeft(BigDecimal roomLeft) {
        this.roomLeft = roomLeft;
    }

    public BigDecimal getRoomRight() {
        return roomRight;
    }

    public void setRoomRight(BigDecimal roomRight) {
        this.roomRight = roomRight;
    }

    public BigDecimal getRoomHeight() {
        return roomHeight;
    }

    public void setRoomHeight(BigDecimal roomHeight) {
        this.roomHeight = roomHeight;
    }
}
