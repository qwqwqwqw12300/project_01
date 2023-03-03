package com.newlandnpt.varyar.common.core.domain.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备呼入号码
 * @author ljx
 * @date 2023/3/1
 */
public class DeviceIncomingCall implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 昵称
     */
    private String phoneName;

    /**
     * 通讯录ID
     */
    private String addressBookId;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 呼入时段
     */
    private ArrayList<String> timePeriods;



    public DeviceIncomingCall() {
    }

    public DeviceIncomingCall(String phoneName, String addressBookId, String phoneNumber, ArrayList<String> timePeriods) {
        this.phoneName = phoneName;
        this.addressBookId = addressBookId;
        this.phoneNumber = phoneNumber;
        this.timePeriods = timePeriods;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<String> getTimePeriods() {
        ArrayList<String> objects = new ArrayList<>();
        objects.add("0000-2359");
        return objects;
    }

    public void setTimePeriods(ArrayList<String> timePeriods) {
        ArrayList<String> objects = new ArrayList<>();
        objects.add("0000-2359");
        this.timePeriods = timePeriods;
    }

    public String getAddressBookId() {
        return addressBookId;
    }

    public void setAddressBookId(String addressBookId) {
        this.addressBookId = addressBookId;
    }
}
