package com.newlandnpt.varyar.tcp;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.newlandnpt.varyar.netty.CustMessageConvertDecoder;
import com.newlandnpt.varyar.netty.CustStringDecoder;
import com.newlandnpt.varyar.netty.TcpServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

/**
 * 
 * author : Bean
 * date : 2018-12-04
 */
@Component
public class TcpServer {
	
	private static final Logger log = LoggerFactory.getLogger(TcpServer.class);
	
	public void start(int port) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new IdleStateHandler(300,0,0,TimeUnit.SECONDS));//300秒读超时
					ch.pipeline().addLast(new LineBasedFrameDecoder(5000));
					ch.pipeline().addLast(new CustStringDecoder());
					ch.pipeline().addLast(new CustMessageConvertDecoder());
					ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));//增加解码器
					ch.pipeline().addLast(new TcpServerHandler());
				}
			});
			//服务端将不能处理的客户端连接请求放在队列中等待处理
			b.option(ChannelOption.SO_BACKLOG, 1024);
			b.childOption(ChannelOption.TCP_NODELAY,true);//设置封包,关闭Nagle算法,有数据就马上发送,不需要等到包积累到一定大小在发送
			b.childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} catch (Exception e) {  
            e.printStackTrace();  
        } finally {
			bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
		}
	}
}
