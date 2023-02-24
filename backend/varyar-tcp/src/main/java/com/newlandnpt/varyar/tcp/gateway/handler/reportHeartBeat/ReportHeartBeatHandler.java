package com.newlandnpt.varyar.tcp.gateway.handler.reportHeartBeat;

import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.common.utils.tcp.domain.ReportHeartBeatResponseMqMsgEntity;
import com.newlandnpt.varyar.tcp.base.Response;
import com.newlandnpt.varyar.tcp.gateway.AbstractChannelMessageHandler;
import io.netty.channel.ChannelHandlerContext;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * 上报心跳数据包处理器
 * @author ljx
 * @date 2023/2/23
 */
@Component
public class ReportHeartBeatHandler extends AbstractChannelMessageHandler<ReportHeartBeatReq, Response> {
    private static final Logger log = LoggerFactory.getLogger(ReportHeartBeatHandler.class);
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Value("${rocketmq.topic.reportHeartBeat}")
    private String reportHeartBeatTopic;

    @Override
    public ApiTypes apiType() {
        return ApiTypes.REPORT_HEARTBEAT;
    }

    @Override
    public ReportHeartBeatReq newInstance() {
        return new ReportHeartBeatReq();
    }

    @Override
    public Response handle(ChannelHandlerContext ctx, ReportHeartBeatReq req) {
        ReportHeartBeatResponse reportHeartBeatResponse = new ReportHeartBeatResponse();
        //消息内容包括 设备号、SIM 卡卡号、交易流水号、报文时间、设备电量，步数。
        ReportHeartBeatResponseMqMsgEntity msg = new ReportHeartBeatResponseMqMsgEntity(req.getDeviceNo(),req.getIccid(),req.getTranNo(),req.getMsgTime(),
                req.getCurrentPower(),req.getCurrentSteps());
        // 发出mq消息
        SendResult result = rocketMQTemplate.syncSend(reportHeartBeatTopic, MessageBuilder.withPayload(msg).build());
        if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
            log.error("MQ推送失败：{}", "上报心跳数据包");
        }
        return reportHeartBeatResponse;
    }
}
