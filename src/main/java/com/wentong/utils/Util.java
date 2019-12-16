package com.wentong.utils;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class Util {

    private Util() {

    }

    public static String readByteBufIntoString(ByteBuf msg) {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        return new String(bytes);
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

}
