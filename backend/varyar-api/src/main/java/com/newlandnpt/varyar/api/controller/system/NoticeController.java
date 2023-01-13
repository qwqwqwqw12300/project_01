package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.system.domain.TNotice;
import com.newlandnpt.varyar.system.service.ITNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notice")
public class NoticeController extends BaseController {

    @Autowired
    private ITNoticeService itNoticeService;


    /**
     * 获取系统消息已读信息
     */
    @GetMapping("/readInfo")
    public TableDataInfo list()
    {
        Long memberId = this.getLoginUser().getMemberId();
        List<TNotice> list = itNoticeService.selectTNoticeByMemberId(memberId);
        return getDataTable(list);
    }
}
