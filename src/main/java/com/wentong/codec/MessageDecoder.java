package com.wentong.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;


/**
 * 这是你必须实现的唯一抽象方法。
 * decode() 方法被调用时将会传 入一个包含了传入数据的 ByteBuf ，
 * 以及一个用来添加解码消息 的 List 。对这个方法的调用将会重复进行，
 * 直到确定没有新的元 素被添加到该 List ，或者该 ByteBuf 中没有更多可读取的字节 时为止。
 * 然后，如果该 List 不为空，那么它的内容将会被传递给 ChannelPipeline 中的下一个 ChannelInboundHandler
 */
public class MessageDecoder extends ByteToMessageDecoder{
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() >= 0 && in.readableBytes() >= 100000) {
            readSomething(in, out);
            readSomething(in, out);
            readSomething(in, out);
        } else {
            throw new TooLongFrameException("Frame too big!");
        }
    }

    private void readSomething(ByteBuf in, List<Object> out) {
        int size = in.readInt();
        ByteBuf buf = in.readBytes(size);
        out.add(new String(buf.array()));
    }
}
