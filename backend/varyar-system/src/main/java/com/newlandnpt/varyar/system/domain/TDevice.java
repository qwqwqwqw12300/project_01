package com.newlandnpt.varyar.system.domain;

import java.io.Serializable;
import java.util.*;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.newlandnpt.varyar.common.core.domain.entity.*;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
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
    /**是否有人 0:无人  1:有人 */
    @Transient
    private String hasPerson;

    /** 当前电量**/
    @Transient
    private String currentPower;

    /** 当前步数**/
    @Transient
    private String currentSteps;

    /**gps数据**/
    @Transient
    private String gpsData;

    /**LBS数据**/
    @Transient
    private String lbsData;

    /**Wi-Fi数据**/
    @Transient
    private String wifiData;

    /**设备未读消息数量*/
    @Transient
    private String msgNum;
    /**设备实时位置*/
    @Transient
    private String nowLoacation;
    /**电子围栏设备数据*/
    @Transient
    private TDeviceFence deviceFences;

    public TDeviceFence getDeviceFences() {
        return deviceFences;
    }

    public void setDeviceFences(TDeviceFence deviceFences) {
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

    public String getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(String currentPower) {
        this.currentPower = currentPower;
    }

    public String getCurrentSteps() {
        return currentSteps;
    }

    public void setCurrentSteps(String currentSteps) {
        this.currentSteps = currentSteps;
    }

    public String getGpsData() {
        return gpsData;
    }

    public void setGpsData(String gpsData) {
        this.gpsData = gpsData;
    }

    public String getLbsData() {
        return lbsData;
    }

    public void setLbsData(String lbsData) {
        this.lbsData = lbsData;
    }

    public String getWifiData() {
        return wifiData;
    }

    public void setWifiData(String wifiData) {
        this.wifiData = wifiData;
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

    public String getHasPerson() {
        return hasPerson;
    }

    public void setHasPerson(String hasPerson) {
        this.hasPerson = hasPerson;
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

    @Override
    public String toString() {
        return "TDevice{" +
                "deviceId=" + deviceId +
                ", devicegroupId=" + devicegroupId +
                ", name='" + name + '\'' +
                ", no='" + no + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", parameter=" + parameter +
                ", registerTime=" + registerTime +
                ", location='" + location + '\'' +
                ", deviceGroupName='" + deviceGroupName + '\'' +
                ", memberId=" + memberId +
                ", memberNo='" + memberNo + '\'' +
                ", memberPhone='" + memberPhone + '\'' +
                ", familyId=" + familyId +
                ", familyName='" + familyName + '\'' +
                ", roomId=" + roomId +
                ", roomName='" + roomName + '\'' +
                ", distributeFlag='" + distributeFlag + '\'' +
                ", orgId=" + orgId +
                ", orgName='" + orgName + '\'' +
                ", orgType='" + orgType + '\'' +
                ", orgNo='" + orgNo + '\'' +
                ", delFlag='" + delFlag + '\'' +
                ", onlineFlag='" + onlineFlag + '\'' +
                ", msgNum='" + msgNum + '\'' +
                ", nowLoacation='" + nowLoacation + '\'' +
                ", deviceFences=" + deviceFences +
                '}';
    }

    /**
     * 设备参数设置
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,include = JsonTypeInfo.As.PROPERTY,property = "type",visible = true)
    @JsonSubTypes({
            @JsonSubTypes.Type(value=RadarWaveDeviceSettings.class,name = "0"),
            @JsonSubTypes.Type(value=WatchSettings.class,name = "1")})
    public static class DeviceParameter implements Serializable {
        private static final long serialVersionUID = 1L;
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
    public static class RadarWaveDeviceSettings extends DeviceParameter{

        public RadarWaveDeviceSettings() {
            this.setType(TYPE_READER_WAVE);
        }
        /**
         * 房间子区域
         */
        private List<TRoomZone> roomZones = new ArrayList<>();


        /**
         * 设备安装位置：0:壁挂、1:顶挂
         * */
        private String installPosition = "0";

        /**
         * 设备位置信息
         * */
        @Deprecated
        private DeviceLocation deviceLocation =new DeviceLocation();

        private DeviceLocationTop deviceLocationTop =new DeviceLocationTop();

        private DeviceLocationWall deviceLocationWall =new DeviceLocationWall();

        /**
         * 设备房间设置信息
         */
        @Deprecated
        private DeviceRoomParameter deviceRoomParameter;

        /**
         * 设备预警规则
         */
        private DeviceWarnParameter deviceWarnParameter =new DeviceWarnParameter();


        public DeviceWarnParameter getDeviceWarnParameter() {
            return deviceWarnParameter;
        }

        public void setDeviceWarnParameter(DeviceWarnParameter deviceWarnParameter) {
            this.deviceWarnParameter = deviceWarnParameter;
        }
        @Deprecated
        public DeviceRoomParameter getDeviceRoomParameter() {
            return deviceRoomParameter;
        }
        @Deprecated
        public void setDeviceRoomParameter(DeviceRoomParameter deviceRoomParameter) {
            this.deviceRoomParameter = deviceRoomParameter;
        }
        @Deprecated

        public DeviceLocation getDeviceLocation() {
            return deviceLocation;
        }
        @Deprecated

        public void setDeviceLocation(DeviceLocation deviceLocation) {
            this.deviceLocation = deviceLocation;
        }

        public String getInstallPosition() {
            return installPosition;
        }

        public void setInstallPosition(String installPosition) {
            this.installPosition = installPosition;
        }

        public DeviceLocationTop getDeviceLocationTop() {
            return deviceLocationTop;
        }

        public void setDeviceLocationTop(DeviceLocationTop deviceLocationTop) {
            this.deviceLocationTop = deviceLocationTop;
        }

        public DeviceLocationWall getDeviceLocationWall() {
            return deviceLocationWall;
        }

        public void setDeviceLocationWall(DeviceLocationWall deviceLocationWall) {
            this.deviceLocationWall = deviceLocationWall;
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
    public static class WatchSettings extends DeviceParameter{

        public WatchSettings() {
            setType(TYPE_WATCH);
        }

        /**
         * 电子围栏
         */
        private TDeviceFence fence;

        public List<DevicePhone> list;
        /**
         * Map Key值（P：普通电话  0：sos电话  1：按钮1  2：按钮2  3：按钮3  4：按钮4)
         * */
        public Map<String ,DevicePhone> mapSet;


        /**
         * 通讯录设置
         */
        public List<DeviceIncomingCall> addressBookList;

        /**
         * 呼入限制
         */
        public String incomingCallRestrict = "2";

        /**
         * 位置守护任务
         */
        public List<LocationJob> locationJobs;

        /**
         * 禁用时段
         */
        public Map<Integer,ClassTimePeriod> classTimePeriods;

        public Map<Integer, ClassTimePeriod> getClassTimePeriods() {
            return classTimePeriods;
        }

        public void setClassTimePeriods(Map<Integer, ClassTimePeriod> classTimePeriods) {
            this.classTimePeriods = classTimePeriods;
        }

        public List<LocationJob> getLocationJobs() {
            return locationJobs;
        }

        public void setLocationJobs(List<LocationJob> locationJobs) {
            this.locationJobs = locationJobs;
        }

        public List<DeviceIncomingCall> getAddressBookList() {
            return addressBookList;
        }

        public void setAddressBookList(List<DeviceIncomingCall> addressBookList) {
            this.addressBookList = addressBookList;
        }

        public String getIncomingCallRestrict() {
            return incomingCallRestrict;
        }

        public void setIncomingCallRestrict(String incomingCallRestrict) {
            this.incomingCallRestrict = incomingCallRestrict;
        }

        public List<DevicePhone> getList() {
            return list;
        }

        public void setList(List<DevicePhone> list) {
            this.list = list;
        }

        public Map<String, DevicePhone> getMapSet() {
            return mapSet;
        }

        public void setMapSet(Map<String, DevicePhone> mapSet) {
            this.mapSet = mapSet;
        }

        public TDeviceFence getFence() {
            return fence;
        }

        public void setFence(TDeviceFence fence) {
            this.fence = fence;
        }
    }
}
