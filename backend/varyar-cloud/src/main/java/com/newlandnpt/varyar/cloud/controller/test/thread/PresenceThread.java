package com.newlandnpt.varyar.cloud.controller.test.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.newlandnpt.varyar.cloudBase.domain.Presence;
import com.newlandnpt.varyar.cloudBase.domain.vo.TrackerTargetVo;
import com.newlandnpt.varyar.cloudBase.utils.HttpClientUtil;
import com.newlandnpt.varyar.common.utils.spring.SpringUtils;

@Service("test.data.thread.presenceThread")
public class PresenceThread extends Thread {

	private static final String serverUrl = "127.0.0.1";
	
	@Override
	public void run() {
		String serverPort = SpringUtils.getRequiredProperty("server.port");
		int i = 0;
		Presence presence = null;
        while (true) {
        	presence = new Presence();
        	presence.setTimestamp(new Date());
        	presence.setDeviceId("id_QTg6MDM6MkE6NjI6REI6Rkm");
        	presence.setTimestampMillis(System.currentTimeMillis());
        	presence.setRoomPresenceDetected(true);
        	presence.setPresenceRegionMap("{\"0\":0,\"1\":0,\"2\":1,\"3\":0}");
        	List<TrackerTargetVo> trackerTargets = new ArrayList<>();
        	TrackerTargetVo vo = new TrackerTargetVo();
        	vo.setId("0");
        	vo.setxPosCm("-36.0");
        	vo.setyPosCm("92.0");
        	vo.setzPosCm("16.0");
        	trackerTargets.add(vo);
        	presence.setTrackerTargets(trackerTargets);
        	HttpClientUtil.sendPost("http://"+serverUrl+":"+serverPort+"/cloud/notify/presence", presence, "");
        	i++; 
            try {
            	Thread.sleep(10*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}

}
