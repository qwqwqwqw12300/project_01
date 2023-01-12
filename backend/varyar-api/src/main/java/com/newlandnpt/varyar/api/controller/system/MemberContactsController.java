package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.MemberContactsRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.system.domain.TMemberContacts;
import com.newlandnpt.varyar.system.service.IMemberContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会员联络人
 *
 * @author newlandnpt
 */
@RestController
@RequestMapping("/api/memberContacts")
public class MemberContactsController extends BaseController {

    @Autowired
    private IMemberContactsService iMemberContactsService;

    /**
     * 获取紧急电话列表
     * */
    @GetMapping("/list")
    public TableDataInfo list(TMemberContacts memberContacts) {
        startPage();
        Long memberId = getLoginUser().getMemberId();
        memberContacts.setMemberId(memberId);
        List<TMemberContacts> list = iMemberContactsService.selectMemberContactsList(memberContacts);
        return getDataTable(list);
    }
    /**
     * 创建紧急电话
     * */
    @PostMapping("/creMemberCon")
    public AjaxResult createMemberContacts(
            @RequestBody @Validated MemberContactsRequest memberContactsRequest) {
        AjaxResult ajax = AjaxResult.success();
        if(checkInfo(memberContactsRequest,ajax)!=null){
            return ajax;
        }
        TMemberContacts memberContacts = new TMemberContacts();
        memberContacts.setMemberId(this.getLoginUser().getMemberId());
        memberContacts.setCreateBy(this.getLoginUser().getUsername());
        memberContacts.setName(memberContactsRequest.getPhoneName());
        memberContacts.setPhone(memberContactsRequest.getPhone());
        memberContacts.setOrderNum(Long.valueOf(memberContactsRequest.getOrderNum()));
        memberContacts.setDelFlag("0");
        try {
            iMemberContactsService.insertMemberContacts(memberContacts);
        } catch (Exception e){
            ajax = AjaxResult.error("创建紧急电话失败！");
            return ajax;
        }
        return ajax;
    }

    /**
     * 修改紧急电话
     * */
    @PostMapping("/editMemberCon")
    public AjaxResult editMemberContacts(
            @RequestBody @Validated MemberContactsRequest memberContactsRequest) {
        AjaxResult ajax = AjaxResult.success();
        if (memberContactsRequest.getMemberContactsId().equals("")|| memberContactsRequest.getMemberContactsId()==null){
            ajax = AjaxResult.error("会员联络人Id不能为空！");
            return ajax;
        }
        TMemberContacts memberContacts = iMemberContactsService.selectMemberContactsByMemberContactsId(Long.valueOf(memberContactsRequest.getMemberContactsId()));
        memberContacts.setName(memberContactsRequest.getPhoneName());
        memberContacts.setPhone(memberContactsRequest.getPhone());
        memberContacts.setOrderNum(Long.valueOf(memberContactsRequest.getOrderNum()));
        try {
            iMemberContactsService.updateMemberContacts(memberContacts);
        } catch (Exception e){
            ajax = AjaxResult.error("修改紧急电话失败！");
            return ajax;
        }
        return ajax;
    }

    /**
     * 删除紧急电话
     * */
    @PostMapping("/remMemberCon")
    public AjaxResult removeMemberContacts(
            @RequestBody @Validated MemberContactsRequest memberContactsRequest) {
        AjaxResult ajax = AjaxResult.success();
        if (memberContactsRequest.getMemberContactsId().equals("")|| memberContactsRequest.getMemberContactsId()==null){
            ajax = AjaxResult.error("会员联络人Id不能为空！");
            return ajax;
        }
        try {
            iMemberContactsService.deleteMemberContactsByMemberContactsId(Long.valueOf(memberContactsRequest.getMemberContactsId()));
        } catch (Exception e){
            ajax = AjaxResult.error("删除紧急电话失败！");
            return ajax;
        }
        return ajax;
    }

    private AjaxResult checkInfo(MemberContactsRequest memberContactsRequest,AjaxResult ajax){
        if (memberContactsRequest.getPhoneName().equals("")|| memberContactsRequest.getPhoneName()==null){
            ajax = AjaxResult.error("联络人姓名不能为空！");
            return ajax;
        }
        if (memberContactsRequest.getPhone().equals("")|| memberContactsRequest.getPhone()==null){
            ajax = AjaxResult.error("联络人手机号不能为空！");
            return ajax;
        }
        if (memberContactsRequest.getOrderNum().equals("")|| memberContactsRequest.getOrderNum()==null){
            ajax = AjaxResult.error("显示顺序不能为空！");
            return ajax;
        }
        return null;
    }

}
