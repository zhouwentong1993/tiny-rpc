package com.wentong.provider;

import com.wentong.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        System.out.println("HelloServiceImpl.sayHello");
        return "Hello " + name;
    }
}
