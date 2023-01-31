package com.newlandnpt.varyar.common.core.domain.entity;

import java.math.BigDecimal;

public class DeviceLocation {

    /** 长度 */
    private BigDecimal roomLength;

    /** 左侧 */
    private BigDecimal roomLeft;

    /** 右侧 */
    private BigDecimal roomRight;

    /** 高度 */
    private BigDecimal roomHeight;

    public BigDecimal getRoomLength() {
        return roomLength;
    }

    public void setRoomLength(BigDecimal roomLength) {
        this.roomLength = roomLength;
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
