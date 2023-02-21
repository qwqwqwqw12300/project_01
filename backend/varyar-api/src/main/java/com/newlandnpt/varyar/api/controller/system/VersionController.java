package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.system.domain.TVersion;
import com.newlandnpt.varyar.system.service.IVersionService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.newlandnpt.varyar.system.service.ISysConfigService;

import java.util.HashMap;
import java.util.Map;

/**
 * 版本查询Controller
 * 
 * @author chenxw
 * @date 2023-01-05
 */
@Api(tags = "APP版本")
@RestController
@RequestMapping("/api/version")
public class VersionController extends BaseController
{
    @Autowired
    private IVersionService versionService;

    @Autowired
    private ISysConfigService configService;

    private static final String IOS_TYPE="1";
    /**
     * 获取版本详细信息
     */
    @ApiOperation("获取APP版本详细信息")
    @ApiImplicitParam(name = "versionType", value = "版本类型:0:android,1:ios",required = true, dataType = "String", dataTypeClass = String.class)
    @ApiResponses({
            @ApiResponse(code=400,message = "请求参数没填好"),
            @ApiResponse(code=404,message = "请求路径找不到"),
            @ApiResponse(code=200,message = "请求成功OK")

    })
    @GetMapping(value = "/selectVersionInfo")
    public AjaxResult selectVersionInfo(String versionType)
    {
        //版本下载地址获取
        String iosAddress = configService.selectConfigByKey("app.ios.download.address");
        String androidAddress = configService.selectConfigByKey("app.android.download.address");
        TVersion tVersion = versionService.selectTVersionByVersionType(versionType);
        if (tVersion == null){
            return error("版本信息为空！");
        }
        //IOS
        if(IOS_TYPE.equals(versionType)){
            tVersion.setDownloadAddress(iosAddress);
        }else{
            tVersion.setDownloadAddress(androidAddress);
        }

        return success(tVersion);
    }
}
