package com.wentong.rpc.netty.transport;

import com.wentong.rpc.netty.transport.command.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.concurrent.CompletableFuture;

/**
 * Netty 传输
 */
public class NettyTransport implements Transport {

    private Channel channel;
    private InFlightRequests inFlightRequests;

    public NettyTransport(Channel channel, InFlightRequests inFlightRequests) {
        this.channel = channel;
        this.inFlightRequests = inFlightRequests;
    }

    @Override
    public CompletableFuture<Command> send(Command request) {
        CompletableFuture<Command> completableFuture = new CompletableFuture<>();
        try {
            // 在发送之前将请求放入 flight 里
            inFlightRequests.put(new ResponseFuture(request.getHeader().getRequestId(), completableFuture));

            ChannelFuture channelFuture = channel.writeAndFlush(request);
            channelFuture.addListener(future -> {
                // 如果请求不成功，关闭 channel
                // remove 操作在 onException 做
                if (!future.isSuccess()) {
                    completableFuture.completeExceptionally(channelFuture.cause());
                    channel.close();
                }
            });
        } catch (Throwable e) { // 这里捕获所有异常
            inFlightRequests.remove(new ResponseFuture(request.getHeader().getRequestId(), completableFuture));
            completableFuture.completeExceptionally(e);
        }
        return completableFuture;
    }
}
