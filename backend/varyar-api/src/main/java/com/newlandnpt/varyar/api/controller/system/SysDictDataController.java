package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.entity.SysDictData;
import com.newlandnpt.varyar.common.core.domain.entity.SysDictType;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.system.service.ISysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字段表
 *
 * @author newlandnpt
 */
@Api("字典表")
@RestController
@RequestMapping("/api/dictData")
public class SysDictDataController  extends BaseController {


    @Autowired
    private ISysDictTypeService dictTypeService;

    /**
     * 根据字典类型查询字典数据信息
     */
    @ApiOperation("获取字典信息")
    @GetMapping(value = "/getDict")
    public AjaxResult dictInfo(){
        List<SysDictType> data = dictTypeService.selectDictTypeAll();
        if (StringUtils.isNull(data)){
            data = new ArrayList<SysDictType>();
        }
        return success(getDataInfo(data));
    }
    private Map<String,Map<String,String>> getDataInfo(List<SysDictType> data){
        Map<String,Map<String,String>> map = new HashMap<String,Map<String,String>>();
        for (SysDictType item : data){
            Map<String,String> dataMap = new HashMap<String,String>();
            for (SysDictData it:dictTypeService.selectDictDataByType(item.getDictType())){
//                dataMap.put(it.getDictLabel(),);
                dataMap.put(it.getDictValue(),it.getDictLabel());

            }
//            map.put(item.getDictName()+":"+item.getDictType(),dataMap);
            map.put(item.getDictType() +":"+item.getDictName(),dataMap);
        }
        return map;
    }
}
