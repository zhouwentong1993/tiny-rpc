package com.wentong.utils;

import com.wentong.common.ClassStructure;
import io.netty.buffer.ByteBuf;

import java.util.Objects;
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

    public static ClassStructure parseMessage(String message) {
        Objects.requireNonNull(message);
        String[] split = message.split("#");
        return new ClassStructure(split[1], split[2], split[3]);
    }

}
