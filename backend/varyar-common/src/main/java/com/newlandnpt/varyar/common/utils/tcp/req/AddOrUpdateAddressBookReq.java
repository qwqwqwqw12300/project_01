package com.newlandnpt.varyar.common.utils.tcp.req;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 新增或修改通讯录请求
 * @author ljx
 * @date 2023/3/2
 */
public class AddOrUpdateAddressBookReq {

    /**
     * 设备No
     */
    private String deviceNo;
    /**
     * 通讯录ID
     */
    private String addressBookId;
    /**
     * 手机号
     */
    @Pattern(regexp = "^[1]\\d{10}$",message = "手机号格式错误")
    @Size(min = 0, max = 11, message = "手机号不能超过11个字符")
    private String phoneNumber;

    /**
     * 手机号拥有者昵称
     */
    @Size(min = 0, max = 16, message = "昵称不能超过16个字符")
    private String phoneName;

    /**
     * 呼入时段
     */
    private List<String> timePeriods = new ArrayList<>();


    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getAddressBookId() {
        return addressBookId;
    }

    public void setAddressBookId(String addressBookId) {
        this.addressBookId = addressBookId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public List<String> getTimePeriods() {
        return timePeriods;
    }

    public void setTimePeriods(List<String> timePeriods) {
        this.timePeriods = timePeriods;
    }

}
