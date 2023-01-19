package com.newlandnpt.varyar.cloud.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newlandnpt.varyar.cloudBase.domain.Fall;
import com.newlandnpt.varyar.cloudBase.service.FallService;

/**
 * 进出区域通知
 * @author bean
 */
@Controller
@RequestMapping("/cloud/notify")
public class FallApi {
    
	private static final Logger log = LoggerFactory.getLogger(FallApi.class);
	
    @Resource(name = "cloud.fallService")
	private FallService fallService;
    
    @PostMapping("/fall")
    @ResponseBody
    public void fall(@RequestBody Fall fall) {
    	log.info("收到摔倒事件报文:{}", fall);
    	fallService.receve(fall);
    }
    
}
