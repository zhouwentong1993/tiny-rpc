package com.wentong.rpc.spi;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 服务加载支持类，通过指定 Class，来获取配置的实现对象。
 * <p>
 * 通过 Java SPI 机制实现
 */
public class ServiceSupport {

    private ServiceSupport(){}

    private static final Map<String, Object> SINGLETON_MAP = new HashMap<>();

    public static synchronized <S> List<S> loadAll(@NotNull Class<S> clazz) {
        return StreamSupport.stream(ServiceLoader.load(clazz).spliterator(), false)
                .map(ServiceSupport::singletonFilter).collect(Collectors.toList());
    }

    public static synchronized <S> S load(@NotNull Class<S> clazz) {
        return StreamSupport.stream(ServiceLoader.load(clazz).spliterator(), false)
                .map(ServiceSupport::singletonFilter).findFirst().orElseThrow(ServiceLoadException::new);
    }

    private static <S> S singletonFilter(@NotNull S service) {
        if (service.getClass().isAnnotationPresent(Singleton.class)) {
            String canonicalName = service.getClass().getCanonicalName();
            return (S) SINGLETON_MAP.putIfAbsent(canonicalName, service);
        } else {
            return service;
        }
    }

}
