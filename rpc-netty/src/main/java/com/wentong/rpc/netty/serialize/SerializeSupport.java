package com.wentong.rpc.netty.serialize;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 对外暴露的序列化&反序列化类
 */
public class SerializeSupport {

    // type -> class 的映射
    private static Map<Byte, Class<?>> typeMap = new HashMap<>();

    // Class -> 具体 Serializer 映射
    private static Map<Class<?>, Serializer> serializerMap = new HashMap<>();

    public static <E> E parse(byte[] data) {
        Objects.requireNonNull(data);
        if (data.length < 2) {
            return null;
        }
        // 获得 type
        byte type = data[0];
        // 通过 type 映射到 class
        Class<?> clazz = typeMap.get(type);
        // 进而找到对应的序列化算法
        Serializer serializer = serializerMap.get(clazz);
        Objects.requireNonNull(serializer);
        // 从 1 开始反序列化，因为第一个元素保存的是 type。
        Object entry = serializer.parse(data, 1, data.length - 1);
        if (clazz.isAssignableFrom(entry.getClass())) {
            return (E) entry;
        } else {
            throw new SerializeException("无法反序列化");
        }
    }

    public static <E> byte[] serialize(E entry) {
        Class<?> aClass = entry.getClass();
        Serializer serializer = serializerMap.get(aClass);
        Objects.requireNonNull(serializer);
        int length = serializer.length(entry);
        // +1 是因为第 0 个元素要保存 type
        byte[] container = new byte[length + 1];
        container[0] = serializer.type();
        serializer.serialize(entry, container, 1, length - 1);
        return container;
    }

}
