package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.RoomRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.system.domain.TFamily;
import com.newlandnpt.varyar.system.domain.TMemberFamily;
import com.newlandnpt.varyar.system.domain.TRoom;
import com.newlandnpt.varyar.system.mapper.TMemberFamilyMapper;
import com.newlandnpt.varyar.system.service.IFamilyService;
import com.newlandnpt.varyar.system.service.IMemberFamilyService;
import com.newlandnpt.varyar.system.service.IRoomService;
import org.apache.commons.collections4.CollectionUtils;
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

    @Autowired
    private IFamilyService tFamilyService;

    @Autowired
    private IMemberFamilyService iMemberFamilyService;
/*
* 获取房间列表
* */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody @Validated RoomRequest roomRequest) {
        if (roomRequest.getFamilyId().equals("")|| roomRequest.getFamilyId()==null){
            throw new ServiceException(String.format("家庭Id不能为空！"));
        }
        startPage();
        Long memberId = getLoginUser().getMemberId();
        // 查询当前会员与家庭关系
        List<TMemberFamily> TMemberFamilys =
                iMemberFamilyService.selectTMemberFamilyByMemberFamilyId(Long.valueOf(roomRequest.getFamilyId()),memberId);
        if(CollectionUtils.isEmpty(TMemberFamilys)||TMemberFamilys.size()==0){
            throw new ServiceException(String.format("家庭信息不存在！"));
        }
        //获取家庭与房间信息
        List<TRoom> list = iRoomService.selectTRoomByFamilyId(Long.valueOf(roomRequest.getFamilyId()));
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
            ajax = ajax.error("房间名称不能为空！");
            return ajax;
        }
        if (roomRequest.getFamilyId().equals("")|| roomRequest.getFamilyId()==null){
            ajax = ajax.error("家庭Id不能为空！");
            return ajax;
        }
        Long memberId = getLoginUser().getMemberId();
        //校验家庭信息是否存在
        TFamily tFamily = tFamilyService.selectTFamilyByFamilyId(Long.valueOf(roomRequest.getFamilyId()));
        if(tFamily == null ){
            ajax = ajax.error("家庭信息不存在！");
            return ajax;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String roomNo = "R"+sdf.format(new Date());
        try {
            TRoom  tRoom = new TRoom();
            tRoom.setFamilyId(Long.valueOf(roomRequest.getFamilyId()));
            tRoom.setDelFlag("0");
            tRoom.setCreateById(String.valueOf(this.getLoginUser().getMemberId()));
            tRoom.setName(roomRequest.getRoomName());
            tRoom.setRoomLength(roomRequest.getRoomLength());
            tRoom.setRoomHeight(roomRequest.getRoomHeight());
            tRoom.setRoomLeft(roomRequest.getRoomLeft());
            tRoom.setRoomRight(roomRequest.getRoomRight());
            iRoomService.insertTRoom(tRoom);
            ajax = AjaxResult.success(tRoom);
        } catch (Exception e){
            ajax = ajax.error("新增我的房间失败！");
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
            ajax = ajax.error("房间Id不能为空！");
            return ajax;
        }
        //查询我的房间（需要修改的）
        TRoom tRoom =  iRoomService.selectTRoomByRoomId(Long.valueOf(roomRequest.getRoomId()));
        if(!tRoom.getCreateById().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            ajax = ajax.error("非创建者无权限修改！");
            return ajax;
        }
        try {
            tRoom.setName(roomRequest.getRoomName());
            tRoom.setRoomLength(roomRequest.getRoomLength());
            tRoom.setRoomHeight(roomRequest.getRoomHeight());
            tRoom.setRoomLeft(roomRequest.getRoomLeft());
            tRoom.setRoomRight(roomRequest.getRoomRight());
            iRoomService.updateTRoom(tRoom);
        } catch (Exception e){
            ajax = ajax.error("修改我的房间失败！");
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
        if(!tRoom.getCreateById().equals(String.valueOf(this.getLoginUser().getMemberId()))){
            ajax = AjaxResult.error("非创建者无权限删除！");
            return ajax;
        }
        try {
            iRoomService.deleteTRoomByRoomId(tRoom.getRoomId());
        } catch (Exception e){
            ajax = AjaxResult.error("删除我的房间失败！");
            return ajax;
        }
        return ajax;
    }
}
