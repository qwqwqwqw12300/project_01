package com.newlandnpt.varyar.system.core.device.settings;

import com.newlandnpt.varyar.common.core.domain.config.AppConfig;
import com.newlandnpt.varyar.common.core.domain.config.DeviceConfig;
import com.newlandnpt.varyar.common.core.domain.config.SubRegion;
import com.newlandnpt.varyar.common.core.domain.config.WalabotConfig;
import com.newlandnpt.varyar.common.exception.base.BaseException;
import com.newlandnpt.varyar.system.core.device.DeviceSettingsDisposer;
import com.newlandnpt.varyar.system.domain.TDevice;
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
public class WatchDeviceSettingsDisposer extends DeviceSettingsDisposer<TDevice.WatchSettings> {



    @Override
    protected Class<TDevice.WatchSettings> supportType() {
        return TDevice.WatchSettings.class;
    }

    @Override
    protected void insideDispose(String deviceNo, TDevice.WatchSettings settings) {

        //todo 电子牵挂卡 参数下发 绑定电子围栏和设备

    }
}
