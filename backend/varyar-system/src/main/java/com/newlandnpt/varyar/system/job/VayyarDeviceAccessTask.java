package com.newlandnpt.varyar.system.job;

import com.github.pagehelper.PageInfo;
import com.newlandnpt.varyar.common.constant.DeviceConstants;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.utils.PageUtils;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.IDeviceService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.newlandnpt.varyar.common.constant.CacheConstants.T_DEVICE_VAYYAR_ACCESS_CALCULATE_TIME_WHEEL_KEY;
import static com.newlandnpt.varyar.common.core.redis.RedisCache.TimeWheelUnit.MINUTES;

/**
 * 雷达波进出监控 定时任务
 * @author lin.ju
 * @date 2023/3/2
 */
@Component("vayyarDeviceAccessTask")
public class VayyarDeviceAccessTask {

    public static final String TRIGGER_TIME = "TRIGGER_TIME";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    /**
     * 进出房间事件
     * 设备模块接收到信息，mq推送，新增进房间事件
     * 增加定时任务，规定时间内，没有出房间事件，则新增失联事件
     */
    @Value("${rocketmq.topic.access-calculate:}")
    private String accessTopic;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private IDeviceService deviceService;

    public void check(){
        TDevice device = new TDevice();
        device.setType(DeviceConstants.TYPE_READER_WAVE);
        int pageNo = 1,pageSize = 100;
        PageInfo pageInfo;
        Calendar triggerCalendar = Calendar.getInstance();
        triggerCalendar.setTime(new Date());
        String triggerTime = DateFormatUtils.format(triggerCalendar.getTime(), YYYY_MM_DD_HH_MM);
        do {
            PageUtils.startPage(pageNo++, pageSize);
            List<TDevice> list = deviceService.selectDeviceList(device);

            list.stream().parallel().forEach(p->{
                if(p.getParameter() == null || !(p.getParameter() instanceof TDevice.RadarWaveDeviceSettings)){
                    return;
                }
                TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)p.getParameter();
                boolean hasNobodyWarn = false;
                if(radarWaveDeviceSettings.getDeviceWarnParameter()!=null&&
                        radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn()!=null&&
                        "1".equals(radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getNoBody())&&
                        CollectionUtils.isNotEmpty(radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getWarnRules())&&
                        radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getWarnRules().stream().anyMatch(rule->"1".equals(rule.getRuleSwitch()))
                        ){
                    hasNobodyWarn = true;
                }

                boolean hasOutWarn = false;
                if(CollectionUtils.isNotEmpty(radarWaveDeviceSettings.getRoomZones())&&
                        radarWaveDeviceSettings.getRoomZones().stream()
                                .anyMatch(roomZone->roomZone.getLeaveBedWarnParameter()!=null&&
                                        "1".equals(roomZone.getLeaveBedWarnParameter().getLeaveBedInterval()))
                ){
                    hasOutWarn = true;
                }

                if(hasNobodyWarn==true || hasOutWarn == true){
                    // 先补偿之前遗漏执行的任务
                    RedisCache.TimeWheel timeWheel = new RedisCache.TimeWheel(T_DEVICE_VAYYAR_ACCESS_CALCULATE_TIME_WHEEL_KEY +p.getNo(),MINUTES);
                    timeWheel.setCurrentGraduationValue(triggerCalendar.getTime());
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
                        String calculateTime =DateFormatUtils.format(calculateCalendar.getTime(), YYYY_MM_DD_HH_MM);
                        // 无人预警 或离床预警开启时触发计算
                        rocketMQTemplate.syncSend(accessTopic, MessageBuilder.withPayload(p)
                                .setHeader("KEYS",p.getNo())
                                .setHeader(TRIGGER_TIME,calculateTime)
                                .build());
                        timeWheel.setCurrentGraduationValue(calculateCalendar.getTime());
                        timeWheel.next();
                    }

                    // 无人预警 或离床预警开启时触发计算
                    rocketMQTemplate.syncSend(accessTopic, MessageBuilder.withPayload(p)
                            .setHeader("KEYS",p.getNo())
                            .setHeader(TRIGGER_TIME,triggerTime)
                            .build());
                }

            });

            pageInfo = new PageInfo(list);
        }while (pageInfo.isHasNextPage());

    }

}
