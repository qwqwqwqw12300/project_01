package com.newlandnpt.varyar.cloud.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    
    @Resource(name = "cloud.fallService")
	private FallService fallService;
    
    @GetMapping("/fall")
    @ResponseBody
    public void fall(@RequestBody Fall fall) {
    	fallService.receve(fall);
    }

}
