package com.newlandnpt.varyar.system.domain.dto.event;

/**
 * 根据级别聚合统计事件数量
 * @author lin.ju
 * @date 2023/1/5
 */
public class EventCountGroupByLevelDto {

    /**
     * 级别
     */
    private String level;

    /**
     * 数量
     */
    private long count;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
