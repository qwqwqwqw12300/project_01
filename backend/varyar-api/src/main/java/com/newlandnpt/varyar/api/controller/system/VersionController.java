package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.system.domain.TVersion;
import com.newlandnpt.varyar.system.service.IVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 版本查询Controller
 * 
 * @author chenxw
 * @date 2023-01-05
 */
@RestController
@RequestMapping("/api/version")
public class VersionController extends BaseController
{
    @Autowired
    private IVersionService versionService;

    /**
     * 获取版本详细信息
     */
    @GetMapping(value = "/selectVersionInfo")
    public AjaxResult selectVersionInfo(String versionType)
    {
        TVersion tVersion = versionService.selectTVersionByVersionType(versionType);
        if (tVersion == null){
            error("版本信息为空！");
        }
        return success(tVersion);
    }
}
