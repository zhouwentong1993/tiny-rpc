package com.wentong.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.Callable;

import static com.wentong.common.CommonValue.REQUEST_HEAD;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable<Object> {

    // 执行方法传递的参数
    private String param;
    // 执行方法返回结果
    private String result;
    // 上下文对象，在 Channel 建立连接时初始化
    private ChannelHandlerContext context;

    public void setParam(String param) {
        System.out.println("NettyClientHandler.setParam");
        this.param = param;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("NettyClientHandler.channelRead");
        result = (String) msg;
        notifyAll();
        System.out.println("response:[" + result + "]");
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyClientHandler.channelActive111");
        ctx.writeAndFlush(REQUEST_HEAD + "#com.wentong.provider.HelloServiceImpl#sayHello#aaa");
        context = ctx;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("NettyClientHandler.exceptionCaught + "+ cause);
        ctx.close();
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println("NettyClientHandler.close");
    }

    @Override
    public synchronized Object call() throws InterruptedException {
        System.out.println("客户端发送消息");
        context.writeAndFlush(param);
        synchronized (this) {
            while (StringUtils.isEmpty(result)) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
