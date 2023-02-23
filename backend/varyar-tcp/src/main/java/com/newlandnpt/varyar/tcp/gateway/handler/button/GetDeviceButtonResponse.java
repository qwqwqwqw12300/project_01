package com.newlandnpt.varyar.tcp.gateway.handler.button;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Response;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备按键获取响应
 * @author chenxw1
 * @date 2023/2/22
 *
 *报文体格式：
 * 应答报文体：
 * 0=sos按键!按键1=按键1!2=按键2!
 * 0=13900000000!1=13900000001!2=13900000002!
 * 异常应答：0，无设置 1非平台用户2其他异常
 *
 * 报文示例
 *[35380100360174,898600MFSSYYGXXXXXXP,201805082000001000,GET_NORM
 *AL_BUTTON,4,20180528111820,21,0=13900000000!1=13900000001!2=139000000
 *02!]
 *
 */
public class GetDeviceButtonResponse extends MessageHead implements Response {

    /**
     * 按键表单
     */
    private List<buttonPhone> buttonFroms;

    public List<buttonPhone> getButtonFroms() {
        return buttonFroms;
    }

    public void setButtonFroms(List<buttonPhone> buttonFroms) {
        this.buttonFroms = buttonFroms;
    }

    public static class buttonPhone{

        /**
         * 按键
         */
        private String button;

        /**
         * 手机号
         */
        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getButton() {
            return button;
        }

        public void setButton(String button) {
            this.button = button;
        }

        public String generateMessage(){
            return Arrays.asList(button,phone).stream().collect(Collectors.joining("="));
        }
    }
    @Override
    public List<String> getResponses() {
        return Arrays.asList(
                CollectionUtils.isEmpty(buttonFroms)?"0":buttonFroms.stream().map(p->p.generateMessage()).collect(Collectors.joining("!","","!"))
        );
    }
}
