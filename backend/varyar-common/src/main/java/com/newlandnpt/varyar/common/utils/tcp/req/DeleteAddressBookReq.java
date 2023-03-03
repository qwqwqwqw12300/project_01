package com.newlandnpt.varyar.common.utils.tcp.req;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 删除通讯录请求
 * @author ljx
 * @Date 2023/3/2
 */
public class DeleteAddressBookReq {
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

    public DeleteAddressBookReq() {
    }

    public DeleteAddressBookReq(String deviceNo, String addressBookId, @Pattern(regexp = "^[1]\\d{10}$", message = "手机号格式错误") @Size(min = 0, max = 11, message = "手机号不能超过11个字符") String phoneNumber) {
        this.deviceNo = deviceNo;
        this.addressBookId = addressBookId;
        this.phoneNumber = phoneNumber;
    }

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
}
