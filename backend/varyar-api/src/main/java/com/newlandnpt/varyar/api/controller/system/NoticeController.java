package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.NoticeRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.system.domain.SysNotice;
import com.newlandnpt.varyar.system.domain.TNotice;
import com.newlandnpt.varyar.system.service.ISysNoticeService;
import com.newlandnpt.varyar.system.service.ITNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@Api(tags = "系统公告信息")
@RestController
@RequestMapping("/api/notice")
public class NoticeController extends BaseController {

    @Autowired
    private ITNoticeService itNoticeService;
    @Autowired
    private ISysNoticeService iSysNoticeService;
    /**
     * 获取系统公告
     */
    @ApiOperation("获取系统公告")
    @GetMapping("/sysNotice")
    public TableDataInfo sysNotice(){
        List<SysNotice>  list = iSysNoticeService.selectNoticeList(new SysNotice());
        for (SysNotice item:list){
            TNotice cond = new TNotice();
            cond.setSysNoticeId(Long.valueOf(item.getNoticeId()));
            cond.setMemberId(this.getLoginUser().getMemberId());
            List<TNotice> tNotices = itNoticeService.selectTNoticeList(cond);
            item.setReadFlag(tNotices.size()==0?"0":"1");
        }
        return getDataTable(list);
    }

    /**
     * 获取系统公告已读信息
     */
    @ApiOperation("获取系统公告已读信息")
    @GetMapping("/readInfo")
    public AjaxResult readInfo()
    {
        List<SysNotice>  list = iSysNoticeService.selectNoticeListByReadFlag(this.getLoginUser().getMemberId());
        List<SysNotice>  newList = new ArrayList<SysNotice>();
        for(SysNotice item:list){
            TNotice cond = new TNotice();
            cond.setSysNoticeId(Long.valueOf(item.getNoticeId()));
            cond.setMemberId(this.getLoginUser().getMemberId());
            List<TNotice> tNotices = itNoticeService.selectTNoticeList(cond);
            if (list.size()>0){
                item.setReadFlag("1");
                newList.add(item);
            }
        }
        return AjaxResult.success(newList);
    }
    /**
     * 获取系统公告未读信息数量
     */
    @ApiOperation("获取系统公告未读信息数量")
    @GetMapping("/unReadNum")
    public AjaxResult unReadNum()
    {
        List<SysNotice>  list = iSysNoticeService.selectNoticeList(new SysNotice());
        List<SysNotice>  newList = new ArrayList<SysNotice>();
        for (SysNotice item:list){
            TNotice cond = new TNotice();
            cond.setSysNoticeId(Long.valueOf(item.getNoticeId()));
            cond.setMemberId(this.getLoginUser().getMemberId());
            List<TNotice> tNotices = itNoticeService.selectTNoticeList(cond);
            if(tNotices.size()==0){
                item.setReadFlag("0");
                newList.add(item);
            }
        }
        return AjaxResult.success(newList);
    }

    /**
     * 标志系统公告已读信息
     */
    @ApiOperation("标志系统公告已读信息更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "readFlag", value = "已读未读标识 0:未读 1:已读", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "noticeId", value = "公告id",required = true,  dataType = "String", dataTypeClass = String.class)
    })
    @PostMapping("/setNoticeFlag")
    public AjaxResult setNoticeFlag(@RequestBody @Validated NoticeRequest noticeRequest)
    {   AjaxResult ajax =  AjaxResult.success();
        TNotice cond = new TNotice();
        cond.setSysNoticeId(Long.valueOf(noticeRequest.getNoticeId()));
        cond.setMemberId(this.getLoginUser().getMemberId());
        List<TNotice> tNotices = itNoticeService.selectTNoticeList(cond);
        TNotice tNotice = new TNotice();
        tNotice.setMemberId(this.getLoginUser().getMemberId());;
        tNotice.setSysNoticeId(Long.valueOf(noticeRequest.getNoticeId()));
        //同一个公告一个会员不能重复标记
        if (tNotices.size()>0 && tNotices.size()==1 ){
            return error("系统消息已读！");
        }else if(tNotices.size()==0){
            tNotice.setReadFlag("1");
            try {
                itNoticeService.insertTNotice(tNotice);
            }catch (Exception e){
                return  error("标志系统标识失败！");
            }
        }else {
            return error("标志系统标识失败！");
        }
        return ajax;
    }

}
