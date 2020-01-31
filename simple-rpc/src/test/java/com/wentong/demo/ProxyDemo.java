package com.wentong.demo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理
 */
public class ProxyDemo {
    public static void main(String[] args) {
        Student student1 = new MidStudent();
        Student change = (Student) Proxy.newProxyInstance(student1.getClass().getClassLoader(), student1.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
                // 注意，这个方法 return 了什么和 Proxy.newProxyInstance 无关，这个是代理之后的方法
                // 调用返回的结果。newProxyInstance 返回的结果内部是通过构造方法实现的，见 Proxy 源码 739 行
                return method.invoke(student1, "change");
            }
        });
        String test = change.hello("test");
        System.out.println(test);

    }
}

interface Student {
    String hello(String name);
}

class MidStudent implements Student{

    @Override
    public String hello(String name) {
        return "hello mid " + name;
    }
}

class HighStudent implements Student{

    @Override
    public String hello(String name) {
        return "hello high " + name;
    }
}
