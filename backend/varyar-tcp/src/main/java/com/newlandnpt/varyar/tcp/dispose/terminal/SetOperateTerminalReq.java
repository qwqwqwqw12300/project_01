package com.newlandnpt.varyar.tcp.dispose.terminal;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;

import java.util.Arrays;
import java.util.List;

/**
 * 远程终端操作
 * @author chenxw1
 * @date 2023/2/23
 * 报文体格式：
 * 重启终端@恢复除平台地址以外的出厂设置
 * 字段说明: 重启终端: 0=不执行，1=执行
 * 恢复除平台地址以外的出厂设置: 0=不执行，1=执行
 *
 * [35380100360174,898600MFSSYYGXXXXXXP,201805082000001000,REMOTE_OP
 * ERATE_TERMINAL,1,20180528111820,3,1@1]
 */
public class SetOperateTerminalReq extends MessageHead implements Req {

    /**
     * 重启终端: 0=不执行，1=执行
     */
    private String restart ="1";

    /**
     * 恢复除平台地址以外的出厂设置: 0=不执行，1=执行
     */
    private String initialization  ="0";


    public String getRestart() {
        return restart;
    }

    public void setRestart(String restart) {
        this.restart = restart;
    }

    public String getInitialization() {
        return initialization;
    }

    public void setInitialization(String initialization) {
        this.initialization = initialization;
    }

    public SetOperateTerminalReq() {
        setApiType("REMOTE_OPERATE_TERMINAL");
        setMsgType("1");
    }

    @Override
    public List<String> getRequests() {
        return Arrays.asList(
                restart,initialization
        );
    }

}
