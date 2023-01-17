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

    /**
     * 电话类型（P：普通电话  0：sos电话  1：按钮1  2：按钮2  3：按钮3  4：按钮4)
     * */
    public String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
