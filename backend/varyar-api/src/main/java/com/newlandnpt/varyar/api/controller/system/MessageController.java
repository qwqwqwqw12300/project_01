package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.system.domain.TFamily;
import com.newlandnpt.varyar.system.domain.TMsg;
import com.newlandnpt.varyar.system.service.ITMsgService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统消息
 *
 * @author newlandnpt
 */
@RestController
@RequestMapping("/api/message")
public class MessageController extends BaseController {

    private ITMsgService itMsgService;

    @GetMapping("/list")
    public TableDataInfo list(TFamily tFamily) {
        startPage();
        Long memberId = getLoginUser().getMemberId();
        TMsg tMsg =new TMsg();
        List<TMsg> list = itMsgService.selectTMsgList(tMsg);
        return getDataTable(list);
    }
}
