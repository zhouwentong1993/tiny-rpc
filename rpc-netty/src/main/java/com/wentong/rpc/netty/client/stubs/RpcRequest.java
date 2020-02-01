package com.wentong.rpc.netty.client.stubs;

import java.util.Arrays;

/**
 * RPC 请求抽象类
 */
public class RpcRequest {

    // 接口名
    private String interfaceName;
    // 方法名
    private String methodName;
    // 方法参数
    private byte[] methodArgument;

    public RpcRequest(String interfaceName, String methodName, byte[] methodArgument) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.methodArgument = methodArgument;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public byte[] getMethodArgument() {
        return methodArgument;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "interfaceName='" + interfaceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", methodArgument=" + Arrays.toString(methodArgument) +
                '}';
    }
}
