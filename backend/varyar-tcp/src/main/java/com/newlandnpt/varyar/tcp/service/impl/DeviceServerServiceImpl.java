package com.newlandnpt.varyar.tcp.service.impl;

import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.tcp.dispose.ChannelMessageDisposer;
import com.newlandnpt.varyar.tcp.dispose.serverInfo.SetServerInfoReq;
import com.newlandnpt.varyar.tcp.dispose.serverInfo.SetServerInfoResponse;
import com.newlandnpt.varyar.tcp.dispose.terminal.SetOperateTerminalReq;
import com.newlandnpt.varyar.tcp.dispose.terminal.SetOperateTerminalResponse;
import com.newlandnpt.varyar.tcp.service.IDeviceServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备按键设置服务实现类
 * @author chenxw1
 * @date 2023/2/23
 */
@Service
public class DeviceServerServiceImpl implements IDeviceServerService {

    @Autowired
    private ChannelMessageDisposer channelMessageDisposer;

    /**
     * 设置服务信息
     * @param req
     * @return
     */
    @Override
    public SetServerInfoResponse setServerInfoResponse(SetServerInfoReq req) {
        return channelMessageDisposer.dispose(ApiTypes.SET_SERVER_INFO, req, SetServerInfoResponse::new);
    }

    /**
     * 远程终端操作
     * @param req
     * @return
     */
    @Override
    public SetOperateTerminalResponse setOperateTerminalResponse(SetOperateTerminalReq req) {
        return channelMessageDisposer.dispose(ApiTypes.REMOTE_OPERATE_TERMINAL, req, SetOperateTerminalResponse::new);
    }
}
