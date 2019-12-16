package com.wentong.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 对于每个需要被解码为另一种格式的入站消息来说，
 * 该方法都 将会被调用。解码消息随后会被传递给 ChannelPipeline 中的下一个 ChannelInboundHandler
 */
public class MessageEncoder extends MessageToByteEncoder<Request> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Request msg, ByteBuf out) throws Exception {
        write(msg.getType(), out);
        write(msg.getRequestId(), out);
        write(msg.getPayload(), out);
    }

    private void write(String something, ByteBuf byteBuf) {
        byteBuf.writeInt(something.getBytes().length);
        byteBuf.writeBytes(something.getBytes());
    }

}
