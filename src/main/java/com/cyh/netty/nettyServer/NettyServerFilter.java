package com.cyh.netty.nettyServer;

import com.cyh.netty.entity.NettyFileProtocolDecoder;
import com.cyh.netty.entity.NettyFileProtocolEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
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
        //传输的协议 Protobuf
        ph.addLast(new ProtobufVarint32FrameDecoder());
//        ph.addLast(new ProtobufDecoder(UserMsg.getDefaultInstance()));
        ph.addLast(new ProtobufVarint32LengthFieldPrepender());
        ph.addLast(new ProtobufEncoder());
        //业务逻辑实现类
        ph.addLast("nettyServerHandler", nettyServerHandler);

        // 添加自定义协议的编解码工具
        ph.addLast(new NettyFileProtocolDecoder());
        ph.addLast(new NettyFileProtocolEncoder());
        // 处理网络IO
        ph.addLast(new NettyServerHandler());
    }
}
