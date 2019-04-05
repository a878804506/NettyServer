package com.cyh.netty.nettyFileTransferServer;

import com.cyh.netty.util.CommonUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NettyServer {

    @Value("${netty.server.port}")
    private int serverProt;

    @Qualifier("bossGroup")
    @Autowired
    private EventLoopGroup boss;

    @Qualifier("workerGroup")
    @Autowired
    private EventLoopGroup work;

    @Autowired
    private ServerBootstrap serverBootstrap;

    // 添加相应的设置
    @Autowired
    private NettyServerFilter nettyServerFilter;

    public void run() {
        try {
            serverBootstrap.group(boss, work);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(nettyServerFilter); // 设置过滤器
            // 服务器绑定端口监听
            ChannelFuture f = serverBootstrap.bind(serverProt).sync();
            CommonUtil.print("服务端启动成功,端口是:" + serverProt);
            // 监听服务器关闭监听
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭EventLoopGroup，释放掉所有资源包括创建的线程
            work.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
