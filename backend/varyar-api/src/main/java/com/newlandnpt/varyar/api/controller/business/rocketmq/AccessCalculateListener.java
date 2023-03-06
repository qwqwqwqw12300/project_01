package com.newlandnpt.varyar.api.controller.business.rocketmq;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newlandnpt.varyar.common.core.domain.entity.AccessInfo;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.domain.TRoomZone;
import com.newlandnpt.varyar.system.service.DeviceEventService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.newlandnpt.varyar.common.constant.CacheConstants.T_DEVICE_VAYYAR_ACCESS_KEY;
import static com.newlandnpt.varyar.common.constant.CacheConstants.T_DEVICE_VAYYAR_LEAVE_BED_WARN_MARK_KEY;
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
        consumeMode = ORDERLY)
public class AccessCalculateListener implements RocketMQListener<MessageExt> {

    private static final Logger log = LoggerFactory.getLogger(AccessCalculateListener.class);
    @Autowired(required = false)
    private ObjectMapper jacksonObjectMapper = new ObjectMapper();
    @Value("${rocketmq.topic.accessDelay}")
    private String accessDelayTopic;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 事件类型 离开房间
     */
    public static final String LEAVE_ROOM = "leave_room";

    /**
     * 事件类型 进入房间
     */
    public static final String ENTER_ROOM = "enter_room";

    /**
     * 事件类型 离开区域0
     */
    public static final String LEAVE_ZONE_0 = "leave_zone_0";
    /**
     * 事件类型 进入区域0
     */
    public static final String ENTER_ZONE_0 = "enter_zone_0";

    /**
     * 事件类型 离开区域1
     */
    public static final String LEAVE_ZONE_1 = "leave_zone_1";
    /**
     * 事件类型 进入区域1
     */
    public static final String ENTER_ZONE_1 = "enter_zone_1";

    /**
     * 事件类型 离开区域2
     */
    public static final String LEAVE_ZONE_2 = "leave_zone_2";
    /**
     * 事件类型 进入区域2
     */
    public static final String ENTER_ZONE_2 = "enter_zone_2";

    /**
     * 事件类型 离开区域3
     */
    public static final String LEAVE_ZONE_3 = "leave_zone_3";
    /**
     * 事件类型 进入区域3
     */
    public static final String ENTER_ZONE_3 = "enter_zone_3";

    private static final String ACCESS_DELAY_TOPIC = "access_delay_";
    @Autowired
    private DeviceEventService deviceEventService;

    @Override
    public void onMessage(MessageExt message) {

        log.debug("----" + System.currentTimeMillis() + "----" + " 房间进出计算事件消息： " + new String(message.getBody()));
        TDevice device;
        try{
            device = jacksonObjectMapper.readValue(message.getBody(),TDevice.class);
        }catch (Exception e){
            log.error(">>>>>> 房间进出计算事件消息处理 类型转换异常，忽略执行",e);
            return;
        }

        // 计算并发送事件
        if (device.getParameter() == null || !(device.getParameter() instanceof TDevice.RadarWaveDeviceSettings)) {
            return;
        }
        Date calculateTime;
        if (message.getProperties().containsKey(TRIGGER_TIME)) {
            // 如果指定了触发时间,使用触发时间作为计算时间
            try {
                Calendar current = Calendar.getInstance();
                current.setTime(DateUtils.parseDate(message.getProperties().get(TRIGGER_TIME),
                        YYYY_MM_DD_HH_MM));
                calculateTime = current.getTime();
            } catch (Exception e) {
                log.error(">>>>>>>> 触发日期转换异常，忽略执行", e);
                return;
            }
        } else {
            calculateTime = new Date();
        }

        TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings) device.getParameter();
        if (radarWaveDeviceSettings.getDeviceWarnParameter() != null &&
                radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn() != null &&
                "1".equals(radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getNoBody()) &&
                CollectionUtils.isNotEmpty(radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getWarnRules()) &&
                radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getWarnRules().stream().anyMatch(rule -> "1".equals(rule.getRuleSwitch()))
        ) {
            // 如果开启了无人预警则进行计算
            nobodyWarnCalculate(device, calculateTime, radarWaveDeviceSettings);
        }

        if (CollectionUtils.isNotEmpty(radarWaveDeviceSettings.getRoomZones()) &&
                radarWaveDeviceSettings.getRoomZones().stream()
                        .anyMatch(roomZone -> roomZone.getLeaveBedWarnParameter() != null &&
                                "1".equals(roomZone.getLeaveBedWarnParameter().getLeaveBedInterval()))
        ) {
            // 如果开启了离床预警则进行计算
            leaveBedWarnCalculate(device, calculateTime, radarWaveDeviceSettings);
        }

    }

    private void leaveBedWarnCalculate(TDevice device, Date calculateTime, TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings) {
        for(int zoneNo=0;zoneNo<radarWaveDeviceSettings.getRoomZones().size();zoneNo++){
            TRoomZone roomZone = radarWaveDeviceSettings.getRoomZones().get(zoneNo);
            final int finalZoneNo = zoneNo;
            if (roomZone.getLeaveBedWarnParameter() == null || !"1".equals(roomZone.getLeaveBedWarnParameter().getLeaveBedInterval())) {
                return;
            }
            // 时间日期不为空时进行时间范围判断，为空则代表全天候都进行离床预警
            if (roomZone.getLeaveBedWarnParameter().getSetRuleDate() != null) {
                if ("0".equals(roomZone.getLeaveBedWarnParameter().getSetRuleDate().getDateType()) && roomZone.getLeaveBedWarnParameter().getSetRuleDate().getStartDate() != null) {
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.setTime(roomZone.getLeaveBedWarnParameter().getSetRuleDate().getStartDate());
                    startCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    startCalendar.set(Calendar.MINUTE, 0);
                    startCalendar.set(Calendar.SECOND, 0);
                    startCalendar.set(Calendar.MILLISECOND, 0);
                    if (calculateTime.getTime() < startCalendar.getTimeInMillis()) {
                        log.debug(">>>>> 离床预警规则：{},当前时间：{},小于开始计算时间{},忽略", roomZone.getName(), calculateTime, startCalendar.getTime());
                        return;
                    }
                }
                if ("0".equals(roomZone.getLeaveBedWarnParameter().getSetRuleDate().getDateType()) && roomZone.getLeaveBedWarnParameter().getSetRuleDate().getEndDate() != null) {
                    Calendar endCalendar = Calendar.getInstance();
                    endCalendar.setTime(roomZone.getLeaveBedWarnParameter().getSetRuleDate().getEndDate());
                    endCalendar.set(Calendar.HOUR_OF_DAY, 23);
                    endCalendar.set(Calendar.MINUTE, 59);
                    endCalendar.set(Calendar.SECOND, 59);
                    endCalendar.set(Calendar.MILLISECOND, 999);
                    if (calculateTime.getTime() > endCalendar.getTimeInMillis()) {
                        log.debug(">>>>> 离床预警规则：{},当前时间：{},大于结束计算时间{},忽略", roomZone.getName(), calculateTime, endCalendar.getTime());
                        return;
                    }
                }

                if ("1".equals(roomZone.getLeaveBedWarnParameter().getSetRuleDate().getDateType())) {
                    if (roomZone.getLeaveBedWarnParameter().getSetRuleDate().getWeek() == null || roomZone.getLeaveBedWarnParameter().getSetRuleDate().getWeek().length == 0) {
                        log.debug(">>>>> 无人预警规则：{},未设置计算的星期,忽略", roomZone.getName());
                        return;
                    }
                    Calendar now = Calendar.getInstance();
                    now.setTime(calculateTime);
                    // calendar 取值是1-7（1代表星期天）对应设置的值为0-6
                    int dayOfWeek = now.get(Calendar.DAY_OF_WEEK) - 1;
                    final int finalDayOfWeek = dayOfWeek;
                    if (Stream.of(roomZone.getLeaveBedWarnParameter().getSetRuleDate().getWeek()).noneMatch(p -> p.equals("" + finalDayOfWeek))) {
                        log.debug(">>>>> 离床预警规则：{},当前星期{},未在执行计算星期{}内,忽略", roomZone.getName(), finalDayOfWeek, roomZone.getLeaveBedWarnParameter().getSetRuleDate().getWeek());
                        return;
                    }
                }
            }

            String redisKey = T_DEVICE_VAYYAR_ACCESS_KEY + device.getNo() + DateFormatUtils.format(calculateTime, ":yyyy-MM-dd");

            Calendar now = Calendar.getInstance();
            now.setTime(calculateTime);
            //是否跨天
            boolean crossDay = false;

            // 结束时间
            Calendar endTime = Calendar.getInstance();
            if (roomZone.getLeaveBedWarnParameter().getSetRuleDate() != null &&
                    roomZone.getLeaveBedWarnParameter().getSetRuleDate().getEndTime() != null) {
                endTime.setTime(roomZone.getLeaveBedWarnParameter().getSetRuleDate().getEndTime());
            } else {
                endTime.set(Calendar.HOUR_OF_DAY, 23);
                endTime.set(Calendar.MINUTE, 59);
                endTime.set(Calendar.SECOND, 0);
                endTime.set(Calendar.MILLISECOND, 0);
            }
            endTime.set(Calendar.YEAR, now.get(Calendar.YEAR));
            endTime.set(Calendar.MONTH, now.get(Calendar.MONTH));
            endTime.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

            // 开始时间
            Calendar startTime = Calendar.getInstance();
            if (roomZone.getLeaveBedWarnParameter().getSetRuleDate() != null &&
                    roomZone.getLeaveBedWarnParameter().getSetRuleDate().getStartTime() != null) {
                startTime.setTime(roomZone.getLeaveBedWarnParameter().getSetRuleDate().getStartTime());
            } else {
                // 起始时间没提供默认给 0点0分作为计算时间点
                startTime.set(Calendar.HOUR_OF_DAY, 0);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.SECOND, 0);
                startTime.set(Calendar.MILLISECOND, 0);
            }
            startTime.set(Calendar.YEAR, now.get(Calendar.YEAR));
            startTime.set(Calendar.MONTH, now.get(Calendar.MONTH));
            startTime.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
            if(startTime.getTimeInMillis()>endTime.getTimeInMillis()){
                if(startTime.getTimeInMillis()<calculateTime.getTime()){
                    // 开始时间比计算时间小，说明还在当天,结束时间设置为计算时间
                    endTime.setTime(calculateTime);
                }else{
                    // 起始时间大于结束时间说明是设置跨天的夜间时段，开始时间减一天
                    startTime.add(Calendar.DAY_OF_MONTH,-1);
                    crossDay = true;
                }
            }

            if (now.getTimeInMillis() >= startTime.getTimeInMillis() &&
                    now.getTimeInMillis() <= endTime.getTimeInMillis()) {
                // 在无人预警计算时间范围内则开始计算
                // 获取计算当天的进出记录
                // 获取计算当天的进出记录
                List<AccessInfo> accessInfos = new ArrayList<>();
                List<AccessInfo> cacheList = redisCache.getCacheList(redisKey);
                if(cacheList!=null){
                    accessInfos.addAll(cacheList);
                }
                if(crossDay){
                    //跨天的情况还需获取前一天的数据
                    String lastDayRedisKey = T_DEVICE_VAYYAR_ACCESS_KEY + device.getNo() + DateFormatUtils.format(startTime, ":yyyy-MM-dd");
                    List<AccessInfo> lastDayCacheList = redisCache.getCacheList(lastDayRedisKey);
                    if(lastDayCacheList!=null){
                        accessInfos.addAll(lastDayCacheList);
                    }
                }
                AccessInfo accessInfo = null;
                // 计算周期内有进出记录则取周期内的最后一条
                accessInfo = accessInfos.stream()
                        .filter(p -> (("enter_zone_"+finalZoneNo).equals(p.getType()) || ("leave_zone_"+finalZoneNo).equals(p.getType())) &&
                                p.getTime() > startTime.getTimeInMillis() && p.getTime() < endTime.getTimeInMillis())
                        //按时间倒序排
                        .sorted(Comparator.comparing(p -> -p.getTime()))
                        .findFirst()
                        // 如果没有记录
                        .orElse(null);


                if (accessInfo != null && accessInfo.getType().startsWith("leave_zone_")) {
                    // 如果区间内最后一条是离开则证明监控时间范围内离开未回来
                    // 判断当前时间点是否已触发过警告
                    String markKey = T_DEVICE_VAYYAR_LEAVE_BED_WARN_MARK_KEY+device.getNo()+DateFormatUtils.format(calculateTime,":yyyy-MM-dd");
                    List<AccessInfo> marks = redisCache.getCacheList(markKey);
                    final AccessInfo finalAccessInfo = accessInfo;
                    if(CollectionUtils.isNotEmpty(marks)&&marks.stream()
                            .anyMatch(p->p.getTime()==finalAccessInfo.getTime()&&
                                    finalAccessInfo.getZoneNo() == p.getZoneNo()&&
                                    StringUtils.equals(finalAccessInfo.getType(),p.getType()))){
                        log.debug(">>>>>> 已经触发过告警，忽略本次告警");
                        return;
                    }

                    boolean triggerWarn = false;
                    if (StringUtils.equals(DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM, endTime.getTime()),
                            DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM, calculateTime))) {
                        //计算时间等于结束时间 直接触发警告
                        triggerWarn = true;
                    } else {
                        // 否则计算是否离开超过间隔时间
                        if (calculateTime.getTime() >= accessInfo.getTime() + (roomZone.getLeaveBedWarnParameter().getIntervalTime() * 1000)) {
                            triggerWarn = true;
                        }
                    }
                    if (triggerWarn) {
                        deviceEventService.deviceAccessIssue(device.getNo(), roomZone.getName(), "leave_zone_" + zoneNo, (now.getTimeInMillis() - accessInfo.getTime()) / 1000);
                        boolean hasKey = redisCache.hasKey(markKey);
                        redisCache.setCacheList(markKey, Arrays.asList(accessInfo));
                        if(!hasKey){
                            // 初次设值的时候设值过期时间二天
                            redisCache.expire(redisKey,2, TimeUnit.DAYS);
                        }
                    }
                }
            }
        }
    }

    private void nobodyWarnCalculate(TDevice device, Date calculateTime, TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings) {
        log.debug(">>>>> 设备{}开启无人预警,开始计算", device.getNo());
        // 无人预警计算
        radarWaveDeviceSettings.getDeviceWarnParameter().getNobodyWarn().getWarnRules().forEach(rule -> {
            if (!"1".equals(rule.getRuleSwitch())) {
                return;
            }
            if ("0".equals(rule.getDateType()) && rule.getStartDate() != null) {
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.setTime(rule.getStartDate());
                startCalendar.set(Calendar.HOUR_OF_DAY, 0);
                startCalendar.set(Calendar.MINUTE, 0);
                startCalendar.set(Calendar.SECOND, 0);
                startCalendar.set(Calendar.MILLISECOND, 0);
                if (calculateTime.getTime() < startCalendar.getTimeInMillis()) {
                    log.debug(">>>>> 无人预警规则：{},当前时间：{},小于开始计算时间{},忽略", rule.getWarnRuleName(), calculateTime, startCalendar.getTime());
                    return;
                }
            }
            if ("0".equals(rule.getDateType()) && rule.getEndDate() != null) {
                Calendar endCalendar = Calendar.getInstance();
                endCalendar.setTime(rule.getEndDate());
                endCalendar.set(Calendar.HOUR_OF_DAY, 23);
                endCalendar.set(Calendar.MINUTE, 59);
                endCalendar.set(Calendar.SECOND, 59);
                endCalendar.set(Calendar.MILLISECOND, 999);
                if (calculateTime.getTime() > endCalendar.getTimeInMillis()) {
                    log.debug(">>>>> 无人预警规则：{},当前时间：{},大于结束计算时间{},忽略", rule.getWarnRuleName(), calculateTime, endCalendar.getTime());
                    return;
                }
            }

            if ("1".equals(rule.getDateType())) {
                if (rule.getWeek() == null || rule.getWeek().length == 0) {
                    log.debug(">>>>> 无人预警规则：{},未设置计算的星期,忽略", rule.getWarnRuleName());
                    return;
                }
                Calendar now = Calendar.getInstance();
                now.setTime(calculateTime);
                // calendar 取值是1-7（1代表星期天）对应设置的值为0-6
                int dayOfWeek = now.get(Calendar.DAY_OF_WEEK) - 1;
                final int finalDayOfWeek = dayOfWeek;
                if (Stream.of(rule.getWeek()).noneMatch(p -> p.equals("" + finalDayOfWeek))) {
                    log.debug(">>>>> 无人预警规则：{},当前星期{},未在执行计算星期{}内,忽略", rule.getWarnRuleName(), finalDayOfWeek, rule.getWeek());
                    return;
                }
            }

            if (rule.getStartTime() == null) {
                // 起始时间没提供默认给 0点0分作为计算时间点
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.set(Calendar.HOUR_OF_DAY, 0);
                startCalendar.set(Calendar.MINUTE, 0);
                startCalendar.set(Calendar.SECOND, 0);
                startCalendar.set(Calendar.MILLISECOND, 0);
                rule.setStartTime(startCalendar.getTime());
            }
            if (rule.getEndTime() == null) {
                // 结束时间没提供默认给 23点59分作为计算时间点
                Calendar endCalendar = Calendar.getInstance();
                endCalendar.set(Calendar.HOUR_OF_DAY, 23);
                endCalendar.set(Calendar.MINUTE, 59);
                endCalendar.set(Calendar.SECOND, 0);
                endCalendar.set(Calendar.MILLISECOND, 0);
                rule.setEndTime(endCalendar.getTime());
            }
            String redisKey = T_DEVICE_VAYYAR_ACCESS_KEY + device.getNo() + DateFormatUtils.format(calculateTime, ":yyyy-MM-dd");

            // 如果当前时间超过结束时间开始进行计算

            Calendar now = Calendar.getInstance();
            now.setTime(calculateTime);

            // 结束时间
            Calendar endTime = Calendar.getInstance();
            endTime.setTime(rule.getEndTime());
            endTime.set(Calendar.YEAR, now.get(Calendar.YEAR));
            endTime.set(Calendar.MONTH, now.get(Calendar.MONTH));
            endTime.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

            //是否跨天
            boolean crossDay = false;
            // 开始时间
            Calendar startTime = Calendar.getInstance();
            startTime.setTime(rule.getStartTime());
            startTime.set(Calendar.YEAR, now.get(Calendar.YEAR));
            startTime.set(Calendar.MONTH, now.get(Calendar.MONTH));
            startTime.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
            if(startTime.getTimeInMillis()>endTime.getTimeInMillis()){
                if(startTime.getTimeInMillis()<calculateTime.getTime()){
                    // 开始时间比计算时间小，说明还在当天,结束时间设置为计算时间
                    endTime.setTime(calculateTime);
                }else{
                    // 起始时间大于结束时间说明是设置跨天的夜间时段，开始时间减一天
                    startTime.add(Calendar.DAY_OF_MONTH,-1);
                    crossDay = true;
                }
            }

            // 计算时间等于结束时间开始计算
            if (StringUtils.equals(DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM, endTime.getTime()),
                    DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM, calculateTime))) {
                // 获取计算当天的进出记录
                List<AccessInfo> accessInfos = new ArrayList<>();
                List<AccessInfo> cacheList = redisCache.getCacheList(redisKey);
                if(cacheList!=null){
                    accessInfos.addAll(cacheList);
                }
                if(crossDay){
                    //跨天的情况还需获取前一天的数据
                    String lastDayRedisKey = T_DEVICE_VAYYAR_ACCESS_KEY + device.getNo() + DateFormatUtils.format(startTime, ":yyyy-MM-dd");
                    List<AccessInfo> lastDayCacheList = redisCache.getCacheList(lastDayRedisKey);
                    if(lastDayCacheList!=null){
                        accessInfos.addAll(lastDayCacheList);
                    }
                }
                AccessInfo accessInfo = null;
                // 计算周期内有进出记录则取周期内的最后一条
                accessInfo = accessInfos.stream()
                        .filter(p -> (ENTER_ROOM.equals(p.getType()) || LEAVE_ROOM.equals(p.getType())) &&
                                p.getTime() > startTime.getTimeInMillis() && p.getTime() < endTime.getTimeInMillis())
                        //按时间倒序排
                        .sorted(Comparator.comparing(p -> -p.getTime()))
                        .findFirst()
                        // 如果没有记录
                        .orElse(null);
                if (accessInfo != null && LEAVE_ROOM.equals(accessInfo.getType())) {
                    // 如果区间内最后一条是离开则证明监控时间范围内离开未回来，抛出警告
                    deviceEventService.deviceAccessIssue(device.getNo(), "房间", LEAVE_ROOM, (now.getTimeInMillis() - accessInfo.getTime()) / 1000);
                }
            }

        });
        log.debug(">>>>> 设备{}无人预警计算结束", device.getNo());
    }
}
