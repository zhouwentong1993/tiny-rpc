package com.wentong.rpc.netty.serialize.impl;

import com.wentong.rpc.netty.client.stubs.RpcRequest;
import com.wentong.rpc.netty.serialize.Serializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static com.wentong.rpc.netty.serialize.impl.SerializerType.RPC_SERIALIZE;

/**
 * PRC 请求序列化与反序列化工具类
 * 字段顺序按照 interfaceName、methodName、methodArgument 排列，每一个字段前面都一个 int 类型变量，标志其 length。
 */
public class RpcRequestSerializer implements Serializer<RpcRequest> {

    @Override
    public int length(RpcRequest rpcRequest) {
        return Integer.BYTES + rpcRequest.getInterfaceName().getBytes(StandardCharsets.UTF_8).length
                + Integer.BYTES + rpcRequest.getMethodName().getBytes(StandardCharsets.UTF_8).length
                + Integer.BYTES + rpcRequest.getMethodArgument().length;
    }

    @Override
    public void serialize(RpcRequest rpcRequest, byte[] container, int offset, int length) {
        // 通过 ByteBuffer 对 byte[] 操作。
        ByteBuffer byteBuffer = ByteBuffer.wrap(container, offset, length);
        // 为代码整洁，暂不抽取
        byteBuffer.putInt(rpcRequest.getInterfaceName().getBytes(StandardCharsets.UTF_8).length);
        byteBuffer.put(rpcRequest.getInterfaceName().getBytes(StandardCharsets.UTF_8));

        byteBuffer.putInt(rpcRequest.getMethodName().getBytes(StandardCharsets.UTF_8).length);
        byteBuffer.put(rpcRequest.getMethodName().getBytes(StandardCharsets.UTF_8));

        byteBuffer.putInt(rpcRequest.getMethodArgument() == null ? 0 : rpcRequest.getMethodArgument().length);
        byteBuffer.put(rpcRequest.getMethodArgument());

    }

    @Override
    public RpcRequest parse(byte[] src, int offset, int length) {

        ByteBuffer byteBuffer = ByteBuffer.wrap(src, offset, length);

        int interfaceNameByteLength = byteBuffer.getInt();
        byte[] interfaceBytes = new byte[interfaceNameByteLength];
        byteBuffer.get(interfaceBytes);

        int methodNameByteLength = byteBuffer.getInt();
        byte[] methodNameBytes = new byte[methodNameByteLength];
        byteBuffer.get(methodNameBytes);

        int methodArgumentByteLength = byteBuffer.getInt();
        byte[] methodArgumentBytes = new byte[methodArgumentByteLength];
        byteBuffer.get(methodArgumentBytes);

        return new RpcRequest(new String(interfaceBytes,StandardCharsets.UTF_8),new String(methodNameBytes,StandardCharsets.UTF_8),methodArgumentBytes);
    }

    @Override
    public byte type() {
        return RPC_SERIALIZE;
    }

    @Override
    public Class<RpcRequest> serializeClass() {
        return RpcRequest.class;
    }
}
