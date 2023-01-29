package com.newlandnpt.varyar.common.core.domain.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DeviceParameter implements Serializable
{
    private static final long serialVersionUID = 1L;

    public List<DevicePhone> list;
    /**
     * Map Key值（P：普通电话  0：sos电话  1：按钮1  2：按钮2  3：按钮3  4：按钮4)
     * */
    public Map<String ,DevicePhone> mapSet;

    public Map<String, DevicePhone> getMapSet() {
        return mapSet;
    }

    public void setMapSet(Map<String, DevicePhone> mapSet) {
        this.mapSet = mapSet;
    }

    public List<DevicePhone> getList() {
        return list;
    }

    public void setList(List<DevicePhone> list) {
        this.list = list;
    }
}
