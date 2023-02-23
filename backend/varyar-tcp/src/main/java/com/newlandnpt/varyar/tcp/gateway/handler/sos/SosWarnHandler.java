package com.newlandnpt.varyar.tcp.gateway.handler.sos;

import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.common.utils.tcp.domain.SosWarnResponseMqMsgEntity;
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
 * SOS报警处理器
 * @author ljx
 * @date 2023/2/22
 */

@Component
public class SosWarnHandler  extends AbstractChannelMessageHandler<SosWarnReq, Response> {
    private static final Logger log = LoggerFactory.getLogger(SosWarnHandler.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Value("${rocketmq.topic.sosWarn}")
    private String sosWarnTopic;

    @Override
    public ApiTypes apiType() {
        return ApiTypes.REPORT_SOS;
    }

    @Override
    public SosWarnReq newInstance() {
        return new SosWarnReq();
    }

    @Override
    public Response handle(ChannelHandlerContext ctx, SosWarnReq req) {
        SosWarnResponse sosWarnResponse = new SosWarnResponse();
        // 设置消息内容，包括 设备号、SIM卡卡号、交易流水号、报文时间
        SosWarnResponseMqMsgEntity msg = new SosWarnResponseMqMsgEntity(req.getDeviceNo(),req.getIccid(),req.getTranNo(),req.getMsgTime());
        // 发出mq消息
        SendResult result = rocketMQTemplate.syncSend(sosWarnTopic, MessageBuilder.withPayload(msg).build());
        if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
            log.error("MQ推送失败：{}", "SOS警告上报");
        }
        return sosWarnResponse;
    }
}
