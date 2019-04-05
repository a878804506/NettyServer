package com.cyh.netty.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class nettyServerConfig {

    @Bean("bossGroup")
    public EventLoopGroup createBossGroup(){
        return new NioEventLoopGroup();
    }

    @Bean("workerGroup")
    public EventLoopGroup createWorkerGroup(){
        return new NioEventLoopGroup();
    }

    @Scope("prototype")
    @Bean("serverBootstrap")
    public ServerBootstrap createServerBootstrap(){
        return new ServerBootstrap();
    }
}
