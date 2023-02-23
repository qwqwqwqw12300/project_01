package com.newlandnpt.varyar.tcp.gateway.handler.locationInfo;

import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.common.utils.tcp.domain.LocationInfoResponseMqMsgEntity;
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
 * 位置上报处理器
 * @author ljx
 * @date 2023/2/22
 */
@Component
public class LocationInfoHandler extends AbstractChannelMessageHandler<LocationInfoReq, Response> {
    private static final Logger log = LoggerFactory.getLogger(LocationInfoHandler.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Value("${rocketmq.topic.locationInfo}")
    private String locationInfoTopic;


    @Override
    public ApiTypes apiType() {
        return ApiTypes.REPORT_LOCATION_INFO;
    }

    @Override
    public LocationInfoReq newInstance() {
        return new LocationInfoReq();
    }

    @Override
    public Response handle(ChannelHandlerContext ctx, LocationInfoReq req) {
        LocationInfoResponse locationInfoResponse = new LocationInfoResponse();
        // 设置消息内容，包括 设备号、SIM 卡卡号、交易流水号、报文时间
        //gps位置信息、LBS数据、Wi-Fi数据
        LocationInfoResponseMqMsgEntity msg = new LocationInfoResponseMqMsgEntity(req.getDeviceNo(),req.getIccid(),req.getTranNo(),req.getMsgTime(),
                req.getGpsData(),req.getLbsData(),req.getWifiData());
        // 发出mq消息
        SendResult result = rocketMQTemplate.syncSend(locationInfoTopic, MessageBuilder.withPayload(msg).build());
        if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
            log.error("MQ推送失败：{}", "SOS警告上报");
        }
        return locationInfoResponse;
    }
}
