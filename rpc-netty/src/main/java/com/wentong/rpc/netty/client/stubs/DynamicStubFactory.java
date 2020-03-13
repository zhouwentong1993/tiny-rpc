package com.wentong.rpc.netty.client.stubs;

import com.itranswarp.compiler.JavaStringCompiler;
import com.wentong.rpc.netty.client.ServiceStub;
import com.wentong.rpc.netty.client.StubFactory;
import com.wentong.rpc.netty.transport.Transport;
import lombok.SneakyThrows;

import java.util.Map;

/**
 * 生成桩工厂类
 */
public class DynamicStubFactory implements StubFactory {

    // 定义要替换的模板，动态生成要服务的类
    private static final String TEMPLATE = "package com.wentong.rpc.netty.client.stubs;\n" +
            "\n" +
            "import com.wentong.rpc.netty.serialize.SerializeSupport;\n" +
            "\n" +
            "public class %s extends AbstractStub implements %s {\n" +
            "\n" +
            "    @Override\n" +
            "    public String %s(String param) {\n" +
            "        return SerializeSupport.parse(\n" +
            "                invokeRemote(\n" +
            "                    new RpcRequest(\n" +
            "                            %s,\n" +
            "                            %s,\n" +
            "                    SerializeSupport.serialize(param)\n" +
            "                    )\n" +
            "                )\n" +
            "        );\n" +
            "    }\n" +
            "}";

    @SneakyThrows
    @Override
    public <T> T crateStub(Transport transport, Class<T> clazz) {
        // 生成的服务类桩名
        String simpleName = clazz.getSimpleName() + "Stub";
        // 服务类的全限定名
        String serviceName = clazz.getName();
        // 获取方法名，默认只取第一个
        String methodName = clazz.getMethods()[0].getName();

        String fullName = "com.wentong.rpc.netty.client.stubs." + simpleName;

        // 替换好的源码
        String source = String.format(TEMPLATE, simpleName, serviceName, methodName, serviceName, methodName);
        JavaStringCompiler javaStringCompiler = new JavaStringCompiler();
        Map<String, byte[]> compile = javaStringCompiler.compile(simpleName + ".java", source);
        Class<?> serviceClass = javaStringCompiler.loadClass(fullName, compile);
        ServiceStub serviceStub = (ServiceStub) serviceClass.newInstance();
        serviceStub.setTransport(transport);
        return (T) serviceStub;
    }

}
