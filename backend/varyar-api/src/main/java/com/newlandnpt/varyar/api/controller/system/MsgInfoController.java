package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.AdviseRequest;
import com.newlandnpt.varyar.common.core.domain.model.LoginUser;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.system.domain.TFamily;
import com.newlandnpt.varyar.system.domain.TMsg;
import com.newlandnpt.varyar.system.service.IAdviseService;
import com.newlandnpt.varyar.system.service.IFamilyService;
import com.newlandnpt.varyar.system.service.ISysDictTypeService;
import com.newlandnpt.varyar.system.service.IMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 详情页-查询消息Controller
 * 
 * @author chenxw
 * @date 2023-01-05
 */
@RestController
@RequestMapping("/api/msgInfo")
public class MsgInfoController extends BaseController
{
    @Autowired
    private IFamilyService tFamilyService;

    @Autowired
    private IMsgService msgService;

    /**
     * 查询消息列表
     */
    @GetMapping("/list")
    public AjaxResult list(TMsg tMsg)
    {

        List<TMsg> list = msgService.selectTMsgList(tMsg);

        return success(list);
    }


    /**
     * 查询消息
     */
    @PostMapping("/selectMsg")
    public AjaxResult selectMsgInfo(@RequestBody AdviseRequest advise)
    {
        return success("查询消息成功");
    }

    /**
     * 根据会员id找家庭
     */
    public List<TFamily> selectFamily()
    {
        //获取当前用户的id
        LoginUser loginUser = getLoginUser();
        Long memberId = loginUser.getMemberId();
        List<TFamily> data = tFamilyService.selectMembersFamilyList(memberId);
        if (StringUtils.isNull(data))
        {
            data = new ArrayList<TFamily>();
        }
         return data;
    }

    /**
     * 查询消息
     */
    @PostMapping("/selectMsgCountByFlag")
    public AjaxResult selectMsgCountByFlag(@RequestParam("operateFlag") String operateFlag)
    {
        AjaxResult ajax = AjaxResult.success();
        int count = msgService.selectMsgCountByFlag(operateFlag);
        ajax.put("count",count);
        return ajax;
    }

    /**
     * 查询消息
     */
    @PostMapping("/selectMsgCount")
    public AjaxResult selectMsgCount(@RequestParam("operateFlag") String operateFlag)
    {
        AjaxResult ajax = AjaxResult.success();
        int count = msgService.selectMsgCount(operateFlag);
        ajax.put("count",count);

        return ajax;
    }


}
