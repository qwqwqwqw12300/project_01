package com.newlandnpt.varyar.tcp.gateway.handler.reportHeartBeat;

import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.utils.tcp.domain.DeviceOnlineInfo;
import com.newlandnpt.varyar.common.utils.tcp.domain.ReportHeartBeatResponseMqMsgEntity;
import com.newlandnpt.varyar.tcp.base.Response;
import com.newlandnpt.varyar.tcp.gateway.AbstractChannelMessageHandler;
import io.netty.channel.ChannelHandlerContext;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 上报心跳数据包处理器
 * @author ljx
 * @date 2023/2/23
 */
@Component
public class ReportHeartBeatHandler extends AbstractChannelMessageHandler<ReportHeartBeatReq, Response> {
    private static final Logger log = LoggerFactory.getLogger(ReportHeartBeatHandler.class);

    @Autowired
    private RedisCache redisCache;

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
        //缓存内容包括 设备号、SIM 卡卡号、交易流水号、报文时间、设备电量，步数。
        DeviceOnlineInfo info = new DeviceOnlineInfo();
        DeviceOnlineInfo oldInfo = redisCache.getCacheObject(CacheConstants.DEVICE_ONLINE_INFO+ req.getDeviceNo());
        if(!Objects.isNull(oldInfo)) {
            BeanUtils.copyProperties(oldInfo, info);
        }
        BeanUtils.copyProperties(req,info);
        redisCache.setCacheObject(CacheConstants.DEVICE_ONLINE_INFO+req.getDeviceNo(),info,8, TimeUnit.MINUTES);
        redisCache.setCacheObject(CacheConstants.DEVICE_STATE_KEY+req.getDeviceNo(),req.getDeviceNo(),8,TimeUnit.MINUTES);
        return reportHeartBeatResponse;
    }
}
