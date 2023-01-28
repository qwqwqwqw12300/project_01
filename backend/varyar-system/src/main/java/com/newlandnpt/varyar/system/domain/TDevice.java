package com.newlandnpt.varyar.system.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.newlandnpt.varyar.common.core.domain.entity.DeviceParameter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.newlandnpt.varyar.common.constant.DeviceConstants.TYPE_READER_WAVE;
import static com.newlandnpt.varyar.common.constant.DeviceConstants.TYPE_WATCH;

/**
 * 设备对象 t_device
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public class TDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备id */
    private Long deviceId;

    /** 设备组id */
    private Long devicegroupId;

    /** 设备名称 */
    @Excel(name = "设备名称",type = Excel.Type.EXPORT)
    private String name;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String no;

    /** 状态（0未激活 1激活 2下线） */
    private String status;

    /** 类型（0雷达波 1监控设备 ） */
    @Excel(name = "设备类型",readConverterExp = "0=雷达波,1=监控设备",combo={"雷达波","监控设备"})
    private String type;
    /** 设备参数信息 */
    private DeviceParameter parameter;
    /** 激活时间 */
    private Date registerTime;

    /** 设备位置 */
    @Excel(name = "设备位置",type = Excel.Type.EXPORT)
    private String location;

    /** 设备组名称 */
    @Transient
    @Excel(name = "设备分组",type = Excel.Type.EXPORT)
    private String deviceGroupName;

    /** 会员id */
    private Long memberId;

    /** 会员编号 */
    @Transient
    private String memberNo;

    /** 会员手机号 */
    @Transient
    private String memberPhone;

    /** 家庭id */
    private Long familyId;

    /** 家庭名称 */
    @Transient
    private String familyName;

    /** 房间id */
    private Long roomId;

    /** 房间名称 */
    @Transient
    private String roomName;

    /** 分配标志（0未分配 1已分配） */
    private String distributeFlag;

    /** 机构id */
    private Long orgId;

    /** 机构名称 */
    @Transient
    @Excel(name = "机构名称",type = Excel.Type.EXPORT)
    private String orgName;

    /** 机构名称 */
    @Transient
    @Excel(name = "机构类型",type = Excel.Type.EXPORT,readConverterExp = "0=企业机构,1=个人机构")
    private String orgType;

    /** 机构名称 */
    @Transient
    @Excel(name = "机构编号",type = Excel.Type.EXPORT)
    private String orgNo;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;
    /**是否在线 0:不在线  1:在线*/
    @Transient
    private String onlineFlag;
    /**设备未读消息数量*/
    @Transient
    private String msgNum;
    /**设备参数配置*/
    @Transient
    private DeviceSettings settings;
    /**设备实时位置*/
    @Transient
    private String nowLoacation;
    /**电子围栏设备数据*/
    @Transient
    private String deviceFences;

    public String getDeviceFences() {
        return deviceFences;
    }

    public void setDeviceFences(String deviceFences) {
        this.deviceFences = deviceFences;
    }

    public String getNowLoacation() {
        //经 度
        String longitude ="119.42178";
        //纬度
        String latitude ="26.02571";
        //地址
        String address = "马尾区儒江西路新大陆园区";
        Map<String,String > map = new HashMap<String,String>();
        map.put("longitude",longitude);
        map.put("latitude",latitude);
        map.put("address",address);
        return JSON.toJSONString(map);
    }

    public void setNowLoacation(String nowLoacation) {
        this.nowLoacation = nowLoacation;
    }

    public String getMsgNum() {
        return this.msgNum;
    }

    public void setMsgNum(String msgNum) {
        this.msgNum = msgNum;
    }

    public String getOnlineFlag() {
        return onlineFlag;
    }

    public void setOnlineFlag(String onlineFlag) {
        this.onlineFlag = onlineFlag;
    }

    public void setDeviceId(Long deviceId)
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }

    public void setDevicegroupId(Long devicegroupId) 
    {
        this.devicegroupId = devicegroupId;
    }

    public Long getDevicegroupId() 
    {
        return devicegroupId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setNo(String no) 
    {
        this.no = no;
    }

    @NotBlank(message = "设备编号不能为空")
    @Size(min = 0, max = 50, message = "设备编号长度不能超过50个字符")
    public String getNo() 
    {
        return no;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    @NotBlank(message = "设备状态不能为空")
    public String getStatus() 
    {
        return status;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setRegisterTime(Date registerTime) 
    {
        this.registerTime = registerTime;
    }

    public Date getRegisterTime() 
    {
        return registerTime;
    }
    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public void setFamilyId(Long familyId)
    {
        this.familyId = familyId;
    }

    public Long getFamilyId() 
    {
        return familyId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setRoomId(Long roomId)
    {
        this.roomId = roomId;
    }

    public Long getRoomId() 
    {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setDistributeFlag(String distributeFlag)
    {
        this.distributeFlag = distributeFlag;
    }

    @NotBlank(message = "分配标志不能为空")
    public String getDistributeFlag() 
    {
        return distributeFlag;
    }
    public void setOrgId(Long orgId) 
    {
        this.orgId = orgId;
    }

    public Long getOrgId() 
    {
        return orgId;
    }
    public void setOrgName(String orgName) 
    {
        this.orgName = orgName;
    }

    public String getOrgName() 
    {
        return orgName;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public String getDeviceGroupName() {
        return deviceGroupName;
    }

    public void setDeviceGroupName(String deviceGroupName) {
        this.deviceGroupName = deviceGroupName;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public DeviceParameter getParameter() {
        return parameter;
    }

    public void setParameter(DeviceParameter parameter) {
        this.parameter = parameter;
    }

    public DeviceSettings getSettings() {
        return settings;
    }

    public void setSettings(DeviceSettings settings) {
        this.settings = settings;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("deviceId", deviceId)
                .append("devicegroupId", devicegroupId)
                .append("name", name)
                .append("no", no)
                .append("status", status)
                .append("type", type)
                .append("parameter", parameter)
                .append("registerTime", registerTime)
                .append("location", location)
                .append("deviceGroupName", deviceGroupName)
                .append("memberId", memberId)
                .append("memberNo", memberNo)
                .append("familyId", familyId)
                .append("familyName", familyName)
                .append("roomId", roomId)
                .append("roomName", roomName)
                .append("distributeFlag", distributeFlag)
                .append("orgId", orgId)
                .append("orgName", orgName)
                .append("orgType", orgType)
                .append("orgNo", orgNo)
                .append("delFlag", delFlag)
                .append("onlineFlag", onlineFlag)
                .append("msgNum", msgNum)
                .toString();
    }

    /**
     * 设备参数设置
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,include = JsonTypeInfo.As.PROPERTY,property = "type",visible = true)
    @JsonSubTypes({@JsonSubTypes.Type(value=RadarWaveDeviceSettings.class,name = "0"),@JsonSubTypes.Type(value=WatchSettings.class,name = "1")})
    public static class DeviceSettings extends DeviceParameter{
        /** 类型（0雷达波 1监控设备 ） */
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    /**
     * 雷达波设备参数设置
     */
    public static class RadarWaveDeviceSettings extends DeviceSettings{

        public RadarWaveDeviceSettings() {
            this.setType(TYPE_READER_WAVE);
        }

        /**
         * 房间
         */
        private TRoom room;

        /**
         * 房间子区域
         */
        private List<TRoomZone> roomZones;


        public TRoom getRoom() {
            return room;
        }

        public void setRoom(TRoom room) {
            this.room = room;
        }

        public List<TRoomZone> getRoomZones() {
            return roomZones;
        }

        public void setRoomZones(List<TRoomZone> roomZones) {
            this.roomZones = roomZones;
        }
    }

    /**
     * 监护设备
     */
    public static class WatchSettings extends DeviceSettings{

        public WatchSettings() {
            setType(TYPE_WATCH);
        }

        /**
         * 电子围栏
         */
        private TDeviceFence fence;

        public TDeviceFence getFence() {
            return fence;
        }

        public void setFence(TDeviceFence fence) {
            this.fence = fence;
        }
    }
}
