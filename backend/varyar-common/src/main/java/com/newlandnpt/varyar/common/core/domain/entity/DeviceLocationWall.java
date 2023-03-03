package com.newlandnpt.varyar.common.core.domain.entity;

import java.math.BigDecimal;

public class DeviceLocationWall {

    /** 长度 */
    private BigDecimal roomLength =new BigDecimal(4);

    /** 左侧 */
    private BigDecimal roomLeft=new BigDecimal(2);

    /** 右侧 */
    private BigDecimal roomRight=new BigDecimal(2);

    /** 高度  固定1.5米 */
    private BigDecimal roomHeight =new BigDecimal(1.5);




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
