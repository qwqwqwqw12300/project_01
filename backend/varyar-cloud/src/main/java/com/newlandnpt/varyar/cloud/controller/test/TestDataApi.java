package com.newlandnpt.varyar.cloud.controller.test;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newlandnpt.varyar.cloud.controller.test.thread.FallThread;
import com.newlandnpt.varyar.cloud.controller.test.thread.PresenceThread;
import com.newlandnpt.varyar.cloud.controller.test.thread.StateThread;

/**
 * 开启测试数据推送
 * @author bean
 */
@Controller
@RequestMapping("/cloud/api/test/data")
public class TestDataApi {
    
	private static final Logger log = LoggerFactory.getLogger(TestDataApi.class);
	
	@Resource(name = "test.data.thread.stateThread")
	private StateThread stateThread;
	
	@Resource(name = "test.data.thread.fallThread")
	private FallThread fallThread;
	
	@Resource(name = "test.data.thread.presenceThread")
	private PresenceThread presenceThread;
	
	@PostMapping("/start")
    @ResponseBody
    public void start() {
		if(stateThread.isAlive()){
			stateThread.resume();
			fallThread.resume();
			presenceThread.resume();
		}else {
			stateThread.start();
			fallThread.start();
			presenceThread.start();
		}
    	System.out.println("test ok");
    }
    
	@PostMapping("/stop")
    @ResponseBody
    public void stop() {
		stateThread.suspend();
		fallThread.suspend();
		presenceThread.suspend();
    	System.out.println("test ok");
    }
	
}
