package com.newlandnpt.varyar.system.job;

import com.github.pagehelper.PageInfo;
import com.newlandnpt.varyar.common.constant.DeviceConstants;
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

import java.util.Date;
import java.util.List;

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
        int pageNo = 1,pageSize = 10;
        PageInfo pageInfo;
        String triggerTime = DateFormatUtils.format(new Date(), YYYY_MM_DD_HH_MM);
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
                if(radarWaveDeviceSettings.getLeaveBedWarnParameter()!=null&&
                        CollectionUtils.isNotEmpty(radarWaveDeviceSettings.getLeaveBedWarnParameter().getBeds())
                ){
                    hasOutWarn = true;
                }

                if(hasNobodyWarn==true || hasOutWarn == true){
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
