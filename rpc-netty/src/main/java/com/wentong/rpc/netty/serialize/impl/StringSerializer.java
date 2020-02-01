package com.wentong.rpc.netty.serialize.impl;

import com.wentong.rpc.netty.serialize.Serializer;

import java.nio.charset.StandardCharsets;

import static com.wentong.rpc.netty.serialize.impl.SerializerType.STRING_SERIALIZE;

/**
 * 字符串序列化
 */
public class StringSerializer implements Serializer<String> {

    @Override
    public int length(String s) {
        return s.getBytes().length;
    }

    @Override
    public void serialize(String s, byte[] container, int offset, int length) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(bytes, 0, container, offset, bytes.length);
    }

    @Override
    public String parse(byte[] src, int offset, int length) {
        return new String(src, offset, length, StandardCharsets.UTF_8);
    }

    @Override
    public byte type() {
        return STRING_SERIALIZE;
    }

    @Override
    public Class<String> serializeClass() {
        return String.class;
    }
}
