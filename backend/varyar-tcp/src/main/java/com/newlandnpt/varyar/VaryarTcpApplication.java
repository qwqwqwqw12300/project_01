package com.newlandnpt.varyar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import com.newlandnpt.varyar.tcp.TcpServer;

/**
 * 启动程序
 * 
 * @author newlandnpt
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class VaryarTcpApplication implements CommandLineRunner {
	
	@Autowired
	private TcpServer tcpServer;
	
	public static void main(String[] args) {
		// System.setProperty("spring.devtools.restart.enabled", "false");
		SpringApplication.run(VaryarTcpApplication.class, args);
		System.out.println("(♥◠‿◠)ﾉﾞ  varyarCloud启动成功   ლ(´ڡ`ლ)ﾞ  \n");
	}

	@Override
	public void run(String... args) throws Exception {
		tcpServer.start(5569);
	}
}
