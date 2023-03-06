package com.newlandnpt.varyar.common.utils.tcp;

import com.newlandnpt.varyar.common.core.domain.entity.ClassTimePeriod;
import com.newlandnpt.varyar.common.utils.tcp.req.SetClassModelReq;

import java.util.ArrayList;
import java.util.List;

/**
 * 将时间类型转换为课堂模式的转换器
 * @author ljx
 * @date 2023/3/6
 */
public  class DateToClassModelConverter {

    public static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    public static List<SetClassModelReq.timePeriod> dateToClass(List<ClassTimePeriod> classTimePeriods){
        List<SetClassModelReq.timePeriod> timePeriods = new ArrayList<>();
        for(int i=1;i<=classTimePeriods.size();i++){
            ClassTimePeriod dataClassTimePeriod = classTimePeriods.get(i - 1);
            String time = i +"="+dataClassTimePeriod.getBeginTime()+"-"+dataClassTimePeriod.getEndTime();
            String period = dataClassTimePeriod.getPeriod();
            timePeriods.add(new SetClassModelReq.timePeriod(time,period));
        }
        return null;
    }

}
