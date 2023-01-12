package com.newlandnpt.varyar.cloud.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newlandnpt.varyar.cloudBase.domain.State;
import com.newlandnpt.varyar.cloudBase.service.StateService;

/**
 * 设备状态通知
 * @author bean
 */
@Controller
@RequestMapping("/cloud/notify")
public class StateApi {
	
    @Resource(name = "cloud.stateService")
	private StateService stateService;

    @GetMapping("/state")
    @ResponseBody
    public void state(@RequestBody State state) {
    	stateService.receve(state);
    }

}
