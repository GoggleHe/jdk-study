package org.eaxmple.io.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MultiplexerTimeServer implements Runnable {
    private ServerSocketChannel serverSocketChannel = null;

    private Selector selector;

    private volatile boolean stop;


    public MultiplexerTimeServer() {
        try {
            serverSocketChannel = ServerSocketChannel.open();

            serverSocketChannel.bind(new InetSocketAddress(InetAddress.getLocalHost(), 8080));
            serverSocketChannel.configureBlocking(false);

            selector = Selector.open();

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Time Server init succeed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        stop = true;
    }


    @Override
    public void run() {
        while (!stop) {
            try {
                selector.select(TimeUnit.SECONDS.toMillis(1));
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    iterator.remove();
                    handle(next);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(SelectionKey key) {
        System.out.println("handle start");
        if (key.isValid()) {
            System.out.println("key.isValid()");
            if (key.isAcceptable()) {
                System.out.println("key.isAcceptable()");
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                try {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (key.isReadable()) {
                System.out.println("key.isReadable()");
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                try {
                    int read = socketChannel.read(buffer);
                    if (read > 0) {
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        String body = new String(bytes, StandardCharsets.UTF_8);
                        String format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
                        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? format : "BAD REQUEST";
                        System.out.println(currentTime);

                        doWrite(socketChannel, currentTime);

                    } else if (read < 0) {
                        key.cancel();
                        socketChannel.close();
                    } else {

                    }
                } catch (IOException e) {
                    try {
                        socketChannel.close();
                        key.cancel();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        }
        System.out.println("handle end");
    }

    private void doWrite(SocketChannel socketChannel, String response) {
        if (response != null && response.trim().length() > 0) {
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            try {
                while (writeBuffer.hasRemaining()) {
                    socketChannel.write(writeBuffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }
}
