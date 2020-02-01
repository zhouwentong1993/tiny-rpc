package com.wentong.rpc.netty.transport;

import com.wentong.rpc.netty.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * 网络请求抽象类
 */
public interface Transport {

    /**
     * 发送请求
     * @param request 具体请求
     * @return 响应的 CompletableFuture 格式
     */
    CompletableFuture<Command> send(Command request);

}
