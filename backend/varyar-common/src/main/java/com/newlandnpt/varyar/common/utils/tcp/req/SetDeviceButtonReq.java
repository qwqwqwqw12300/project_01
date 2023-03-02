package com.newlandnpt.varyar.common.utils.tcp.req;

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
public class SetDeviceButtonReq extends MessageHead  {

    /**
     * 按键表单
     */
    private List<ButtonPhone> buttonFroms;

    public List<ButtonPhone> getButtonFroms() {
        return buttonFroms;
    }

    public void setButtonFroms(List<ButtonPhone> buttonFroms) {
        this.buttonFroms = buttonFroms;
    }

    public static class ButtonPhone{

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

}
