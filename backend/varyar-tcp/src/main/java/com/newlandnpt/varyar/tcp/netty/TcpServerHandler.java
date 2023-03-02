package com.newlandnpt.varyar.tcp.netty;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.tcp.base.ChannelMessageHandlers;
import com.newlandnpt.varyar.tcp.base.DeviceChannelCache;
import com.newlandnpt.varyar.tcp.base.Response;
import com.newlandnpt.varyar.tcp.utils.AESUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newlandnpt.varyar.tcp.base.Req;
import com.newlandnpt.varyar.tcp.gateway.handler.login.DeviceLoginReq;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.newlandnpt.varyar.common.constant.CacheConstants.SECOND_4_CYCLE_NUMBER;

public class TcpServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger log = LoggerFactory.getLogger(TcpServerHandler.class);



	// todo 按文档需求 连续两个心跳时长（10 分钟）未接收到设备的心跳或者报文， 则平台认为设备断连，关闭该链接
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			//String str = (String) msg;
			Req req = (Req) msg;
			// todo 设备登录后缓存设备信息及channel，用于后续平台下发请求
			// todo 未登录的请求链接给予关闭避免占用资源
			Response response = ChannelMessageHandlers.handleRequest(ctx, req);
			response.setHeadByRequest(req);
			Channel channel = ctx.channel();

			log.debug(">>>>>>>>>>>>当前连接id:{}",channel.id());
			log.debug(">>>>>>>>>>>>缓存连接id:{}",DeviceChannelCache.getChannelByDeviceNo(req.getDeviceNo()).id());
			//System.out.println(str);
			// 响应消息
			String message = response.generateMessage();
			log.debug(">>>>>> 响应报文：{}",message);
			message = AESUtils.encryptFromString(response.generateMessage(), Mode.CBC, Padding.PKCS5Padding);
			log.debug(">>>>>> 加密响应报文：{}",message+"==#morefun#170");
			channel.writeAndFlush(message+"==#morefun#170");
		} catch (Exception e) {
			log.error("报文解析异常", e);
			log.error("异常报文内容:");
		} finally {
			//释放收到的数据
			ReferenceCountUtil.release(msg);
		}
	}
	
	@Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel chanel = ctx.channel();
		System.out.println("handlerAdded" + chanel.id().asShortText());
		System.out.println("通讯管理机进入：shortId:"+chanel.id().asShortText());
    }
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel chanel = ctx.channel();
		System.out.println("离线:" + chanel.id().asShortText());
		ctx.close();
	}
	
	/***
	 * 发生异常时调用
	 * @param ctx
	 * @param cause
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel channel = ctx.channel();
		String macid = (String) channel.attr(AttributeKey.valueOf("macid")).get();
		log.error("通讯管理机异常，macid="+macid);
		log.error("cause message:"+cause.getMessage(), cause);
		ctx.close();
	}
	
	@Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		super.userEventTriggered(ctx, evt);
		if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            Channel channel = ctx.channel();
            String macid = (String) channel.attr(AttributeKey.valueOf("macid")).get();
            if(StringUtils.isBlank(macid)) {
            		return ;
            }
            if (event.state().equals(IdleState.READER_IDLE)) {
	            	ctx.close();
	            	System.out.println("设备离线");
            }
            log.error("通讯管理机IDLE状态异常 [" + event.state() + "] ，macid="+macid);
        }
    }
	
}
