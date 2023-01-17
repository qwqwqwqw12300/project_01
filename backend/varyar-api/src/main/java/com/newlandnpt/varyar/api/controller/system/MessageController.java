package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.entity.BatchMessage;
import com.newlandnpt.varyar.common.core.domain.entity.MemberInfo;
import com.newlandnpt.varyar.common.core.domain.entity.MemberParameter;
import com.newlandnpt.varyar.common.core.domain.model.BatchMessageRequest;
import com.newlandnpt.varyar.common.core.domain.model.MessagePushRequest;
import com.newlandnpt.varyar.common.core.domain.model.MessageQueryRequest;
import com.newlandnpt.varyar.common.core.domain.model.MessageRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.domain.TMsg;
import com.newlandnpt.varyar.system.service.IMemberService;
import com.newlandnpt.varyar.system.service.IMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
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
    @PostMapping("/familyList")
    public TableDataInfo familyList(@RequestBody @Validated MessageQueryRequest messageRequest) {
        startPage();
        TMsg tMsg = new TMsg();
        if(messageRequest.getDeviceId()!=null&&!messageRequest.getDeviceId().equals("")){
            tMsg.setDeviceId(Long.valueOf(messageRequest.getDeviceId()));
        }
        if(messageRequest.getFamilyId()!=null&&!messageRequest.getFamilyId().equals("")){
            tMsg.setFamilyId(Long.valueOf(messageRequest.getFamilyId()));
        }
        if(messageRequest.getDeviceType()!=null&&!messageRequest.getDeviceType().equals("")){
            tMsg.setDeviceType(messageRequest.getDeviceType());
        }
        tMsg.setEventLevel(messageRequest.getEventlevel());
        tMsg.setOperateFlag(messageRequest.getReadFlag());
        tMsg.setStartDate(messageRequest.getStartDate());
        tMsg.setEndDate(messageRequest.getEndDate());
        List<TMsg> list = itMsgService.selectTMsgList(tMsg);
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
            error("消息标识不能为空！");
        }
        TMsg msg = itMsgService.selectTMsgByMsgId(Long.valueOf(messageRequest.getMsgId()));
        msg.setOperateFlag(messageRequest.getMsgFlag());
        try {
            itMsgService.updateTMsg(msg);
        }  catch (Exception e){
            error("设置推送消息开关异常！");
        }
        return ajax;
    }
    /**
     * 标记消息状态 (批量)
     * */
    @PostMapping("/setBatchMsgInfo")
    public AjaxResult setBatchMessageInfo(@RequestBody @Validated BatchMessageRequest messageRequest) {
        AjaxResult ajax = AjaxResult.success();
        if (CollectionUtils.isEmpty(messageRequest.getMsgFlags())){
            error("批量消息标识不能为空！");
        }
        for (BatchMessage item : messageRequest.getMsgFlags()){
            TMsg msg = itMsgService.selectTMsgByMsgId(Long.valueOf(item.getMsgId()));
            msg.setOperateFlag(item.getMsgFlag());
            try {
                itMsgService.updateTMsg(msg);
            }  catch (Exception e){
                error("设置推送消息开关异常！");
            }
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
            error("查询会员信息失败！");
        }
        MemberParameter parameter = new MemberParameter();
        parameter.setPushMessage(messagePushRequest.getFlag());
        member.setParameter(parameter);
        try {
            iMemberService.updateMember(member);
        }  catch (Exception e){
            error("设置推送消息开关失败！");
        }
        return ajax;
    }

    /**
     * 设备消息未读数量
     */
    @GetMapping("/getDMsgUnReadNum")
    public String  getDeviceMsgUnRead(Long deviceId){
        TMsg tMsg = new TMsg();
        tMsg.setDeviceId(deviceId);
        tMsg.setOperateFlag("0");
        List<TMsg> list = itMsgService.selectTMsgList(tMsg);
        return String.valueOf(list.size());
    }
}
