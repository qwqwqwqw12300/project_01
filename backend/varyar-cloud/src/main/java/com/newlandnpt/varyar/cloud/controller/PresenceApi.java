package com.newlandnpt.varyar.cloud.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newlandnpt.varyar.cloudBase.domain.Presence;
import com.newlandnpt.varyar.cloudBase.service.PresenceService;

/**
 * 进出区域通知
 * @author bean
 */
@Controller
@RequestMapping("/cloud/notify")
public class PresenceApi {
	
    @Resource(name = "cloud.presenceService")
	private PresenceService presenceService;

    @GetMapping("/presence")
    @ResponseBody
    public void presence(@RequestBody Presence presence) {
    	presenceService.receve(presence);
    }

}
