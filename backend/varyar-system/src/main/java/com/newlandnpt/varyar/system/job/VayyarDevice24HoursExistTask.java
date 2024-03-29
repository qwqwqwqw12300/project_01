package com.newlandnpt.varyar.system.job;

import com.github.pagehelper.PageInfo;
import com.newlandnpt.varyar.common.constant.DeviceConstants;
import com.newlandnpt.varyar.common.core.domain.entity.AccessInfo;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.utils.PageUtils;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.DeviceEventService;
import com.newlandnpt.varyar.system.service.IDeviceService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.newlandnpt.varyar.common.constant.CacheConstants.*;
import static com.newlandnpt.varyar.common.core.redis.RedisCache.TimeWheelUnit.MINUTES;

/**
 * vayyar 雷达波设备 24小时 无人预警
 * @author lin.ju
 * @date 2023/3/1
 */
@Component("vayyarDevice24HoursExistTask")
public class VayyarDevice24HoursExistTask {

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private DeviceEventService disconnectionService;
    @Autowired
    private RedisCache redisCache;

    public void check(){
        TDevice device = new TDevice();
        device.setType(DeviceConstants.TYPE_READER_WAVE);
        int pageNo = 1,pageSize = 100;
        PageInfo pageInfo;
        do{
            PageUtils.startPage(pageNo++,pageSize);
            List<TDevice> list = deviceService.selectDeviceList(device);
            list.forEach(p->{
                if(p.getParameter() == null || !(p.getParameter() instanceof TDevice.RadarWaveDeviceSettings)){
                    return;
                }
                TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)p.getParameter();

                if(radarWaveDeviceSettings.getDeviceWarnParameter()!=null &&
                        radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn()!=null &&
                        "1".equals(radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getNoBody())&&
                        "1".equals(radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getNoBodyContinue())){
                    // 开启24小时无人预警后计算
                    String outKey = T_DEVICE_VAYYAR_ACCESS_24HOURS_OUT_ROOM_KEY + p.getNo();
                    AccessInfo outInfo = redisCache.getCacheObject(outKey);
                    if(outInfo == null){
                        // 房间未离开
                        return;
                    }
                    String inKey = T_DEVICE_VAYYAR_ACCESS_24HOURS_IN_ROOM_KEY + p.getNo();
                    AccessInfo inInfo = redisCache.getCacheObject(inKey);
                    if(inInfo!=null){
                        // 进入信息不为空 判断进入时间事件如果大于离开,则忽略
                        if(inInfo.getTime()>outInfo.getTime()){
                            return;
                        }
                    }

                    Calendar triggerCalendar = Calendar.getInstance();
                    triggerCalendar.setTime(new Date());
                    // 先补偿之前遗漏执行的任务
                    RedisCache.TimeWheel timeWheel = new RedisCache.TimeWheel(T_DEVICE_VAYYAR_ACCESS_24HOURS_CALCULATE_TIME_WHEEL_KEY +p.getNo(),MINUTES);
                    timeWheel.setCurrentGraduation(triggerCalendar.getTime());
                    LocalTime triggerGraduation = timeWheel.getCurrentGraduation();
                    timeWheel.homing(LocalDate.of(triggerCalendar.get(Calendar.YEAR),
                            triggerCalendar.get(Calendar.MONTH)+1,triggerCalendar.get(Calendar.DAY_OF_MONTH)));
                    Calendar calculateCalendar = Calendar.getInstance();
                    while (triggerGraduation.compareTo(timeWheel.getCurrentGraduation())!=0){
                        calculateCalendar.setTime(triggerCalendar.getTime());
                        if(triggerGraduation.toSecondOfDay()<timeWheel.getCurrentGraduation().toSecondOfDay()){
                            // 如果触发时间小，说明查了一天，这里减一天
                            calculateCalendar.add(Calendar.DAY_OF_MONTH,-1);
                        }
                        // 回退差值
                        calculateCalendar.add(Calendar.SECOND,-(triggerGraduation.toSecondOfDay()-timeWheel.getCurrentGraduation().toSecondOfDay()));

                        triggerEvent(p, outKey, outInfo, calculateCalendar.getTimeInMillis());
                        timeWheel.setCurrentGraduationValue(calculateCalendar.getTime());
                        timeWheel.next();
                    }

                    triggerEvent(p, outKey, outInfo, triggerCalendar.getTimeInMillis());
                    timeWheel.setCurrentGraduationValue(triggerCalendar.getTime());

                }

            });
            pageInfo = new PageInfo(list);
        }while (pageInfo.isHasNextPage());

    }

    private void triggerEvent(TDevice p, String outKey, AccessInfo outInfo, long currentTimeMills) {
        currentTimeMills -= outInfo.getTime()+
                24*60*60*1000;
        if(currentTimeMills >=0){
            // 触发24小时无人预警
            disconnectionService.device24HoursExistsIssue(p.getNo(), p);
            // 离开时间更新到当前时间，开始新一轮计算周期
            outInfo.setTime(new Date().getTime());
            redisCache.setCacheObject(outKey, outInfo);
        }
    }

}
