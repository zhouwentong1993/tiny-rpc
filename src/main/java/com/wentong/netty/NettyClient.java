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

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.wentong.common.CommonValue.REQUEST_HEAD;

public class NettyClient {

    private static final ExecutorService SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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
        try (NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup()) {
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
            ChannelFuture sync = bootstrap.connect(host, port).sync();
            sync.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

