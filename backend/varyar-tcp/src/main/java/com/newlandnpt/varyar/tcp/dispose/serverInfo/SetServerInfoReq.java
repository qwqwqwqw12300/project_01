package com.newlandnpt.varyar.tcp.dispose.serverInfo;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设置服务信息
 * @author chenxw1
 * @date 2023/2/23
 * 报文体格式：
 * 平台服务地址URL@端口号
 *
 * [35380100360174,898600MFSSYYGXXXXXXP,201805082000001000,SET_SERVE
 * R_INFO,1,20180528111820,36,www.xxt.gz.chinamobile.com@80]
 */
public class SetServerInfoReq extends MessageHead implements Req {

    /**
     * 平台服务地址URL
     */
    private String serverUrl;

    /**
     * 端口号
     */
    private String serverPost;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getServerPost() {
        return serverPost;
    }

    public void setServerPost(String serverPost) {
        this.serverPost = serverPost;
    }

    public SetServerInfoReq() {
        setApiType("SET_SERVER_INFO");
        setMsgType("1");
    }

    @Override
    public List<String> getRequests() {
        return Arrays.asList(
                serverUrl,serverPost
        );
    }

}
