package com.newlandnpt.varyar.tcp.netty;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.newlandnpt.varyar.common.utils.StringUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义数据包格式，解决拆包粘包
 * author : Bean
 * date : 2018-12-18
 * 描述：已经测试过如下报文格式
[123,456]

[123,456][789]

[123,
456]

[123,456][78
9]

[123,
456][789]

[123
456
789]

[123
456
][78
9]

[123
456
789
][10,11
12]
 */
public class CustStringDecoder extends StringDecoder {

	private String tempMsg = "";

	private static final Logger log = LoggerFactory.getLogger(TcpServerHandler.class);
	 @Override
	 protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		 int index = in.readerIndex();
		 //当前读到数据
		 int msgLen = in.readableBytes();
		 //System.out.println("当前index:" + index + ",长度:" + msgLen);
		 String msg = in.toString(CharsetUtil.UTF_8);
		 log.debug(">>>>> 接收到字符：{}",msg);
		 if(StringUtils.isBlank(msg)){
			 return ;
		 }
		 String first = StringUtils.substring(msg, 0, 1);
		 String end = StringUtils.substring(msg, msg.length()-1, msg.length());
		 if(!"[".equals(first) || !"]".equals(end)){
			 if(StringUtils.isBlank(tempMsg)){
				 tempMsg = msg;
				 return ;
			 }else {
				 if("[".equals(first)){
					 msg = msg + tempMsg;
				 }else if("]".equals(end)){
					 msg = tempMsg + msg;
				 } else {
					 tempMsg = tempMsg + msg;
					 return ;
				 }
			 }
		 }
		 Pattern p = Pattern.compile("\\[(.*?)\\]");
		 Matcher m = p.matcher(msg);
		 while(m.find()){
			 String outStr = m.group().substring(1, m.group().length()-1);
			 out.add(outStr);
		 }
		 tempMsg = "";
	 }
}
