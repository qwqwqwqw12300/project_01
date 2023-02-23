package com.newlandnpt.varyar.tcp.dispose;

import com.newlandnpt.varyar.common.constant.tcp.ApiTypes;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.tcp.base.DeviceChannelCache;
import com.newlandnpt.varyar.tcp.base.Req;
import com.newlandnpt.varyar.tcp.base.Response;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.newlandnpt.varyar.common.constant.CacheConstants.SECOND_4_CYCLE_NUMBER;
import static com.newlandnpt.varyar.common.constant.CacheConstants.TCP_WATCH_TRADE_NO;

/**
 * @author lin.ju
 * @date 2023/2/22
 */
@Component
public class ChannelMessageDisposer{

    @Autowired
    private RedisCache redisCache;

    /**
     * 分发消息
     *
     * @param apiType
     * @param req
     * @return
     */
    public <T extends Response> T dispose(ApiTypes apiType, Req req, Supplier<T> responseSupplier){
        Channel channel = DeviceChannelCache.getChannelByDeviceNo(req.getDeviceNo());
        if(channel == null){
            throw new NullPointerException("设备未连接");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = sdf.format(new Date());
        req.setIccid(DeviceChannelCache.getIccIdByDeviceNo(req.getDeviceNo()));
        req.setApiType(apiType.name());
        req.setMsgType(apiType.getReqMsgType());
        req.setTranNo(time+redisCache.getAtomicIncrementLongWithDecimalFormat(SECOND_4_CYCLE_NUMBER+time,1,4));
        req.setMsgTime(time);
        T response = responseSupplier.get();
        // 默认3秒超时
        redisCache.setCacheObject(TCP_WATCH_TRADE_NO+req.getTranNo(),null,3, TimeUnit.MINUTES);
        channel.writeAndFlush(req.generateMessage());
        String message = null;
        while (redisCache.hasKey(TCP_WATCH_TRADE_NO + req.getTranNo())) {
            message = redisCache.getCacheObject(TCP_WATCH_TRADE_NO + req.getTranNo());
            if(message!=null){
                break;
            }
        }

        if (message == null) {
            throw new ServiceException("交易号：" + req.getTranNo() + " 超时未响应");
        }

        String[] arr = message.split(",");
        response.setHead(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6]);
        response.handleMessage(arr[7]);

        return response;
    }
}
