package com.newlandnpt.varyar.tcp.gateway.handler.login;

import com.alibaba.fastjson.JSON;
import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.common.utils.tcp.domain.DeviceLoginMqMsgEntity;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.service.IDeviceService;
import com.newlandnpt.varyar.tcp.base.DeviceChannelCache;
import com.newlandnpt.varyar.tcp.base.Response;
import com.newlandnpt.varyar.tcp.gateway.AbstractChannelMessageHandler;
import com.newlandnpt.varyar.tcp.gateway.handler.locationInfo.LocationInfoHandler;
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
 * 设备登录处理器
 * @author lin.ju
 * @date 2023/2/22
 */
@Component
public class DeviceLoginHandler extends AbstractChannelMessageHandler<DeviceLoginReq, Response> {

    private static final Logger log = LoggerFactory.getLogger(DeviceLoginHandler.class);

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Value("${rocketmq.topic.deviceLogin}")
    private String deviceLogin;

    @Override
    public ApiTypes apiType() {
        return ApiTypes.DEVICE_LOGIN;
    }

    @Override
    public DeviceLoginReq newInstance() {
        return new DeviceLoginReq();
    }

    @Override
    public Response handle(ChannelHandlerContext ctx, DeviceLoginReq req) {
        DeviceLoginResponse deviceLoginResponse = new DeviceLoginResponse();
        log.debug("设备【{}】,登录平台，设备信息：{}",req.getDeviceNo(), JSON.toJSONString(req));
        TDevice device = deviceService.selectByDeviceNo(deviceLoginResponse.getDeviceNo());
        if(device == null){
            //设备为空返回 状态 1：非平台用户
            deviceLoginResponse.setStatus("1");
            log.debug("设备【{} 不存在，登录失败",req.getDeviceNo());
            return deviceLoginResponse;
        }
        DeviceChannelCache.setCache(req.getDeviceNo(),req.getIccid(),
                req.getMsgTime(),ctx.channel());

        DeviceLoginMqMsgEntity entity = new DeviceLoginMqMsgEntity(req.getNormalKeyNum(),
                req.getSosKey(),req.getDeviceType(),req.getSupportAreaAlert(),req.getSupportCallIn(),
                req.getProtocolVersion());
        entity.setDeviceNo(req.getDeviceNo());
        SendResult result = rocketMQTemplate.syncSend(deviceLogin, MessageBuilder.withPayload(entity)
                .setHeader("KEYS",req.getDeviceNo()).build());
        if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
            log.error("MQ推送失败：{}", "SOS警告上报");
        }
        return deviceLoginResponse;
    }
}
