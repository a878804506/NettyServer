package com.cyh.netty.nettyFileTransferServer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 网络事件处理器
 */
@Component
public class NettyServerFilter extends ChannelInitializer<SocketChannel> {

    @Autowired
    private NettyServerHandler nettyServerHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline ph = ch.pipeline();

        //入参说明: 读超时时间、写超时时间、所有类型的超时时间、时间格式
        ph.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
        // 解码和编码，应和客户端一致

        // 添加自定义协议的编解码工具
        ph.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
        ph.addLast(new ObjectEncoder());

        //业务逻辑实现类
        ph.addLast("nettyServerHandler", nettyServerHandler);
    }
}
