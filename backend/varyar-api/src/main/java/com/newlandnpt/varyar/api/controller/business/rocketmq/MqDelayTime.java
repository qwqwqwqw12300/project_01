package com.newlandnpt.varyar.api.controller.business.rocketmq;

import com.newlandnpt.varyar.common.constant.Constants;

/**
 * @author lisd
 * @date 2023/1/13
 **/
public class MqDelayTime {

    /**
     * 根据时间差获取相应的延迟等级
     *
     * @param deltaT 时间差
     * @return
     */
    public int getDelayLevel(int deltaT) {
        int delayLevel;
        if (deltaT >= Constants.TimeSecond.TWO_HOUR) {
            //时间差大于2小时，直接延迟2小时
            delayLevel = 18;
        } else if (deltaT <= 0) {
            //如果期望时间小于当前时间，立即发送
            delayLevel = -1;
        } else {
            //rocketMq延迟等级对应的秒数数组
            final int[] delayLevelSecondArr = {1, 5, 10, 30, 60, 120, 180, 240, 300, 360, 420, 480, 540, 600, 1200, 1800, 3600, 7200};
            //由于时间小于60秒直接执行，所以初始延迟等级为5（即60秒）
            delayLevel = 0;
            //根据时间差，计算延迟等级（由于初始延迟等级为5，所有i的初始值为4）
            for (int i = 0; i < delayLevelSecondArr.length; i++) {
                long a = deltaT - delayLevelSecondArr[i];
                if (a < 0) {
                    break;
                }
                delayLevel = i + 1;
            }
        }
        return delayLevel;
    }
}
