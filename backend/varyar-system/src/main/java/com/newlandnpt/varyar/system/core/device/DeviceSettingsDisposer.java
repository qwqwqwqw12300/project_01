package com.newlandnpt.varyar.system.core.device;

import com.newlandnpt.varyar.common.exception.base.BaseException;
import com.newlandnpt.varyar.system.domain.TDevice;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lin.ju
 * @date 2023/1/30
 */
public abstract class DeviceSettingsDisposer<T extends TDevice.DeviceSettings> {

    private static Map<Class<? extends TDevice.DeviceSettings>,DeviceSettingsDisposer> map =
            new HashMap<>();

    /**
     * 支持的设备参数类型
     * @return
     */
    protected abstract Class<T> supportType();

    /**
     * 下发设备设置，内部方法
     */
    protected abstract void insideDispose(String deviceNo, T settings);

    /**
     * 下发设备设置
     * @param settings
     */
    public static void dispose(String deviceNo,TDevice.DeviceSettings settings){
        if(map.containsKey(settings.getClass())){

        }else {
            throw new BaseException("设备编号："+deviceNo+"未找到匹配的下发方式");
        }
    }

    @Component
    public static class DeviceSettingsDisposerRegister implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

            if(bean instanceof DeviceSettingsDisposer){
                map.put(((DeviceSettingsDisposer<?>) bean).supportType(),(DeviceSettingsDisposer)bean);
            }

            return bean;
        }
    }

}
