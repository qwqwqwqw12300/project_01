package com.newlandnpt.varyar.system.service;

import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.system.domain.LocationGuard;
import com.newlandnpt.varyar.system.domain.req.DelLocationGuardReq;

/**
 * 设置位置守护服务
 * @author ljx
 * @date 2023/3/2
 */
public interface ILocationGuardService {


    /**
     * 新建位置守护
     * @param locationGuard
     * @return
     */
    LocationGuard insertLocationGuard(LocationGuard locationGuard);

    /**
     * 更新位置守护
     * @param locationGuard
     * @return
     */
    Integer updateLocationGuard(LocationGuard locationGuard);


}
