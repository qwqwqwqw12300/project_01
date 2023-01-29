package com.newlandnpt.varyar.cloud.controller.test.thread;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.newlandnpt.varyar.cloudBase.domain.Fall;
import com.newlandnpt.varyar.cloudBase.utils.HttpClientUtil;
import com.newlandnpt.varyar.common.utils.spring.SpringUtils;
import com.newlandnpt.varyar.common.utils.uuid.IdUtils;

@Service("test.data.thread.fallThread")
public class FallThread extends Thread {

	private static final String serverUrl = "127.0.0.1";
	
	@Override
	public void run() {
		String serverPort = SpringUtils.getRequiredProperty("server.port");
		int i = 0;
		Fall fall = null;
        while (true) {
        	fall = new Fall();
        	fall.setTimestamp(new Date());
        	fall.setDeviceId("id_QTg6MDM6MkE6NjI6REI6Rkm");
        	fall.setEventId(IdUtils.simpleUUID());
        	fall.setEndTimestamp(new Date());
        	fall.setStatus("fall_confirmed");
        	fall.setType("fall");
        	fall.setIsSimulated(false);
        	fall.setStatusUpdateTimestamp(new Date());
        	fall.setExtra("{\"targetId\":0,\"x_cm\":-94,\"y_cm\":175,\"z_cm\":19}");        	
        	fall.setIsLearning(false);
        	fall.setIsSilent(false);
        	fall.setTimestampMillis(System.currentTimeMillis());
        	HttpClientUtil.sendPost("http://"+serverUrl+":"+serverPort+"/cloud/notify/fall", fall);
        	i++;
            try {
            	Thread.sleep(30*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}

}
