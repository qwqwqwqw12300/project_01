package com.newlandnpt.varyar.system.domain.dto.org;

import java.io.Serializable;

/**
 * 机构设备数dto
 * @author lin.ju
 * @date 2023/1/5
 */
public class OrgDeviceCountDto implements Serializable {

    /**
     * 机构id
     */
    private Long orgId;

    /**
     * 设备数
     */
    private long count;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
