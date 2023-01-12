package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.entity.MemberParameter;
import com.newlandnpt.varyar.common.core.domain.model.MessagePushRequest;
import com.newlandnpt.varyar.common.core.domain.model.MessageRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.domain.TMsg;
import com.newlandnpt.varyar.system.service.IMemberService;
import com.newlandnpt.varyar.system.service.IMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统消息
 *
 * @author newlandnpt
 */
@RestController
@RequestMapping("/api/message")
public class MessageController extends BaseController {

    @Autowired
    private IMsgService itMsgService;

    @Autowired
    private IMemberService iMemberService;
/**
 * 获取消息列表
 * */
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        Long memberId = getLoginUser().getMemberId();
        List<TMsg> list = itMsgService.selectTMsgList(memberId);
        return getDataTable(list);
    }
    /**
     * 获取消息详情
     * */
    @PostMapping("/msgContent")
    public TableDataInfo content(@RequestBody @Validated MessageRequest messageRequest) {
        TMsg msg = itMsgService.selectTMsgByMsgId(Long.valueOf(messageRequest.getMsgId()));
        List<TMsg> list = new ArrayList<TMsg>();
        list.add(msg);
        return getDataTable(list);
    }
    /**
     * 标记消息状态
     * */
    @PostMapping("/setMsgInfo")
    public AjaxResult setMessageInfo(@RequestBody @Validated MessageRequest messageRequest) {
        AjaxResult ajax = AjaxResult.success();
        if (messageRequest.getMsgFlag().equals("")||messageRequest.getMsgFlag()==null){
            ajax = AjaxResult.error("消息标识不能为空！");
            return ajax;
        }
        TMsg msg = itMsgService.selectTMsgByMsgId(Long.valueOf(messageRequest.getMsgId()));
        msg.setOperateFlag(messageRequest.getMsgFlag());
        try {
            itMsgService.updateTMsg(msg);
        }  catch (Exception e){
            ajax = AjaxResult.error("设置推送消息开关异常！");
            return ajax;
        }
        return ajax;
    }
    /**
     * 我的-消息设置	推送开关·
     */
    @PostMapping("/updPushMsg")
    public AjaxResult updatePushMsg(@RequestBody @Validated MessagePushRequest messagePushRequest){
        AjaxResult ajax = AjaxResult.success();
        Long memberId = getLoginUser().getMemberId();
        TMember member = iMemberService.selectMemberByMemberId(memberId);
        if(member == null){
            ajax = AjaxResult.error("查询会员信息失败！");
            return ajax;
        }
        MemberParameter parameter = new MemberParameter();
        parameter.setPushMessage(messagePushRequest.getFlag());
        member.setParameter(parameter);
        try {
            iMemberService.updateMember(member);
        }  catch (Exception e){
            ajax = AjaxResult.error("设置推送消息开关失败！");
            return ajax;
        }
        return ajax;
    }
    /**
     * 我的-消息设置	获取推送开关状态·
     */
    @GetMapping("/getPushMsgState")
    public String  getPushMessageState(){
        Long memberId = getLoginUser().getMemberId();
        TMember member = new TMember();
        try {
            member = iMemberService.selectMemberByMemberId(memberId);
        }  catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
        String flag = "";

        if(member.getParameter() == null || member.getParameter().getPushMessage()==null){
            flag = "0";
            return flag;
        }
        flag = member.getParameter().getPushMessage();
        return flag;
    }

}
