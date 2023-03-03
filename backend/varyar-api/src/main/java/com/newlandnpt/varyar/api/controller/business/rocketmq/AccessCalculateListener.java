package com.newlandnpt.varyar.api.controller.business.rocketmq;

import com.alibaba.fastjson2.JSON;
import com.newlandnpt.varyar.common.core.domain.entity.AccessInfo;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.DeviceEventService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static com.newlandnpt.varyar.common.constant.CacheConstants.T_DEVICE_VAYYAR_ACCESS_KEY;

/**
 * 房间进出计算事件消息 监听器
 *
 * @author lisd
 * @date 2023/1/4
 **/
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic.access-calculate}", consumerGroup = "${rocketmq.group.access-calculate}")
public class AccessCalculateListener implements RocketMQListener<TDevice> {

    private static final Logger log = LoggerFactory.getLogger(AccessCalculateListener.class);

    @Value("${rocketmq.topic.accessDelay}")
    private String accessDelayTopic;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /** 事件类型 离开房间 */
    public static final String LEAVE_ROOM = "leave_room";

    /** 事件类型 进入房间 */
    public static final String ENTER_ROOM = "enter_room";

    /** 事件类型 离开区域0 */
    public static final String LEAVE_ZONE_0= "leave_zone_0";
    /** 事件类型 进入区域0 */
    public static final String ENTER_ZONE_0 = "enter_zone_0";

    /** 事件类型 离开区域1 */
    public static final String LEAVE_ZONE_1= "leave_zone_1";
    /** 事件类型 进入区域1 */
    public static final String ENTER_ZONE_1 = "enter_zone_1";

    /** 事件类型 离开区域2 */
    public static final String LEAVE_ZONE_2= "leave_zone_2";
    /** 事件类型 进入区域2 */
    public static final String ENTER_ZONE_2 = "enter_zone_2";

    /** 事件类型 离开区域3 */
    public static final String LEAVE_ZONE_3= "leave_zone_3";
    /** 事件类型 进入区域3 */
    public static final String ENTER_ZONE_3 = "enter_zone_3";

    private static final String ACCESS_DELAY_TOPIC = "access_delay_";
    @Autowired
    private DeviceEventService deviceEventService;

    @Override
    public void onMessage(TDevice device) {
        log.debug("----" + System.currentTimeMillis() + "----" + " 房间进出计算事件消息： " + JSON.toJSONString(device));

        // 计算并发送事件
        if(device.getParameter() == null || !(device.getParameter() instanceof TDevice.RadarWaveDeviceSettings)){
            return;
        }
        Date currentTime = new Date();
        TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)device.getParameter();
        if(radarWaveDeviceSettings.getDeviceWarnParameter()!=null&&
                radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn()!=null&&
                "1".equals(radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getNoBody())&&
                CollectionUtils.isNotEmpty(radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getWarnRules())&&
                radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getWarnRules().stream().anyMatch(rule->"1".equals(rule.getRuleSwitch()))
        ){
            log.debug(">>>>> 设备{}开启无人预警,开始计算",device.getNo());
            // 无人预警计算
            radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getWarnRules().forEach(rule -> {
                if("0".equals(rule.getDateType()) &&rule.getStartDate()!=null){
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.setTime(rule.getStartDate());
                    startCalendar.set(Calendar.HOUR_OF_DAY,0);
                    startCalendar.set(Calendar.MINUTE,0);
                    startCalendar.set(Calendar.SECOND,0);
                    startCalendar.set(Calendar.MILLISECOND,0);
                    if(currentTime.getTime()<startCalendar.getTimeInMillis()){
                        log.debug(">>>>> 无人预警规则：{},当前时间：{},小于开始计算时间{},忽略",rule.getWarnRuleName(),currentTime,startCalendar.getTime());
                        return;
                    }
                }
                if("0".equals(rule.getDateType()) &&rule.getEndDate()!=null){
                    Calendar endCalendar = Calendar.getInstance();
                    endCalendar.setTime(rule.getEndDate());
                    endCalendar.set(Calendar.HOUR_OF_DAY,23);
                    endCalendar.set(Calendar.MINUTE,59);
                    endCalendar.set(Calendar.SECOND,59);
                    endCalendar.set(Calendar.MILLISECOND,999);
                    if(currentTime.getTime()>endCalendar.getTimeInMillis()){
                        log.debug(">>>>> 无人预警规则：{},当前时间：{},大于结束计算时间{},忽略",rule.getWarnRuleName(),currentTime,endCalendar.getTime());
                        return;
                    }
                }

                if("1".equals(rule.getDateType())){
                    if(rule.getWeek()== null || rule.getWeek().length==0){
                        log.debug(">>>>> 无人预警规则：{},未设置计算的星期,忽略",rule.getWarnRuleName());
                        return;
                    }
                    Calendar now = Calendar.getInstance();
                    now.setTime(currentTime);
                    int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
                    if(dayOfWeek == 7){
                        dayOfWeek = 0;
                    }
                    final int finalDayOfWeek = dayOfWeek;
                    if(Stream.of(rule.getWeek()).noneMatch(p->p.equals(""+finalDayOfWeek))){
                        log.debug(">>>>> 无人预警规则：{},当前星期{},未在执行计算星期{}内,忽略",rule.getWarnRuleName(),finalDayOfWeek,rule.getWeek());
                        return;
                    }
                }

                if(rule.getStartTime()!=null){
                    Calendar startTime = Calendar.getInstance();
                    startTime.setTime(rule.getStartTime());
                    Calendar now = Calendar.getInstance();
                    startTime.set(Calendar.YEAR,now.get(Calendar.YEAR));
                    startTime.set(Calendar.MONTH,now.get(Calendar.MONTH));
                    startTime.set(Calendar.DAY_OF_MONTH,now.get(Calendar.DAY_OF_MONTH));
                    if(currentTime.getTime()<startTime.getTimeInMillis()){
                        log.debug(">>>>> 无人预警规则：{},当前时间：{},小于开始计算时间{},忽略",rule.getWarnRuleName(),currentTime,startTime.getTime());
                        return;
                    }
                }


                if(rule.getStartTime() == null){
                    // 起始时间没提供默认给 0点0分0秒作为计算时间点
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.set(Calendar.HOUR_OF_DAY,23);
                    startCalendar.set(Calendar.MINUTE,59);
                    startCalendar.set(Calendar.SECOND,0);
                    startCalendar.set(Calendar.MILLISECOND,0);
                    rule.setStartTime(startCalendar.getTime());
                }
                if(rule.getEndTime() == null){
                    // 结束时间没提供默认给 23点59分0秒作为计算时间点
                    Calendar endCalendar = Calendar.getInstance();
                    endCalendar.set(Calendar.HOUR_OF_DAY,23);
                    endCalendar.set(Calendar.MINUTE,59);
                    endCalendar.set(Calendar.SECOND,0);
                    endCalendar.set(Calendar.MILLISECOND,0);
                    rule.setEndTime(endCalendar.getTime());
                }
                String redisKey = T_DEVICE_VAYYAR_ACCESS_KEY + device.getNo()+ DateFormatUtils.format(new Date(),"yyyy-MM-dd");

                // 如果当前时间超过结束时间开始进行计算
                Calendar endTime = Calendar.getInstance();
                endTime.setTime(rule.getEndTime());
                Calendar now = Calendar.getInstance();
                endTime.set(Calendar.YEAR,now.get(Calendar.YEAR));
                endTime.set(Calendar.MONTH,now.get(Calendar.MONTH));
                endTime.set(Calendar.DAY_OF_MONTH,now.get(Calendar.DAY_OF_MONTH));


                Calendar startTime = Calendar.getInstance();
                startTime.setTime(rule.getStartTime());
                startTime.set(Calendar.YEAR,now.get(Calendar.YEAR));
                startTime.set(Calendar.MONTH,now.get(Calendar.MONTH));
                startTime.set(Calendar.DAY_OF_MONTH,now.get(Calendar.DAY_OF_MONTH));

                if(currentTime.getTime()>endTime.getTimeInMillis()){
                    List<AccessInfo> accessInfos = redisCache.getCacheList(redisKey);
                    if(accessInfos!=null){
                        // 期间最后一条
                        AccessInfo accessInfo = accessInfos.stream()
                                .filter(p->(ENTER_ROOM.equals(p.getType())||LEAVE_ROOM.equals(p.getType()))&&
                                        p.getTime()>startTime.getTimeInMillis()&&p.getTime()<endTime.getTimeInMillis())
                                //按时间倒序排
                                .sorted(Comparator.comparing(p->-p.getTime()))
                                .findFirst()
                                // 如果没有记录
                                .orElse(null);
                        if(accessInfo!=null){
                            // 如果区间内最后一条是离开着证明监控时间范围内离开未回来，抛出警告
                            deviceEventService.deviceAccessIssue(device.getNo(),"房间",LEAVE_ROOM,(now.getTimeInMillis()-accessInfo.getTime())/1000);
                        }
                    }
                }

            });
            log.debug(">>>>> 设备{}无人预警计算结束",device.getNo());
        }


    }
}
