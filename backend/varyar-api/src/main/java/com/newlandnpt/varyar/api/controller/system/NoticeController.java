package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.NoticeRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.system.domain.SysNotice;
import com.newlandnpt.varyar.system.domain.TNotice;
import com.newlandnpt.varyar.system.service.ISysNoticeService;
import com.newlandnpt.varyar.system.service.ITNoticeService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    @GetMapping("/sysNotice")
    public TableDataInfo sysNotice(){
        List<SysNotice>  list = iSysNoticeService.selectNoticeList(new SysNotice());
        return getDataTable(list);
    }

    /**
     * 获取系统公告已读信息
     */
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
                newList.add(item);
            }
        }
        return AjaxResult.success(newList);
    }
    /**
     * 获取系统公告未读信息数量
     */
    @GetMapping("/unReadNum")
    public AjaxResult unReadNum()
    {
        List<SysNotice>  list = iSysNoticeService.selectNoticeList(new SysNotice());
        for (SysNotice item:list){
            TNotice cond = new TNotice();
            cond.setSysNoticeId(Long.valueOf(item.getNoticeId()));
            cond.setMemberId(this.getLoginUser().getMemberId());
            List<TNotice> tNotices = itNoticeService.selectTNoticeList(cond);
            if(tNotices.size()>0){
                list.remove(item);
            }
        }

        return AjaxResult.success(list);
    }

    /**
     * 标志系统公告已读信息
     */
    @PostMapping("/setNoticeFlag")
    public AjaxResult setNoticeFlag(@RequestBody @Validated NoticeRequest noticeRequest)
    {   AjaxResult ajax =  AjaxResult.success();
        TNotice cond = new TNotice();
        cond.setSysNoticeId(Long.valueOf(noticeRequest.getNoticeId()));
        cond.setMemberId(this.getLoginUser().getMemberId());
        List<TNotice> tNotices = itNoticeService.selectTNoticeList(cond);
        TNotice tNotice = new TNotice();
        //同一个公告一个会员不能重复标记
        if (tNotices.size()>0 && tNotices.size()==1 ){
            tNotices.get(0);
            tNotice.setReadFlag("1");
            tNotice.setSysNoticeId(Long.valueOf(noticeRequest.getNoticeId()));
            tNotice.setMemberId(this.getLoginUser().getMemberId());
            try {
                itNoticeService.insertTNotice(tNotice);
            }catch (Exception e){
                ajax = ajax.error("标志系统标识失败！");
                return ajax;
            }
        }else if(tNotices.size()==0){
            tNotice.setReadFlag("1");
            try {
                itNoticeService.updateTNotice(tNotice);
            }catch (Exception e){
                ajax = ajax.error("标志系统标识失败！");
                return ajax;
            }
        }else {
            ajax = ajax.error("标志系统标识失败！");
            return ajax;
        }
        return ajax;
    }

}
