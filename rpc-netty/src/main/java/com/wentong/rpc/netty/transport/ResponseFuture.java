package com.wentong.rpc.netty.transport;

import com.wentong.rpc.netty.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * 响应异步对象
 */
public class ResponseFuture {

    // 请求的唯一标识
    private final int requestId;
    // 异步的响应对象
    private final CompletableFuture<Command> commandCompletableFuture;
    // 请求时间戳，纳秒
    private final long timestamp;

    public ResponseFuture(int requestId, CompletableFuture<Command> commandCompletableFuture) {
        this.requestId = requestId;
        this.commandCompletableFuture = commandCompletableFuture;
        timestamp = System.nanoTime();
    }

    public int getRequestId() {
        return requestId;
    }

    public CompletableFuture<Command> getCommandCompletableFuture() {
        return commandCompletableFuture;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
