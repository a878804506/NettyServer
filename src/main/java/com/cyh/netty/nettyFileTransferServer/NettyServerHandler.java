package com.cyh.netty.nettyFileTransferServer;

import com.cyh.netty.entity.fileTransfer.NettyFileProtocol;
import com.cyh.netty.util.ByteToFileUtil;
import com.cyh.netty.util.CommonUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /** 空闲次数 */
    private int idle_count = 0;

    @Value("${nginx.staticMessageFilePath}")
    private String staticMessageFilePath ;

    /**
     * 建立连接时，发送一条消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        CommonUtil.print("连接的客户端地址:" + ctx.channel().remoteAddress());
        idle_count = 1;
        super.channelActive(ctx);
    }

    /**
     * 超时处理 如果5秒没有接受客户端的心跳，就触发; 如果超过两次，则直接关闭;
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            if (IdleState.READER_IDLE.equals(event.state())) { // 如果读通道处于空闲状态，说明没有接收到心跳命令
                if (idle_count >= 3) {
                    CommonUtil.print("关闭这个不活跃的channel！");
                    ctx.channel().close();
                }
                idle_count++;
            }
        } else {
            super.userEventTriggered(ctx, obj);
        }
    }

    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if(msg instanceof NettyFileProtocol) {
                NettyFileProtocol nfp = (NettyFileProtocol) msg;
                switch (nfp.getContentType()){
                    case -1: //客户端发送过来的心跳包
                        idle_count = 1; // 超时机制重置
                        break;
                    case 2 : // 客户端发送过来的图片
                        ByteToFileUtil.byteToFile(nfp.getContent(),
                                staticMessageFilePath+nfp.getFronUserId()+"_"+nfp.getToUserId()+"_"+
                                nfp.getTime()+"_"+nfp.getUuNum()+"."+nfp.getFileExt());
                        break;
                    case 3 : // 客户端发送过来的文件

                        break;
                }
            }else{
                CommonUtil.print("未知数据!丢弃！" );
            }
//            ctx.channel().writeAndFlush(new NettyFileProtocol(3,3,3,3,3,new byte[1024*1024*20]));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
