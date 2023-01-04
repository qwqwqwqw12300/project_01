package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.TRoomRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.system.domain.TRoom;
import com.newlandnpt.varyar.system.service.ITRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 家庭
 * 
 * @author newlandnpt
 */
@RestController
@RequestMapping("/api/room")
public class TRoomController extends BaseController {

    @Autowired
    private ITRoomService itRoomService;
/*
* 获取房间列表
* */
    @GetMapping("/list")
    public TableDataInfo list( TRoomController room) {
        startPage();
        Long memberId = getLoginUser().getMemberId();
        List<TRoom> list = itRoomService.selectTRoomList(memberId);
        return getDataTable(list);
    }
    /*
    * 创建我的房间
    * */
    @PostMapping("/creRoom")
    public AjaxResult createRoom(
            @RequestBody @Validated TRoomRequest tRoomRequest){
        AjaxResult ajax = AjaxResult.success();
        if (tRoomRequest.getRoomName().equals("")||tRoomRequest.getRoomName()==null){
            ajax = AjaxResult.error("房间名称不能为空！");
            return ajax;
        }
        if (tRoomRequest.getFamilyId().equals("")||tRoomRequest.getFamilyId()==null){
            ajax = AjaxResult.error("家庭Id不能为空！");
            return ajax;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String roomNo = "R"+sdf.format(new Date());
        try {
            TRoom  tRoom = new TRoom();
            tRoom.setFamilyId(Long.valueOf(tRoomRequest.getFamilyId()));
            tRoom.setDelFlag("0");
            tRoom.setName(tRoomRequest.getRoomName());
            itRoomService.insertTRoom(tRoom);
        } catch (Exception e){
            ajax = AjaxResult.error("新增我的房间失败！");
            return ajax;
        }
        return ajax;
    }
    /*
    * 修改我的房间
    * */
    @PostMapping("/editRoom")
    public AjaxResult editRoom(
            @RequestBody @Validated TRoomRequest tRoomRequest){
        AjaxResult ajax = AjaxResult.success();
        if (tRoomRequest.getRoomId().equals("")||tRoomRequest.getRoomId()==null){
            ajax = AjaxResult.error("房间Id不能为空！");
            return ajax;
        }
        //查询我的房间（需要修改的）
        TRoom tRoom =  itRoomService.selectTRoomByRoomId(Long.valueOf(tRoomRequest.getRoomId()));
        try {
            tRoom.setName(tRoomRequest.getRoomName());
            itRoomService.updateTRoom(tRoom);
        } catch (Exception e){
            ajax = AjaxResult.error("修改我的房间失败！");
            return ajax;
        }
        return ajax;
    }
    /*
     * 删除我的房间
     * */
    @PostMapping("/remRoom")
    public AjaxResult removeRoom(
            @RequestBody @Validated TRoomRequest tRoomRequest){
        AjaxResult ajax = AjaxResult.success();
        if (tRoomRequest.getRoomId().equals("")||tRoomRequest.getRoomId()==null){
            ajax = AjaxResult.error("房间Id不能为空！");
            return ajax;
        }
        //查找我的房间
        TRoom tRoom = itRoomService.selectTRoomByRoomId(Long.valueOf(tRoomRequest.getRoomId()));
        try {
            itRoomService.deleteTRoomByRoomId(tRoom.getRoomId());
        } catch (Exception e){
            ajax = AjaxResult.error("删除我的房间失败！");
            return ajax;
        }
        return ajax;
    }
}
