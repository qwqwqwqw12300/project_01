package com.newlandnpt.varyar.tcp.dispose.incoming.locationMode;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 设置定位模式
 * @author ljx
 * @date 2023/2/23
 */
public class SetLocationModeReq extends MessageHead implements Req {
    /**
     * 实时模式
     */
    private final static String REAL_TIME_MODE = "3";
    /**
     * 定位模式 ( 1省电模式 2平衡模式 3实时模式 )
     * 默认为平衡模式
     */
    private String modeType = "2";

    /**
     * 工作时长
     */
    private String workTime = "";

    public SetLocationModeReq() {
        setApiType("SET_LOCATION_MODE");
        setMsgType("1");
    }

    public String getModeType() {
        return modeType;
    }

    public void setModeType(String modeType) {
        this.modeType = modeType;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    @Override
    public List<String> getRequests() {
        if(REAL_TIME_MODE.equals(getModeType())){
            return Arrays.asList(getModeType(),getWorkTime());
        }else{
            return Collections.singletonList(getModeType());
        }
    }
}
