package com.wentong.rpc.netty.serialize;

/**
 * 统一的序列化与反序列化接口
 * 新增的序列化需要实现该接口
 */
public interface Serializer <T> {

    /**
     * 获取待序列化对象序列化之后的长度，以便提前分配空间
     * @param t 待序列化对象
     * @return 序列化后长度
     */
    int length(T t);

    /**
     * 序列化对象
     * @param t 待序列化对象
     * @param container byte 容器
     * @param offset 从数组的哪个地方开始
     * @param length 放多少元素
     */
    void serialize(T t, byte[] container, int offset, int length);

    /**
     * 反序列化对象
     * @param src 原始数组
     * @param offset 从何处开始
     * @param length 序列化多少
     * @return 反序列化之后的对象
     */
    T parse(byte[] src, int offset, int length);

    /**
     * 标识序列化算法
     * @return 序列化算法
     */
    byte type();

    /**
     * 返回序列化 class
     * @return class
     */
    Class<T> serializeClass();
}
