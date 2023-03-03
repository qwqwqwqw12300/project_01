package com.newlandnpt.varyar.api.controller.business.rocketmq;

import com.alibaba.fastjson2.JSON;
import com.newlandnpt.varyar.common.core.domain.entity.AccessInfo;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.common.utils.StringUtils;
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
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static com.newlandnpt.varyar.common.constant.CacheConstants.T_DEVICE_VAYYAR_ACCESS_KEY;
import static com.newlandnpt.varyar.system.job.VayyarDeviceAccessTask.TRIGGER_TIME;
import static com.newlandnpt.varyar.system.job.VayyarDeviceAccessTask.YYYY_MM_DD_HH_MM;
import static org.apache.rocketmq.spring.annotation.ConsumeMode.ORDERLY;

/**
 * 房间进出计算事件消息 监听器
 *
 * @author lisd
 * @date 2023/1/4
 **/
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic.access-calculate}", consumerGroup = "${rocketmq.group.access-calculate}",
        consumeMode=ORDERLY)
public class AccessCalculateListener implements RocketMQListener<Message<TDevice>> {

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
    public void onMessage(Message<TDevice> message) {
        TDevice device = message.getPayload();
        log.debug("----" + System.currentTimeMillis() + "----" + " 房间进出计算事件消息： " + JSON.toJSONString(device));

        // 计算并发送事件
        if(device.getParameter() == null || !(device.getParameter() instanceof TDevice.RadarWaveDeviceSettings)){
            return;
        }
        Date calculateTime;
        if(message.getHeaders().containsKey(TRIGGER_TIME)){
            // 如果指定了触发时间,使用触发时间作为计算时间
            try {
                Calendar current = Calendar.getInstance();
                current.setTime(DateUtils.parseDate(message.getHeaders().get(TRIGGER_TIME,String.class),
                        YYYY_MM_DD_HH_MM));
                calculateTime = current.getTime();
            }catch (Exception e){
                log.error(">>>>>>>> 触发日期转换异常，忽略执行",e);
                return;
            }
        }else{
            calculateTime = new Date();
        }

        TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings)device.getParameter();
        if(radarWaveDeviceSettings.getDeviceWarnParameter()!=null&&
                radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn()!=null&&
                "1".equals(radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getNoBody())&&
                CollectionUtils.isNotEmpty(radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getWarnRules())&&
                radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getWarnRules().stream().anyMatch(rule->"1".equals(rule.getRuleSwitch()))
        ){
            // 如果开启了无人预警则进行计算
            nobodyWarnCalculate(device, calculateTime, radarWaveDeviceSettings);
        }

        if(radarWaveDeviceSettings.getLeaveBedWarnParameter()!=null&&
                CollectionUtils.isNotEmpty(radarWaveDeviceSettings.getLeaveBedWarnParameter().getBeds())
        ){
            // 如果开启了离床预警则进行计算


        }


    }

    private void nobodyWarnCalculate(TDevice device, Date calculateTime, TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings) {
        log.debug(">>>>> 设备{}开启无人预警,开始计算", device.getNo());
        // 无人预警计算
        radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getWarnRules().forEach(rule -> {
            if(!"1".equals(rule.getRuleSwitch())){
                return;
            }
            if("0".equals(rule.getDateType()) &&rule.getStartDate()!=null){
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.setTime(rule.getStartDate());
                startCalendar.set(Calendar.HOUR_OF_DAY,0);
                startCalendar.set(Calendar.MINUTE,0);
                startCalendar.set(Calendar.SECOND,0);
                startCalendar.set(Calendar.MILLISECOND,0);
                if(calculateTime.getTime()<startCalendar.getTimeInMillis()){
                    log.debug(">>>>> 无人预警规则：{},当前时间：{},小于开始计算时间{},忽略",rule.getWarnRuleName(), calculateTime,startCalendar.getTime());
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
                if(calculateTime.getTime()>endCalendar.getTimeInMillis()){
                    log.debug(">>>>> 无人预警规则：{},当前时间：{},大于结束计算时间{},忽略",rule.getWarnRuleName(), calculateTime,endCalendar.getTime());
                    return;
                }
            }

            if("1".equals(rule.getDateType())){
                if(rule.getWeek()== null || rule.getWeek().length==0){
                    log.debug(">>>>> 无人预警规则：{},未设置计算的星期,忽略",rule.getWarnRuleName());
                    return;
                }
                Calendar now = Calendar.getInstance();
                now.setTime(calculateTime);
                // calendar 取值是1-7（1代表星期天）对应设置的值为0-6
                int dayOfWeek = now.get(Calendar.DAY_OF_WEEK)-1;
                final int finalDayOfWeek = dayOfWeek;
                if(Stream.of(rule.getWeek()).noneMatch(p->p.equals(""+finalDayOfWeek))){
                    log.debug(">>>>> 无人预警规则：{},当前星期{},未在执行计算星期{}内,忽略",rule.getWarnRuleName(),finalDayOfWeek,rule.getWeek());
                    return;
                }
            }

            if(rule.getStartTime() == null){
                // 起始时间没提供默认给 0点0分作为计算时间点
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.set(Calendar.HOUR_OF_DAY,0);
                startCalendar.set(Calendar.MINUTE,0);
                startCalendar.set(Calendar.SECOND,0);
                startCalendar.set(Calendar.MILLISECOND,0);
                rule.setStartTime(startCalendar.getTime());
            }
            if(rule.getEndTime() == null){
                // 结束时间没提供默认给 23点59分作为计算时间点
                Calendar endCalendar = Calendar.getInstance();
                endCalendar.set(Calendar.HOUR_OF_DAY,23);
                endCalendar.set(Calendar.MINUTE,59);
                endCalendar.set(Calendar.SECOND,0);
                endCalendar.set(Calendar.MILLISECOND,0);
                rule.setEndTime(endCalendar.getTime());
            }
            String redisKey = T_DEVICE_VAYYAR_ACCESS_KEY + device.getNo()+ DateFormatUtils.format(calculateTime,"yyyy-MM-dd");

            // 如果当前时间超过结束时间开始进行计算

            Calendar now = Calendar.getInstance();
            now.setTime(calculateTime);

            // 结束时间
            Calendar endTime = Calendar.getInstance();
            endTime.setTime(rule.getEndTime());
            endTime.set(Calendar.YEAR,now.get(Calendar.YEAR));
            endTime.set(Calendar.MONTH,now.get(Calendar.MONTH));
            endTime.set(Calendar.DAY_OF_MONTH,now.get(Calendar.DAY_OF_MONTH));


            // 开始时间
            Calendar startTime = Calendar.getInstance();
            startTime.setTime(rule.getStartTime());
            startTime.set(Calendar.YEAR,now.get(Calendar.YEAR));
            startTime.set(Calendar.MONTH,now.get(Calendar.MONTH));
            startTime.set(Calendar.DAY_OF_MONTH,now.get(Calendar.DAY_OF_MONTH));

            // 计算时间等于结束时间开始计算
            if(StringUtils.equals(DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM,endTime.getTime()),
                    DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM, calculateTime))){
                // 获取计算当天的进出记录
                List<AccessInfo> accessInfos = redisCache.getCacheList(redisKey);
                AccessInfo accessInfo = null;
                if(accessInfos!=null){
                    // 计算周期内有进出记录则取周期内的最后一条
                    accessInfo = accessInfos.stream()
                            .filter(p->(ENTER_ROOM.equals(p.getType())||LEAVE_ROOM.equals(p.getType()))&&
                                    p.getTime()>startTime.getTimeInMillis()&&p.getTime()<endTime.getTimeInMillis())
                            //按时间倒序排
                            .sorted(Comparator.comparing(p->-p.getTime()))
                            .findFirst()
                            // 如果没有记录
                            .orElse(null);

                }
                if(accessInfo!=null&&LEAVE_ROOM.equals(accessInfo.getType())){
                    // 如果区间内最后一条是离开则证明监控时间范围内离开未回来，抛出警告
                    deviceEventService.deviceAccessIssue(device.getNo(),"房间",LEAVE_ROOM,(now.getTimeInMillis()-accessInfo.getTime())/1000);
                }
            }

        });
        log.debug(">>>>> 设备{}无人预警计算结束", device.getNo());
    }
}
