package com.wentong.netty;

import com.wentong.codec.MessageDecoder;
import com.wentong.codec.MessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.wentong.common.CommonValue.REQUEST_HEAD;

public class NettyClient {

    private static final ExecutorService SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    // 同服务器端建立链接重试次数
    private static final AtomicInteger RETRY_COUNT = new AtomicInteger(1);

    // 同服务器端最多建立链接次数
    private static final int MAX_RETRY_COUNT = 3;

    private NettyClientHandler nettyClientHandler;

    public <T> T getBean(Class<T> clazz, String host, int port) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            try {

                if (nettyClientHandler == null) {
                    init(host, port);
                }
                nettyClientHandler.setParam(REQUEST_HEAD + "#HelloService#hello#" + args[0]);
                return (SERVICE.submit(nettyClientHandler).get());
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }


    public void init(String host, int port) {
        nettyClientHandler = new NettyClientHandler();
        try {
            Bootstrap bootstrap = newBootstrap();
            connectToServer(host, port, bootstrap, RETRY_COUNT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bootstrap newBootstrap() {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new MessageDecoder())
                                .addLast(new MessageEncoder())
                                .addLast(new NettyClientHandler());
                    }
                });
        return bootstrap;
    }

    private void connectToServer(String host, int port, Bootstrap bootstrap, AtomicInteger count) throws InterruptedException {
        System.out.println("NettyClient.connectToServer");
        if (count.intValue() <= MAX_RETRY_COUNT) {
            try {
                ChannelFuture connect = bootstrap.connect(host, port);
                connect.addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if (!future.isSuccess()) {
                            count.getAndIncrement();
                            connectToServer(host, port, newBootstrap(), count);
                        }
                    }
                });
                ChannelFuture sync = connect.sync();
                sync.channel().closeFuture().sync();
            } catch (Throwable e) {
                connectToServer(host, port, newBootstrap(), count);
            }

        } else {
            System.out.println("重试超过最大次数：" + MAX_RETRY_COUNT + " 退出！");
            System.exit(1);
        }
    }

}

