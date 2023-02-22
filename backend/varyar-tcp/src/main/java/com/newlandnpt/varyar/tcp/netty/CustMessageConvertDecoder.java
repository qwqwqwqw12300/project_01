package com.newlandnpt.varyar.tcp.netty;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.tcp.base.ChannelMessageHandlers;
import com.newlandnpt.varyar.tcp.base.Req;
import com.newlandnpt.varyar.common.utils.StringUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import static com.newlandnpt.varyar.common.constant.CacheConstants.SECOND_4_CYCLE_NUMBER;
import static com.newlandnpt.varyar.common.constant.CacheConstants.TCP_WATCH_TRADE_NO;

/**
 * 自定义解码包，处理收到的每一帧报文
 * @author Bean
 * @date 2023-02-17
 */
public class CustMessageConvertDecoder extends MessageToMessageDecoder<Object> {

	private static final Logger log = LoggerFactory.getLogger(CustMessageConvertDecoder.class);

	private static RedisCache redisCache;

	@Override
	protected void decode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
		if(msg == null){
			return ;
		}
		String message = msg.toString();
		if(StringUtils.isBlank(message)){
			return ;
		}

		//todo 将设备上报的和平台下发设备响应的区分开
		// 默认3秒超时
		String[] arr = message.split(",");
		if(arr.length>=3&&redisCache.hasKey(TCP_WATCH_TRADE_NO+arr[2])){
			redisCache.setCacheObject(TCP_WATCH_TRADE_NO+arr[2],message,3, TimeUnit.SECONDS);
			return;
		}

		Req req = ChannelMessageHandlers.constructFromMessage(message);
		if(req == null){
			log.error(">>>>>> 接受到无法处理的消息tcp链接将被平台关闭请排查，原因：【消息唯一标识不识别】，消息内容：{}",message);
			ctx.channel().close();
		}
		out.add(req);
	}



	@Component
	public static class CustMessageConvertDecoderInjector implements BeanPostProcessor {

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

			if(bean instanceof RedisCache){
				redisCache = (RedisCache) bean;
			}

			return bean;
		}
	}


}
