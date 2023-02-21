package com.newlandnpt.varyar.netty;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newlandnpt.varyar.base.Req;
import com.newlandnpt.varyar.base.login.DeviceLogin;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;

public class TcpServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger log = LoggerFactory.getLogger(TcpServerHandler.class);
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			//String str = (String) msg;
			Req req = (Req) msg;
			if(req instanceof DeviceLogin){
				System.out.println("设备登录");
				DeviceLogin deviceLogin = (com.newlandnpt.varyar.base.login.DeviceLogin) req;
				System.out.println(deviceLogin.getDeviceNo());
			}
			Channel channel = ctx.channel();
			//System.out.println(str);
			channel.writeAndFlush("服务端return一条数据\r\n");
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
		//System.out.println("离线:" + chanel.id().asShortText());
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
