package com.wentong.rpc.netty.nameservice;

import com.google.common.collect.Lists;
import com.wentong.rpc.NamingService;
import com.wentong.rpc.netty.serialize.SerializeSupport;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LocalNameService implements NamingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalNameService.class);


    private final List<String> support_protocol = Lists.newArrayList("file");
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
    public void register(URI uri, String serviceName) throws IOException {
        LOGGER.info("Register service: {}, uri: {}.", serviceName, uri);
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel fileChannel = raf.getChannel()) {
            FileLock lock = fileChannel.lock();
            try {
                int fileLength = (int) raf.length();
                Metadata metadata;
                byte[] bytes;
                if (fileLength > 0) {
                    bytes = new byte[(int) raf.length()];
                    ByteBuffer buffer = ByteBuffer.wrap(bytes);
                    while (buffer.hasRemaining()) {
                        fileChannel.read(buffer);
                    }

                    metadata = SerializeSupport.parse(bytes);
                } else {
                    metadata = new Metadata();
                }
                List<URI> uris = metadata.computeIfAbsent(serviceName, k -> new ArrayList<>());
                if (!uris.contains(uri)) {
                    uris.add(uri);
                }
                LOGGER.info(metadata.toString());

                bytes = SerializeSupport.serialize(metadata);
                fileChannel.truncate(bytes.length);
                fileChannel.position(0L);
                fileChannel.write(ByteBuffer.wrap(bytes));
                fileChannel.force(true);
            } finally {
                lock.release();
            }
        }
    }

    @Override
    public URI getServiceURI(String serviceName) throws IOException {
        Metadata metadata;
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel fileChannel = raf.getChannel()) {
            FileLock lock = fileChannel.lock();
            try {
                byte[] bytes = new byte[(int) raf.length()];
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                while (buffer.hasRemaining()) {
                    fileChannel.read(buffer);
                }
                metadata = bytes.length == 0 ? new Metadata() : SerializeSupport.parse(bytes);
                LOGGER.info(metadata.toString());
            } finally {
                lock.release();
            }
        }

        List<URI> uris = metadata.get(serviceName);
        if (null == uris || uris.isEmpty()) {
            return null;
        } else {
            return uris.get(ThreadLocalRandom.current().nextInt(uris.size()));
        }
    }
}
