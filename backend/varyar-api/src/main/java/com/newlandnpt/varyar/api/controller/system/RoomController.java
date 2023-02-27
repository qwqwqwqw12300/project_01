package com.newlandnpt.varyar.api.controller.system;

import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.core.domain.model.RoomRequest;
import com.newlandnpt.varyar.common.core.page.TableDataInfo;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.domain.TFamily;
import com.newlandnpt.varyar.system.domain.TMemberFamily;
import com.newlandnpt.varyar.system.domain.TRoom;
import com.newlandnpt.varyar.system.mapper.TMemberFamilyMapper;
import com.newlandnpt.varyar.system.service.IDeviceService;
import com.newlandnpt.varyar.system.service.IFamilyService;
import com.newlandnpt.varyar.system.service.IMemberFamilyService;
import com.newlandnpt.varyar.system.service.IRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Api(tags = "家庭房间信息")
@RestController
@RequestMapping("/api/room")
public class RoomController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(RoomController.class);
    @Autowired
    private IRoomService iRoomService;
    @Autowired
    private IDeviceService iDeviceService;
    @Autowired
    private IFamilyService tFamilyService;

    @Autowired
    private IMemberFamilyService iMemberFamilyService;
/*
* 获取房间列表
* */
    @ApiOperation("获取家庭房间列表")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody @Validated RoomRequest roomRequest) {
        if (roomRequest.getFamilyId()==null||roomRequest.getFamilyId().equals("")){
            throw new ServiceException(String.format("家庭Id不能为空！"));
        }
//        startPage(10000);
        startPage(roomRequest.getPageNum(),roomRequest.getPageNum());
        Long memberId = getLoginUser().getMemberId();
        // 查询当前会员与家庭关系  登录会员创建
        List<TMemberFamily> tMemberFamilys =
                iMemberFamilyService.selectTMemberFamilyByMemberFamilyId(Long.valueOf(roomRequest.getFamilyId()),memberId);
        // 查询当前会员与家庭关系  会员被分享的家庭关系
        List<TMemberFamily> tMemberFamilysShare =
                iMemberFamilyService.selectTMemberFamilyByShare(Long.valueOf(roomRequest.getFamilyId()),memberId);
        for (TMemberFamily item : tMemberFamilysShare){
            tMemberFamilys.add(item);
        }
        if(CollectionUtils.isEmpty(tMemberFamilys)||tMemberFamilys.size()==0){
            throw new ServiceException(String.format("家庭信息不存在！"));
        }
        //获取家庭与房间信息
        List<TRoom> list = iRoomService.selectTRoomByFamilyId(Long.valueOf(roomRequest.getFamilyId()));
        for (TRoom item:list){
            TDevice cond = new TDevice();
            cond.setRoomId(item.getRoomId());
            cond.setDelFlag("0");
            item.setDevices(iDeviceService.selectDeviceList(cond));
        }
        return getDataTable(list);
    }
    /*
    * 创建我的房间
    * */
    @ApiOperation("创建家庭我的房间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "familyId", value = "家庭Id(唯一标识)", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "name", value = "房间名称",  dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "roomId", value = "房间Id(唯一标识)",  dataType = "String", dataTypeClass = String.class)
    })
    @PostMapping("/creRoom")
    public AjaxResult createRoom(
            @RequestBody @Validated RoomRequest roomRequest){
        AjaxResult ajax = AjaxResult.success();
        if (roomRequest.getName()==null||roomRequest.getName().equals("") ){
            return error("房间名称不能为空！");
        }
        if (roomRequest.getFamilyId()==null||roomRequest.getFamilyId().equals("")){
            return error("家庭Id不能为空！");
        }
        //校验家庭信息是否存在
        TFamily tFamily = tFamilyService.selectTFamilyByFamilyId(Long.valueOf(roomRequest.getFamilyId()));
        if(tFamily == null ){
            return error("家庭信息不存在！");
        }
        //房间信息校验
        List<TRoom> list = iRoomService.selectTRoomByFamilyId(Long.valueOf(roomRequest.getFamilyId()));
        for (TRoom item:list){
            if(roomRequest.getName().equals(item.getName()))
            {
                return error("房间名称不能重名！");
            }
        }


        if (!String.valueOf(tFamily.getCreateById()).equals(this.getLoginUser().getMemberId().toString())){
            return error("非创建者无权限创建！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String roomNo = "R"+sdf.format(new Date());
        try {
            TRoom  tRoom = new TRoom();
            tRoom.setFamilyId(Long.valueOf(roomRequest.getFamilyId()));
            tRoom.setDelFlag("0");
            tRoom.setCreateById(String.valueOf(this.getLoginUser().getMemberId()));
            tRoom.setName(roomRequest.getName());
            tRoom.setRoomType(roomRequest.getRoomType());
            iRoomService.insertTRoom(tRoom);
            ajax = AjaxResult.success(tRoom);
        } catch (Exception e){
            log.error(e.getMessage());
            ajax = ajax.error("新增我的房间失败！");
            return ajax;
        }
        return ajax;
    }
    /*
    * 修改我的房间
    * */
    @ApiOperation("编辑修改家庭我的房间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "familyId", value = "家庭Id(唯一标识)", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "name", value = "房间名称",  dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "roomId", value = "房间Id(唯一标识)",  dataType = "String", dataTypeClass = String.class)
    })
    @PostMapping("/editRoom")
    public AjaxResult editRoom(
            @RequestBody @Validated RoomRequest roomRequest){
        AjaxResult ajax = AjaxResult.success();
        if (roomRequest.getRoomId()==null||roomRequest.getRoomId().equals("") ){
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
            tRoom.setName(roomRequest.getName());
            tRoom.setRoomType(roomRequest.getRoomType());
            iRoomService.updateTRoom(tRoom);
        } catch (Exception e){
            log.error(e.getMessage());
            ajax = ajax.error("修改我的房间失败！");
            return ajax;
        }
        return ajax;
    }
    /*
     * 删除我的房间
     * */
    @ApiOperation("删除家庭我的房间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "familyId", value = "家庭Id(唯一标识)", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "name", value = "房间名称",  dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "roomId", value = "房间Id(唯一标识)",  dataType = "String", dataTypeClass = String.class)
    })
    @PostMapping("/remRoom")
    public AjaxResult removeRoom(
            @RequestBody @Validated RoomRequest roomRequest){
        AjaxResult ajax = AjaxResult.success();
        if (roomRequest.getRoomId()==null||roomRequest.getRoomId().equals("")){
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
            log.error(e.getMessage());
            ajax = AjaxResult.error("删除我的房间失败！");
            return ajax;
        }
        return ajax;
    }
}
