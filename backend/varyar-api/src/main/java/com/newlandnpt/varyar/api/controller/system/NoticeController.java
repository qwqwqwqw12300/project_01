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
        List<SysNotice>  list = iSysNoticeService.selectNoticeListByMemberId(this.getLoginUser().getMemberId());
        for (SysNotice item:list){
            if (item.getReadFlag()==null|| item.getReadFlag().equals("")){
                item.setReadFlag("0");
            }
        }
        return getDataTable(list);
    }

    /**
     * 获取系统公告已读信息
     */
    @GetMapping("/readInfo")
    public TableDataInfo readInfo()
    {
        List<SysNotice>  list = iSysNoticeService.selectNoticeListByReadFlag(this.getLoginUser().getMemberId());
        for (int i = 0; i <list.size() ; i++) {
            SysNotice item = list.get(i);
            if (item.getReadFlag()==null|| item.getReadFlag().equals("")){
                list.remove(i);
            }
        }
        return getDataTable(list);
    }

    /**
     * 标志系统公告已读信息
     */
    @PostMapping("/setNoticeFlag")
    public AjaxResult setNoticeFlag(@RequestBody @Validated NoticeRequest noticeRequest)
    {   AjaxResult ajax =  AjaxResult.success();
        TNotice tNotice = itNoticeService.selectTNoticeByNoticeId(Long.valueOf(noticeRequest.getNoticeId()));
        if (tNotice !=null ){
            tNotice.setReadFlag("1");
            try {
                itNoticeService.updateTNotice(tNotice);
            }catch (Exception e){
                ajax = ajax.error("标志系统标识失败！");
                return ajax;
            }
        }else{
            tNotice = new TNotice();
            tNotice.setReadFlag("1");
            tNotice.setSysNoticeId(Long.valueOf(noticeRequest.getNoticeId()));
            tNotice.setMemberId(this.getLoginUser().getMemberId());
            try {
                itNoticeService.insertTNotice(tNotice);
            }catch (Exception e){
                ajax = ajax.error("标志系统标识失败！");
                return ajax;
            }
        }
        return ajax;
    }

}
