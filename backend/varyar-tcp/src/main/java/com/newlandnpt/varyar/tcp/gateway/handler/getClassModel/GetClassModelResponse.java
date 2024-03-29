package com.newlandnpt.varyar.tcp.gateway.handler.getClassModel;

import com.newlandnpt.varyar.common.core.domain.entity.ClassTimePeriod;
import com.newlandnpt.varyar.common.core.domain.entity.LocationJob;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.IDeviceService;
import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Response;
import com.newlandnpt.varyar.tcp.dispose.setClassModel.SetClassModelReq;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 获取课堂模式响应
 * 响应报文体: SOS呼入标识@SOS呼出标识@1=开始时间-结束时间!周期!是否生效@2=开始时间-结束时间!周期!是否生效
 * @author ljx
 * @date 2023/2/22
 */
@Component
public class GetClassModelResponse extends MessageHead implements Response {
    @Autowired
    private IDeviceService iDeviceService;

    /**
     * SOS呼入标识 (0 ：SOS不可呼入 1：SOS可以呼入)
     */
    private String sosIncomingSign="0";

    /**
     * SOS呼出标识 (0：不可拨打SOS 1：可以拨打SOS)
     */
    private String sosOutgoingSign="0";

    /**
     * 时段设置
     */
    private List<timePeriod> timePeriodSetting = new ArrayList<>();

    // 当前类
    private static GetClassModelResponse getClassModelResponse;
    /**
     * 初始化
     */
    @PostConstruct
    public void init(){
        getClassModelResponse = this;
        getClassModelResponse.iDeviceService = this.iDeviceService;
    }



    public static class timePeriod{

        /**
         * 开始时间结束时间
         */
        private String time;

        /**
         * 周期
         */
        private String period;

        /**
         * 是否生效
         */
        private String enable = "1";

        public timePeriod(String time, String period) {
            this.time = time;
            this.period = period;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getEnable() {
            return enable;
        }

        public void setEnable(String enable) {
            this.enable = enable;
        }

        public String generateMessage(){
            return Arrays.asList(time,period,enable).stream().collect(Collectors.joining("!"));
        }
    }

    public List<timePeriod> getTimePeriodSetting() {
        return timePeriodSetting;
    }

    public void setTimePeriodSetting(List<timePeriod> timePeriodSetting) {
        this.timePeriodSetting = timePeriodSetting;
    }

    public String getSosIncomingSign() {
        return sosIncomingSign;
    }

    public void setSosIncomingSign(String sosIncomingSign) {
        this.sosIncomingSign = sosIncomingSign;
    }

    public String getSosOutgoingSign() {
        return sosOutgoingSign;
    }

    public void setSosOutgoingSign(String sosOutgoingSign) {
        this.sosOutgoingSign = sosOutgoingSign;
    }

    @Override
    public List<String> getResponses() {
        // 查库
        TDevice device = getClassModelResponse.iDeviceService.selectByDeviceNo(getDeviceNo());
        TDevice.WatchSettings object = (TDevice.WatchSettings) device.getParameter();
        if(Objects.isNull(object)){
            object = new TDevice.WatchSettings();
        }
        // 获取时段禁用
        List<ClassTimePeriod> classTimePeriods = object.getClassTimePeriods();
        for(int i=1;i<=classTimePeriods.size();i++){
            ClassTimePeriod dataClassTimePeriod = classTimePeriods.get(i-1);
            String time = i +"="+dataClassTimePeriod.getBeginTime()+"-"+dataClassTimePeriod.getEndTime();
            String period = dataClassTimePeriod.getPeriod();
            timePeriod timePeriod = new timePeriod(time, period);
            timePeriodSetting.add(timePeriod);
        }
        return  Arrays.asList(
                sosIncomingSign,sosOutgoingSign,
                CollectionUtils.isEmpty(timePeriodSetting)? "0" : timePeriodSetting.stream().map(p->p.generateMessage()).collect(Collectors.joining("" +
                        "@","","")));
    }
}
