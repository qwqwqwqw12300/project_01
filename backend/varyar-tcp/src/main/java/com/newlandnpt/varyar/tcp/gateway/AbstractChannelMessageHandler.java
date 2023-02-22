package com.newlandnpt.varyar.tcp.gateway;

import com.newlandnpt.varyar.tcp.base.Req;
import com.newlandnpt.varyar.tcp.base.Response;
import io.netty.channel.ChannelHandlerContext;

/**
 * 抽象的通道消息处理器
 * @author lin.ju
 * @date 2023/2/22
 */
public abstract class AbstractChannelMessageHandler<T extends Req,E extends Response> {

    /**
     * 消息标识
     * @return
     */
    public abstract String messageIdentify();

    /**
     * 构建消息实例
     * @return
     */
    public abstract T newInstance();

    /**
     * 处理消息
     *
     * @param ctx
     * @param req
     * @return
     */
    public abstract E handle(ChannelHandlerContext ctx, T req);

}
