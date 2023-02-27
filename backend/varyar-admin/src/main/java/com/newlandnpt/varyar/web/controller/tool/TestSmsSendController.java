package com.newlandnpt.varyar.web.controller.tool;

import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.utils.SMS.SMSUtils;
import com.newlandnpt.varyar.common.utils.SMS.templates.DeviceOutLineWarn;
import com.newlandnpt.varyar.common.utils.SMS.templates.FallWarn;
import com.newlandnpt.varyar.common.utils.SMS.templates.FamilyShare;
import com.newlandnpt.varyar.common.utils.SMS.templates.PassOutWarn;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
@RestController
@RequestMapping("/test/sms")
public class TestSmsSendController {


    @PostMapping("/send")
    public  AjaxResult send() {
        String phone ="13774522537";
        String telNumber = phone.replaceAll("(\\d{3})\\d*(\\d{4})","$1*$2");
        System.out.println(telNumber);
        String subPhone =phone.substring(phone.length()-4);
        System.out.println("手机尾号"+subPhone);
        FamilyShare familyShare = new FamilyShare("137*2537","222");
        boolean flag =familyShare.isCorrectParams();
        System.out.println(flag);


        DeviceOutLineWarn deviceOutLineWarn = new DeviceOutLineWarn(subPhone, "雷达波一号");
        FallWarn fallWarn = new FallWarn(subPhone, "雷达波1号");
        PassOutWarn passOutWarn = new PassOutWarn(subPhone, "雷达波1号", "大卧室", "离开", "50");
        if (deviceOutLineWarn.isCorrectParams()) {
            try {
                SMSUtils.sendMsg(phone, deviceOutLineWarn);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return AjaxResult.error("短信内容参数校验失败！");
        }

        if (fallWarn.isCorrectParams()) {
            try {
                SMSUtils.sendMsg(phone, fallWarn);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return AjaxResult.error("短信内容参数校验失败！");
        }

        if (passOutWarn.isCorrectParams()) {
            try {
                SMSUtils.sendMsg(phone, passOutWarn);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return AjaxResult.error("短信内容参数校验失败！");
        }

        return AjaxResult.success();
    }


}
