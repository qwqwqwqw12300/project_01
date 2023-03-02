package com.newlandnpt.varyar.common.core.domain.entity;

import java.io.Serializable;

/**
 * 设备参数-电话
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public class DevicePhone implements Serializable
{
    private static final long serialVersionUID = 1L;
    /**
     * 联系人名称
     * */
    public String phoneName;
    /**
     * 联系人电话
     * */
    public String phone;

    public DevicePhone() {
    }

    public DevicePhone(String phoneName, String phone) {
        this.phoneName = phoneName;
        this.phone = phone;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
