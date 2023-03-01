package com.newlandnpt.varyar.system.job;

import com.github.pagehelper.PageInfo;
import com.newlandnpt.varyar.common.constant.DeviceConstants;
import com.newlandnpt.varyar.common.utils.PageUtils;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.DeviceEventService;
import com.newlandnpt.varyar.system.service.IDeviceService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * vayyar 雷达波设备 24小时 无人预警
 * @author lin.ju
 * @date 2023/3/1
 */
@Component("deviceNetTask")
public class VayyarDevice24HoursExistTask {

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private DeviceEventService disconnectionService;

    public void check(){
        TDevice device = new TDevice();
        device.setType(DeviceConstants.TYPE_READER_WAVE);
        int pageNo = 1,pageSize = 100;
        PageInfo pageInfo;
        do{
            PageUtils.startPage(pageNo,pageSize);
            List<TDevice> list = deviceService.selectDeviceList(device);
            list.forEach(p->{
                disconnectionService.device24HoursExistsIssue(p.getNo());
            });
            pageInfo = new PageInfo(list);
        }while (pageInfo.isHasNextPage());

    }

}
