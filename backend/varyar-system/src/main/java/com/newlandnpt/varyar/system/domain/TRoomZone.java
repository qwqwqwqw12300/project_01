package com.newlandnpt.varyar.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;

/**
 * 子区域模块生成对象 t_room_zone
 * 
 * @author ruoyi
 * @date 2023-01-05
 */
@ApiModel(description = "子区域模块")
public class TRoomZone extends BaseEntity
{
    public static final String FLAG_YES = "0";

    private static final long serialVersionUID = 1L;

    /** 子区域id */
    @ApiModelProperty("子区域id")
    private Long roomZoneId;

    /** 房间id */
    @ApiModelProperty("房间id")
    @Excel(name = "房间id")
    private Long roomId;

    /** 子区域名称 */
    @ApiModelProperty("子区域名称")
    @Excel(name = "子区域名称")
    private String name;

    /** 最左侧的点 */
    @ApiModelProperty("最左侧的点")
    @Excel(name = "最左侧的点")
    private BigDecimal x1;

    /** 最右侧的点 */
    @ApiModelProperty("最右侧的点")
    @Excel(name = "最右侧的点")
    private BigDecimal x2;


    /** 最近的点 */
    @ApiModelProperty("最近的点")
    @Excel(name = "最近的点")
    private BigDecimal y1;

    /** 最远的点 */
    @ApiModelProperty("最远的点")
    @Excel(name = "最远的点")
    private BigDecimal y2;

    /** 最底部的点 */
    @ApiModelProperty("最底部的点")
    @Excel(name = "最底部的点")
    private BigDecimal z1;

    /** 最顶部的点 */
    @ApiModelProperty("最顶部的点")
    @Excel(name = "最顶部的点")
    private BigDecimal z2;

    /** 存在监测标志（0是 1否） */
    @ApiModelProperty("存在监测标志（0是 1否）")
    @Excel(name = "存在监测标志", readConverterExp = "0=是,1=否")
    private String existFlag;

    /** 跌倒监测标志（0是 1否） */
    @ApiModelProperty("跌倒监测标志（0是 1否）")
    @Excel(name = "跌倒监测标志", readConverterExp = "0=是,1=否")
    private String fallFlag;

    /** 进入时间(s) */
    @ApiModelProperty("进入时间(s)")
    @Excel(name = "进入时间(s)")
    private Long entryTime;

    /** 离开时间(s) */
    @ApiModelProperty("离开时间(s)")
    @Excel(name = "离开时间(s)")
    private Long departureTime;
    /** 进入监控区域超时报警（0是 1否） */
    @ApiModelProperty("进入监控区域超时报警（0是 1否）")

    private String inMonitorFlag;
    /** 离开监控区域超时报警（0是 1否）*/
    @ApiModelProperty("离开监控区域超时报警（0是 1否）")
    private String outMonitorFlag;
    /** 开始时间 */
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /** 结束时间 */
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /** 设备id*/
    @ApiModelProperty("设备id")
    private Long deviceId;
    /** 0:普通区域  1：屏蔽区域  2：床  */
    @ApiModelProperty("0:普通区域  1：屏蔽区域  2：床")
    private String zoneType;

    public String getZoneType() {
        return zoneType;
    }

    public void setZoneType(String zoneType) {
        this.zoneType = zoneType;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getInMonitorFlag() {
        return inMonitorFlag;
    }

    public void setInMonitorFlag(String inMonitorFlag) {
        this.inMonitorFlag = inMonitorFlag;
    }

    public String getOutMonitorFlag() {
        return outMonitorFlag;
    }

    public void setOutMonitorFlag(String outMonitorFlag) {
        this.outMonitorFlag = outMonitorFlag;
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

    public void setRoomZoneId(Long roomZoneId)
    {
        this.roomZoneId = roomZoneId;
    }

    public Long getRoomZoneId() 
    {
        return roomZoneId;
    }
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
    public void setX1(BigDecimal x1) 
    {
        this.x1 = x1;
    }

    public BigDecimal getX1() 
    {
        return x1;
    }
    public void setX2(BigDecimal x2) 
    {
        this.x2 = x2;
    }

    public BigDecimal getX2() 
    {
        return x2;
    }
    public void setY1(BigDecimal y1) 
    {
        this.y1 = y1;
    }

    public BigDecimal getY1() 
    {
        return y1;
    }
    public void setY2(BigDecimal y2) 
    {
        this.y2 = y2;
    }

    public BigDecimal getY2() 
    {
        return y2;
    }
    public void setZ1(BigDecimal z1) 
    {
        this.z1 = z1;
    }

    public BigDecimal getZ1() 
    {
        return z1;
    }
    public void setZ2(BigDecimal z2) 
    {
        this.z2 = z2;
    }

    public BigDecimal getZ2() 
    {
        return z2;
    }
    public void setExistFlag(String existFlag) 
    {
        this.existFlag = existFlag;
    }

    public String getExistFlag() 
    {
        return existFlag;
    }
    public void setFallFlag(String fallFlag) 
    {
        this.fallFlag = fallFlag;
    }

    public String getFallFlag() 
    {
        return fallFlag;
    }
    public void setEntryTime(Long entryTime) 
    {
        this.entryTime = entryTime;
    }

    public Long getEntryTime() 
    {
        return entryTime;
    }
    public void setDepartureTime(Long departureTime) 
    {
        this.departureTime = departureTime;
    }

    public Long getDepartureTime() 
    {
        return departureTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roomZoneId", getRoomZoneId())
            .append("roomId", getRoomId())
            .append("name", getName())
            .append("x1", getX1())
            .append("x2", getX2())
            .append("y1", getY1())
            .append("y2", getY2())
            .append("z1", getZ1())
            .append("z2", getZ2())
            .append("existFlag", getExistFlag())
            .append("fallFlag", getFallFlag())
            .append("entryTime", getEntryTime())
            .append("departureTime", getDepartureTime())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
