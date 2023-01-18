package com.newlandnpt.varyar.system.mapper;

import com.newlandnpt.varyar.system.domain.vo.StateVo;

import java.util.Date;
import java.util.List;

/**
 * 查询设备状态
 * @author lisd
 * @date 2023/1/18
 **/
public interface StateMapper {

    /**
     * 查询房间
     *
     * @param beginTime 查询开始时间
     * @return 房间
     */
    public List<StateVo> selectByTime(Date beginTime);

}
