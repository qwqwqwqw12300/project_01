package com.newlandnpt.varyar.tcp.netty;

import java.util.List;

import com.newlandnpt.varyar.tcp.base.ChannelMessageHandlers;
import com.newlandnpt.varyar.tcp.base.Req;
import com.newlandnpt.varyar.common.utils.StringUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义解码包，处理收到的每一帧报文
 * @author Bean
 * @date 2023-02-17
 */
public class CustMessageConvertDecoder extends MessageToMessageDecoder<Object> {

	private static final Logger log = LoggerFactory.getLogger(CustMessageConvertDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
		if(msg == null){
			return ;
		}
		String message = msg.toString();
		if(StringUtils.isBlank(message)){
			return ;
		}
		Req req = ChannelMessageHandlers.constructFromMessage(message);
		if(req == null){
			log.error(">>>>>> 接受到无法处理的消息tcp链接将被平台关闭请排查，原因：【消息唯一标识不识别】，消息内容：{}",message);
			ctx.channel().close();
		}
		out.add(req);
	}


}
