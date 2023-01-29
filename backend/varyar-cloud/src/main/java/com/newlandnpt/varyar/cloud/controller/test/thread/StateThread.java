package com.newlandnpt.varyar.cloud.controller.test.thread;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.newlandnpt.varyar.cloudBase.utils.HttpClientUtil;
import com.newlandnpt.varyar.common.utils.spring.SpringUtils;

@Service("test.data.thread.stateThread")
public class StateThread extends Thread {
	
	private static final String serverUrl = "127.0.0.1";
	
	@Override
	public void run() {
		String serverPort = SpringUtils.getRequiredProperty("server.port");
		int i = 0;
		com.newlandnpt.varyar.cloudBase.domain.State state = null;
        while (true) {
        	state = new com.newlandnpt.varyar.cloudBase.domain.State();
        	state.setDeviceId("id_QTg6MDM6MkE6NjI6REI6Rkm");
        	state.setTimestamp(new Date());
        	state.setUpTime(System.currentTimeMillis());
        	state.setWifiRssi(-61);
        	HttpClientUtil.sendPost("http://"+serverUrl+":"+serverPort+"/cloud/notify/state", state);
        	i++;
            try {
                Thread.sleep(60*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}

}
