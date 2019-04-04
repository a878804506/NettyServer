package com.cyh.netty;

import com.cyh.netty.nettyFileTransferServer.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NettyServerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(NettyServerApplication.class, args);
		NettyServer bean = applicationContext.getBean(NettyServer.class);
		bean.run();
	}
}
