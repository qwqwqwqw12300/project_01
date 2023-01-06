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
import java.util.List;

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
    private IAdviseService adviseService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IFamilyService tFamilyService;

    @Autowired
    private ISysDictTypeService dictTypeService;

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
     * 下拉选项框接口,根据会员id找家庭
     */
    @GetMapping("/selectFamily")
    public AjaxResult selectFamily()
    {
        //获取当前用户的id
        LoginUser loginUser = getLoginUser();
        Long memberId = loginUser.getMemberId();
        List<TFamily> data = tFamilyService.selectMembersFamilyList(memberId);
        if (StringUtils.isNull(data))
        {
            data = new ArrayList<TFamily>();
        }
         return success(data);
    }

}
