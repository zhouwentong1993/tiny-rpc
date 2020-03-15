package com.wentong.rpc.netty.nameservice;

import com.google.common.collect.Lists;
import com.wentong.rpc.NamingService;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;
import java.util.List;

public class LocalNameService implements NamingService {

    private  final List<String> support_protocol = Lists.newArrayList("file");
    private File file;

    @Override
    public List<String> supportedProtocol() {
        return support_protocol;
    }

    @Override
    public void connect(@NotNull URI nameServiceURI) {
        if (support_protocol.contains(nameServiceURI.getScheme())) {
            file = new File(nameServiceURI);
        } else {
            throw new UnsupportedOperationException("不支持的URI:" + nameServiceURI);
        }
    }

    @Override
    public void register(URI uri, String serviceName) {

    }

    @Override
    public URI getServiceURI(String serviceName) {
        return null;
    }
}
