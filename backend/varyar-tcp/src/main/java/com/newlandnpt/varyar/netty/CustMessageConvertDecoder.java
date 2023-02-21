package com.newlandnpt.varyar.netty;

import java.util.List;

import com.newlandnpt.varyar.base.Req;
import com.newlandnpt.varyar.base.login.DeviceLogin;
import com.newlandnpt.varyar.common.utils.StringUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * 自定义解码包，处理收到的每一帧报文
 * @author Bean
 * @date 2023-02-17
 */
public class CustMessageConvertDecoder extends MessageToMessageDecoder<Object> {


	@Override
	protected void decode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
		if(msg == null){
			return ;
		}
		String message = msg.toString();
		if(StringUtils.isBlank(message)){
			return ;
		}
		System.out.println(message);
		String[] arr = message.split(",");
		Req req = null;
		if("DEVICE_LOGIN".equals(arr[3])){
			req  = new DeviceLogin();
			req.setHead(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6]);
			req.handelMessage(arr[7]);
		}
		out.add(req);
	}
}
