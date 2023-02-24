package com.newlandnpt.varyar.tcp.dispose.button;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;
import com.newlandnpt.varyar.tcp.gateway.handler.button.GetDeviceButtonResponse;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设置普通按键与 SOS 按键
 * @author chenxw1
 * @date 2023/2/23
 * 报文体格式：
 * 终端键值对应的拨打号码(1=拨打号码; 0表示SOS键;
 * 如0=13900000000!1=13900000001!2=13900000002!)
 */
public class SetDeviceButtonReq extends MessageHead implements Req {

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
    public SetDeviceButtonReq() {
        setApiType("SET_NORMAL_BUTTON");
        setMsgType("1");
    }

    @Override
    public List<String> getRequests() {
        return Arrays.asList(
                CollectionUtils.isEmpty(buttonFroms)?"":buttonFroms.stream().map(p->p.generateMessage()).collect(Collectors.joining("!","","!"))
                );
    }

}
