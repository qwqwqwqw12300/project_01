package com.newlandnpt.varyar.system.core.device.settings;

import com.newlandnpt.varyar.common.core.domain.config.AppConfig;
import com.newlandnpt.varyar.common.core.domain.config.DeviceConfig;
import com.newlandnpt.varyar.common.core.domain.config.SubRegion;
import com.newlandnpt.varyar.common.core.domain.config.WalabotConfig;
import com.newlandnpt.varyar.common.exception.base.BaseException;
import com.newlandnpt.varyar.system.core.device.DeviceSettingsDisposer;
import com.newlandnpt.varyar.system.domain.TDevice.RadarWaveDeviceSettings;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

import static com.newlandnpt.varyar.system.domain.TRoomZone.FLAG_YES;

/**
 * @author lin.ju
 * @date 2023/1/30
 */
@Component
public class RadarDeviceSettingsDisposer extends DeviceSettingsDisposer<RadarWaveDeviceSettings> {


    @Value("${rocketmq.topic.deviceConfig}")
    private String deviceConfigTopic;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    protected Class<RadarWaveDeviceSettings> supportType() {
        return RadarWaveDeviceSettings.class;
    }

    @Override
    protected void insideDispose(String deviceNo, RadarWaveDeviceSettings settings) {

        DeviceConfig deviceConfig = new DeviceConfig();
        //赋值设备no = 第三方接口的设备id
        deviceConfig.setDeviceId(deviceNo);
        deviceConfig.setDirection("downstream");
        if(deviceConfig.getAppConfig() == null){
            deviceConfig.setAppConfig(new AppConfig());
        }
        if(deviceConfig.getWalabotConfig() == null){
            deviceConfig.setWalabotConfig(new WalabotConfig());
        }
        // 相当于房间长度 ，https://walabot-home.cn/#/config/rooms 控制台页面获取大于0.3
        deviceConfig.getWalabotConfig().setyMax(settings.getDeviceLocation().getRoomLength().floatValue());
        //https://walabot-home.cn/#/config/rooms 控制台页面获取默认值0.3
        deviceConfig.getWalabotConfig().setyMin(0.3F);
        // 左侧距离
        deviceConfig.getWalabotConfig().setxMin(-settings.getDeviceLocation().getRoomLeft().floatValue());
        // 右侧距离
        deviceConfig.getWalabotConfig().setxMax(settings.getDeviceLocation().getRoomRight().floatValue());
        // 高度最小值 ，https://walabot-home.cn/#/config/rooms 控制台页面获取默认值0
        deviceConfig.getWalabotConfig().setzMin(0F);
        // 房间高度
        deviceConfig.getWalabotConfig().setzMax(settings.getDeviceLocation().getRoomHeight().floatValue());
        // 高度最大值 ，https://walabot-home.cn/#/config/rooms 控制台页面获取默认值1
        deviceConfig.getWalabotConfig().setzMax(1F);
        // 默认墙壁模式：0
        deviceConfig.getWalabotConfig().setSensorMounting(0);
        // 墙壁模式下 设置高度
        deviceConfig.getWalabotConfig().setSensorHeight(settings.getDeviceLocation().getRoomHeight().floatValue());

        deviceConfig.getWalabotConfig().setEnterDuration(FLAG_YES.equals(settings.getDeviceRoomParameter().getInMonitorFlag())?
                Optional.ofNullable(settings.getDeviceRoomParameter().getEntryTime()).map(p->p.intValue()).orElse(0):0);
        deviceConfig.getWalabotConfig().setExitDuration(FLAG_YES.equals(settings.getDeviceRoomParameter().getOutMonitorFlag())?
                Optional.ofNullable(settings.getDeviceRoomParameter().getDepartureTime()).map(p->p.intValue()).orElse(0):0);

        if(CollectionUtils.isNotEmpty(settings.getRoomZones())){
            if(deviceConfig.getWalabotConfig().getTrackerSubRegions() == null){
                deviceConfig.getWalabotConfig().setTrackerSubRegions(new ArrayList<>());
            }
            settings.getRoomZones()
                    .stream()
                    .sorted(Comparator.comparing(p->p.getRoomZoneId()))
                    .forEach(roomZone ->{
                        SubRegion subRegion = new SubRegion();
                        subRegion.setEnterDuration(FLAG_YES.equals(roomZone.getInMonitorFlag())?
                                Optional.ofNullable(roomZone.getEntryTime()).map(p->p.intValue()).orElse(0):0);
                        subRegion.setExitDuration(FLAG_YES.equals(roomZone.getOutMonitorFlag())?
                                Optional.ofNullable(roomZone.getDepartureTime()).map(p->p.intValue()).orElse(0):0);

                        subRegion.setIsFallingDetection(FLAG_YES.equals(roomZone.getFallFlag()));
                        subRegion.setIsPresenceDetection(FLAG_YES.equals(roomZone.getExistFlag()));
                        subRegion.setName(roomZone.getName());

                        subRegion.setxMin(Optional.ofNullable(roomZone.getX1()).map(p->p.floatValue()).orElse(null));
                        subRegion.setxMax(Optional.ofNullable(roomZone.getX2()).map(p->p.floatValue()).orElse(null));
                        subRegion.setyMin(Optional.ofNullable(roomZone.getY1()).map(p->p.floatValue()).orElse(null));
                        subRegion.setyMax(Optional.ofNullable(roomZone.getY2()).map(p->p.floatValue()).orElse(null));
                        subRegion.setzMin(Optional.ofNullable(roomZone.getZ1()).map(p->p.floatValue()).orElse(null));
                        subRegion.setzMax(Optional.ofNullable(roomZone.getZ2()).map(p->p.floatValue()).orElse(null));
                        deviceConfig.getWalabotConfig().getTrackerSubRegions().add(subRegion);
                    });
        }

        SendResult result = rocketMQTemplate.syncSend(deviceConfigTopic, MessageBuilder.withPayload(deviceConfig).build());
        if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
            throw new BaseException("雷达波设备参数下发失败");
        }

    }
}
