package com.newlandnpt.varyar.cloud.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.newlandnpt.varyar.cloudBase.domain.State;
import com.newlandnpt.varyar.cloudBase.service.StateService;

/**
 * 设备状态通知
 * @author bean
 */
@Controller
@RequestMapping("/cloud/notify")
public class StateApi {
	
	private static final Logger log = LoggerFactory.getLogger(StateApi.class);
	
    @Resource(name = "cloud.stateService")
	private StateService stateService;

    @PostMapping("/state")
    @ResponseBody
    public void state(@RequestBody State state) {
    	log.info("收到设备状态消息报文:{}", JSON.toJSONString(state));
    	stateService.receve(state);
    }

}
