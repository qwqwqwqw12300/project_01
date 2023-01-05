package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.RoomRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.system.domain.TRoom;
import com.newlandnpt.varyar.system.service.IRoomService;
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
public class RoomController extends BaseController {

    @Autowired
    private IRoomService iRoomService;
/*
* 获取房间列表
* */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody @Validated RoomRequest roomRequest) {
        startPage();
        Long memberId = getLoginUser().getMemberId();
        List<TRoom> list = iRoomService.selectTRoomList(memberId,Long.valueOf(roomRequest.getFamilyId()));
        return getDataTable(list);
    }
    /*
    * 创建我的房间
    * */
    @PostMapping("/creRoom")
    public AjaxResult createRoom(
            @RequestBody @Validated RoomRequest roomRequest){
        AjaxResult ajax = AjaxResult.success();
        if (roomRequest.getRoomName().equals("")|| roomRequest.getRoomName()==null){
            ajax = AjaxResult.error("房间名称不能为空！");
            return ajax;
        }
        if (roomRequest.getFamilyId().equals("")|| roomRequest.getFamilyId()==null){
            ajax = AjaxResult.error("家庭Id不能为空！");
            return ajax;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String roomNo = "R"+sdf.format(new Date());
        try {
            TRoom  tRoom = new TRoom();
            tRoom.setFamilyId(Long.valueOf(roomRequest.getFamilyId()));
            tRoom.setDelFlag("0");
            tRoom.setName(roomRequest.getRoomName());
            iRoomService.insertTRoom(tRoom);
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
            @RequestBody @Validated RoomRequest roomRequest){
        AjaxResult ajax = AjaxResult.success();
        if (roomRequest.getRoomId().equals("")|| roomRequest.getRoomId()==null){
            ajax = AjaxResult.error("房间Id不能为空！");
            return ajax;
        }
        //查询我的房间（需要修改的）
        TRoom tRoom =  iRoomService.selectTRoomByRoomId(Long.valueOf(roomRequest.getRoomId()));
        try {
            tRoom.setName(roomRequest.getRoomName());
            iRoomService.updateTRoom(tRoom);
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
            @RequestBody @Validated RoomRequest roomRequest){
        AjaxResult ajax = AjaxResult.success();
        if (roomRequest.getRoomId().equals("")|| roomRequest.getRoomId()==null){
            ajax = AjaxResult.error("房间Id不能为空！");
            return ajax;
        }
        //查找我的房间
        TRoom tRoom = iRoomService.selectTRoomByRoomId(Long.valueOf(roomRequest.getRoomId()));
        try {
            iRoomService.deleteTRoomByRoomId(tRoom.getRoomId());
        } catch (Exception e){
            ajax = AjaxResult.error("删除我的房间失败！");
            return ajax;
        }
        return ajax;
    }
}
