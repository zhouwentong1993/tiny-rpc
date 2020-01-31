package com.wentong.socketrpc;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

/**
 * 来自梁飞（Dubbo 开发者）
 */
public class SocketRpc {

    /**
     * 暴露服务
     *
     * @param service 暴露哪个服务
     * @param port    暴露在哪个端口
     */
    public static void export(Object service, int port) throws Exception {
        Objects.requireNonNull(service, "不能暴露空服务");
        validPort(port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            for (; ; ) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        InputStream inputStream = socket.getInputStream();
                        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                        String methodName = objectInputStream.readUTF();
                        Class<?>[] parameterType = (Class<?>[]) objectInputStream.readObject();
                        Object[] parameters = (Object[]) objectInputStream.readObject();
                        Method method = service.getClass().getMethod(methodName, parameterType);
                        Objects.requireNonNull(method);
                        Object result = method.invoke(service, parameters);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeObject(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    private static void validPort(int port) {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("非法的端口");
        }
    }

    public static <T> T refer(Class<T> clazz, String host, int port) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(host);
        validPort(port);

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            Socket socket = new Socket(host, port);
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeUTF(method.getName());
            objectOutputStream.writeObject(method.getParameterTypes());
            objectOutputStream.writeObject(args);
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            return objectInputStream.readObject();
        });
    }

}
