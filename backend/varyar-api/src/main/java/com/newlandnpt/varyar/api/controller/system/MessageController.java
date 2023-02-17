package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.R;
import com.newlandnpt.varyar.common.core.domain.entity.BatchMessage;
import com.newlandnpt.varyar.common.core.domain.entity.MemberInfo;
import com.newlandnpt.varyar.common.core.domain.entity.MemberParameter;
import com.newlandnpt.varyar.common.core.domain.model.BatchMessageRequest;
import com.newlandnpt.varyar.common.core.domain.model.MessagePushRequest;
import com.newlandnpt.varyar.common.core.domain.model.MessageQueryRequest;
import com.newlandnpt.varyar.common.core.domain.model.MessageRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.domain.TMember;
import com.newlandnpt.varyar.system.domain.TMemberContacts;
import com.newlandnpt.varyar.system.domain.TMsg;
import com.newlandnpt.varyar.system.service.*;
import io.swagger.annotations.*;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author newlandnpt
 */
@Api(tags = "消息推送")
@RestController
@RequestMapping("/api/message")
public class MessageController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    private IMsgService itMsgService;
    @Autowired
    private IDeviceService iDeviceService;
    @Autowired
    private IFamilyService iFamilyService;
    @Autowired
    private IRoomService iRoomService;
    @Autowired
    private IMemberService iMemberService;
    @Autowired
    private IMemberContactsService iMemberContactsService;
    /**
    * 获取消息列表
    * */
    @ApiOperation("查看消息列表")
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
    @ApiOperation("查询家庭消息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "readFlag", value = "未读标识 0:未读 1:已读", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "familyId", value = "家庭id",  dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "deviceType", value = "设备类型 （0雷达波 1监控设备）",  dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "eventlevel", value = "事件类型 （0:重要事件  1：普通事件）",  dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "deviceId", value = "设备id",  dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "startDate", value = "开始时间",  dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "endDate", value = "结束时间",  dataType = "String", dataTypeClass = String.class)
    })
    @PostMapping("/familyList")
    public TableDataInfo familyList(@RequestBody @Validated MessageQueryRequest messageRequest) {
        startPage(10000);
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
        Long memberId = getLoginUser().getMemberId();
        tMsg.setMemberId(memberId);
        List<TMsg> list = itMsgService.selectTMsgList(tMsg);
        return getDataTable(list);
    }
    /**
     * 获取消息详情
     * */
    @ApiOperation("查看消息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "msgId", value = "消息id", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "msgFlag", value = "消息标识 0:未处理 1:已处理",  dataType = "String", dataTypeClass = String.class)
    })
    @PostMapping("/msgContent")
    public TableDataInfo content(@RequestBody @Validated MessageRequest messageRequest) {
        TMsg msg = itMsgService.selectTMsgByMsgId(Long.valueOf(messageRequest.getMsgId()));
        TDevice device = new TDevice();
        if(msg.getDeviceId()!=null||!msg.getDeviceId().equals("")){
            device = iDeviceService.selectDeviceByDeviceId(msg.getDeviceId());
        }
        if(device.getFamilyId()!=null||!device.getFamilyId().equals("")){
            msg.setFamilyName(iFamilyService.selectTFamilyByFamilyId(device.getFamilyId()).getName());
        }
        if(device.getRoomId()!=null||!device.getRoomId().equals("")){
            msg.setRoomName(iRoomService.selectTRoomByRoomId(device.getRoomId()).getName());
        }
        List<TMsg> list = new ArrayList<TMsg>();
        list.add(msg);
        return getDataTable(list);
    }
    /**
     * 标记消息状态
     * */
    @ApiOperation("查看消息状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "msgId", value = "消息id", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "msgFlag", value = "消息标识 0:未处理 1:已处理",  dataType = "String", dataTypeClass = String.class)
    })
    @PostMapping("/setMsgInfo")
    public AjaxResult setMessageInfo(@RequestBody @Validated MessageRequest messageRequest) {
        AjaxResult ajax = AjaxResult.success();
        if (messageRequest.getMsgFlag()==null || messageRequest.getMsgFlag().equals("")){
            error("消息标识不能为空！");
        }
        TMsg msg = itMsgService.selectTMsgByMsgId(Long.valueOf(messageRequest.getMsgId()));
        msg.setOperateFlag(messageRequest.getMsgFlag());
        try {
            itMsgService.updateTMsg(msg);
        }  catch (Exception e){
            return error("标记消息状态异常！");
        }
        return ajax;
    }
    /**
     * 标记消息状态 (批量)
     * */
    @ApiOperation("标志所有消息状态")
    @PostMapping("/setBatchMsgInfo")
    public AjaxResult setBatchMessageInfo(@RequestBody @Validated BatchMessageRequest messageRequest) {
        AjaxResult ajax = AjaxResult.success();
        if (CollectionUtils.isEmpty(messageRequest.getMsgFlags())){
            error("批量消息标识不能为空！");
        }
        List<TMsg> tMsgs = new ArrayList<TMsg>();
        for (BatchMessage item : messageRequest.getMsgFlags()){
            TMsg msg = itMsgService.selectTMsgByMsgId(Long.valueOf(item.getMsgId()));
            msg.setOperateFlag(item.getMsgFlag());
            tMsgs.add(msg);
        }
        try {
            itMsgService.updateTMsgs(tMsgs);
        }  catch (Exception e){
            error("批量标记消息状态失败！");
        }
        return ajax;
    }
    /**
     * 我的-消息设置	推送开关·
     */
    @ApiOperation("我的-消息设置:推送开关设置")
    @ApiImplicitParam(name = "flag", value = "推送标识1: 推送  0:不推送",required = true,  dataType = "String", dataTypeClass = String.class)
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
    @ApiOperation("设备消息未读数量")
    @ApiImplicitParam(name = "deviceId", value = "设备id",required = true,  dataType = "Long", dataTypeClass = Long.class)
    @GetMapping("/getDMsgUnReadNum")
    public String  getDeviceMsgUnRead(Long deviceId){
        TMsg tMsg = new TMsg();
        tMsg.setDeviceId(deviceId);
        tMsg.setOperateFlag("0");
        List<TMsg> list = itMsgService.selectTMsgList(tMsg);
        return String.valueOf(list.size());
    }


    @ApiOperation("获取消息对应的联系人信息")
    @ApiImplicitParam(name = "msgId", value = "消息id", required = true, dataType = "long", paramType = "query", dataTypeClass = Long.class)
    @ApiResponses(@ApiResponse(code = 200, message = "",response = TMemberContacts.class))
    @GetMapping("/getMessageContract")
    public AjaxResult getMessageContract(Long msgId){
        return AjaxResult.success(iMemberContactsService.selectMemberContactsListByMsgId(msgId));
    }
}
