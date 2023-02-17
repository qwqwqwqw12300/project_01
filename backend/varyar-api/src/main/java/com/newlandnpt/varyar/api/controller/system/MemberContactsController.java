package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.MemberContactsRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.system.domain.TMemberContacts;
import com.newlandnpt.varyar.system.service.IMemberContactsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会员联络人
 *
 * @author newlandnpt
 */
@Api(tags = "紧急联系人")
@RestController
@RequestMapping("/api/memberContacts")
public class MemberContactsController extends BaseController {

    @Autowired
    private IMemberContactsService iMemberContactsService;

    @ApiOperation("获取紧急电话列表")
    @GetMapping("/list")
    public TableDataInfo list(TMemberContacts memberContacts) {
        startPage();
        Long memberId = getLoginUser().getMemberId();
        memberContacts.setMemberId(memberId);
        List<TMemberContacts> list = iMemberContactsService.selectMemberContactsList(memberContacts);
        return getDataTable(list);
    }

    @ApiOperation("创建紧急电话")
    @PostMapping("/creMemberCon")
    public AjaxResult createMemberContacts(
            @RequestBody @Validated MemberContactsRequest memberContactsRequest) {
        AjaxResult ajax = AjaxResult.success();
        if(checkInfo(memberContactsRequest,ajax)!=null){
            return ajax;
        }
        TMemberContacts cond = new TMemberContacts();
        cond.setOrderNum(Long.valueOf(memberContactsRequest.getOrderNum()));
        cond.setMemberId(this.getLoginUser().getMemberId());
        List<TMemberContacts>  tMemberContacts = iMemberContactsService.selectMemberContactsList(cond);
        if (tMemberContacts.size()>0){
            String oNum = convert(Integer.valueOf(memberContactsRequest.getOrderNum()));
            switch (Integer.valueOf(memberContactsRequest.getOrderNum())){
                case 1 :return error("第"+oNum+"紧急联系人已存在！");
                case 2 :return error("第"+oNum+"紧急联系人已存在！");
                case 3 :return error("第"+oNum+"紧急联系人已存在！");
                default:return error("紧急联系人已存在！");
            }
        }
        TMemberContacts memberContacts = new TMemberContacts();
        memberContacts.setMemberId(this.getLoginUser().getMemberId());
        memberContacts.setCreateBy(this.getLoginUser().getUsername());
        memberContacts.setName(memberContactsRequest.getName());
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


    @ApiOperation("设置紧急电话")
    @PostMapping("/setMemberCon")
    public AjaxResult batchCreateMemberContacts(@RequestBody @Validated List<MemberContactsRequest> memberContactsList){

        iMemberContactsService.setMemberContacts(this.getLoginUser().getMemberId(),memberContactsList);
        return AjaxResult.success();

    }


    @ApiOperation("修改紧急电话")
    @PostMapping("/editMemberCon")
    public AjaxResult editMemberContacts(
            @RequestBody @Validated MemberContactsRequest memberContactsRequest) {
        AjaxResult ajax = AjaxResult.success();
        if (memberContactsRequest.getMemberContactsId()==null||memberContactsRequest.getMemberContactsId().equals("")){
            ajax = AjaxResult.error("会员联络人Id不能为空！");
            return ajax;
        }
        TMemberContacts memberContacts = iMemberContactsService.selectMemberContactsByMemberContactsId(Long.valueOf(memberContactsRequest.getMemberContactsId()));
        memberContacts.setName(memberContactsRequest.getName());
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


    @ApiOperation("删除紧急电话")
    @PostMapping("/remMemberCon")
    public AjaxResult removeMemberContacts(
            @RequestBody @Validated MemberContactsRequest memberContactsRequest) {
        AjaxResult ajax = AjaxResult.success();
        if (memberContactsRequest.getMemberContactsId()==null||memberContactsRequest.getMemberContactsId().equals("")){
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
        if (memberContactsRequest.getName()==null || memberContactsRequest.getName().equals("")){
            ajax = AjaxResult.error("联络人姓名不能为空！");
            return ajax;
        }
        if (memberContactsRequest.getPhone()==null || memberContactsRequest.getPhone().equals("")){
            ajax = AjaxResult.error("联络人手机号不能为空！");
            return ajax;
        }
        if (memberContactsRequest.getOrderNum()==null || memberContactsRequest.getOrderNum().equals("")){
            ajax = AjaxResult.error("显示顺序不能为空！");
            return ajax;
        }
        return null;
    }
    public String convert(int number) {
        //数字对应的汉字
        String[] num = {"一", "二", "三", "四", "五", "六", "七", "八", "九"};
        //单位
        String[] unit = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千", "万亿"};
        //将输入数字转换为字符串
        String result = String.valueOf(number);
        //将该字符串分割为数组存放
        char[] ch = result.toCharArray();
        //结果 字符串
        String str = "";
        int length = ch.length;
        for (int i = 0; i < length; i++) {
            int c = (int) ch[i] - 48;
            if (c != 0) {
                str += num[c - 1] + unit[length - i - 1];
            }
        }
        return str;
    }

}
