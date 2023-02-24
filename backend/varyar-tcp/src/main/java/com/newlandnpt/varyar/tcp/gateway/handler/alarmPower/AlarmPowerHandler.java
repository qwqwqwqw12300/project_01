package com.newlandnpt.varyar.tcp.gateway.handler.alarmPower;

import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.common.utils.tcp.domain.AlarmPowerResponseMqMsgEntity;
import com.newlandnpt.varyar.tcp.base.Response;
import com.newlandnpt.varyar.tcp.gateway.AbstractChannelMessageHandler;
import com.newlandnpt.varyar.tcp.gateway.handler.reportHeartBeat.ReportHeartBeatHandler;
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
 * 缺电报警、关机报警处理器
 * @author ljx
 * @date 2023/2/23
 */
@Component
public class AlarmPowerHandler extends AbstractChannelMessageHandler<AlarmPowerReq, Response> {
    private static final Logger log = LoggerFactory.getLogger(ReportHeartBeatHandler.class);
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Value("${rocketmq.topic.alarmPower}")
    private String alarmPowerTopic;
    @Override
    public ApiTypes apiType() {
        return ApiTypes.ALARM_POWER;
    }

    @Override
    public AlarmPowerReq newInstance() {
        return new AlarmPowerReq();
    }

    @Override
    public Response handle(ChannelHandlerContext ctx, AlarmPowerReq req) {
        AlarmPowerResponse alarmPowerResponse = new AlarmPowerResponse();

        AlarmPowerResponseMqMsgEntity msg  = new AlarmPowerResponseMqMsgEntity(req.getDeviceNo(),req.getIccid(),req.getTranNo(),req.getMsgTime(),req.getReportType(),req.getRemainPower());
        // 发出mq消息
        SendResult result = rocketMQTemplate.syncSend(alarmPowerTopic, MessageBuilder.withPayload(msg).build());
        if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
            log.error("MQ推送失败：{}", "缺电报警、关机报警");
        }
        return alarmPowerResponse;
    }
}
