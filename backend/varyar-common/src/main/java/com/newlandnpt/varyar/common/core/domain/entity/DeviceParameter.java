package com.newlandnpt.varyar.common.core.domain.entity;

import java.io.Serializable;
import java.util.List;

public class DeviceParameter implements Serializable
{
    private static final long serialVersionUID = 1L;

    public List<DevicePhone> list;

    public List<DevicePhone> getList() {
        return list;
    }

    public void setList(List<DevicePhone> list) {
        this.list = list;
    }
}
