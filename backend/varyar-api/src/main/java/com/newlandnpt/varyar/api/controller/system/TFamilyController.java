package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.system.domain.TFamily;
import com.newlandnpt.varyar.system.service.ITFamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 家庭
 * 
 * @author newlandnpt
 */
@RestController
@RequestMapping("/api/family")
public class TFamilyController extends BaseController {

    @Autowired
    private ITFamilyService tFamilyService;

    @GetMapping("/list")
    public TableDataInfo list(TFamily tFamily) {
        startPage();
        String memberPhone = getLoginUser().getMemberPhone();
        Long memberId = getLoginUser().getMemberId();
        System.out.println("memberPhone:"+memberPhone);
        System.out.println("memberId:"+memberId);

        //TODO
        List<TFamily> list = tFamilyService.selectTFamilyList(tFamily);
        return getDataTable(list);
    }
}
