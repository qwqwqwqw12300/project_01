package com.newlandnpt.varyar.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 房间对象 t_room
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public class TRoom extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 房间id */
    private Long roomId;

    /** 房间名称 */
    @Excel(name = "房间名称")
    private String name;

    /** 家庭id */
    @Excel(name = "家庭id")
    private Long familyId;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 长度 */
    private BigDecimal roomLength;

    /** 左侧 */
    private BigDecimal roomLeft;

    /** 右侧 */
    private BigDecimal roomRight;

    /** 高度 */
    private BigDecimal roomHeight;

    public void setRoomId(Long roomId) 
    {
        this.roomId = roomId;
    }

    public Long getRoomId() 
    {
        return roomId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setFamilyId(Long familyId) 
    {
        this.familyId = familyId;
    }

    public Long getFamilyId() 
    {
        return familyId;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

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

    @Override
    public String toString() {
        return "TRoom{" +
                "roomId=" + roomId +
                ", name='" + name + '\'' +
                ", familyId=" + familyId +
                ", delFlag='" + delFlag + '\'' +
                ", roomLength=" + roomLength +
                ", roomLeft=" + roomLeft +
                ", roomRight=" + roomRight +
                ", roomHeight=" + roomHeight +
                '}';
    }
}
