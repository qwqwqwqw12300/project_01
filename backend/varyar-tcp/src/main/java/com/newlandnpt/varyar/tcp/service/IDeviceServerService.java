package com.newlandnpt.varyar.tcp.service;

import com.newlandnpt.varyar.tcp.dispose.serverInfo.SetServerInfoReq;
import com.newlandnpt.varyar.tcp.dispose.serverInfo.SetServerInfoResponse;
import com.newlandnpt.varyar.tcp.dispose.terminal.SetOperateTerminalReq;
import com.newlandnpt.varyar.tcp.dispose.terminal.SetOperateTerminalResponse;

/**
 * 设备服务设置服务接口
 * @author chenxw1
 * @date 2023/2/23
 */
public interface IDeviceServerService {


    /**
     * 设置服务信息
     * @param req
     * @return
     */
    SetServerInfoResponse setServerInfoResponse(SetServerInfoReq req);

    /**
     * 远程终端操作
     * @param req
     * @return
     */
     SetOperateTerminalResponse setOperateTerminalResponse(SetOperateTerminalReq req);

}
