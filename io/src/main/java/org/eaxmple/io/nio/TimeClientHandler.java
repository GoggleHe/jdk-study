package org.eaxmple.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class TimeClientHandler implements Runnable {
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public TimeClientHandler(String host, int port) {
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;

        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            System.out.println("Time Client init succeed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!stop) {
            try {
                System.out.println("selector.select start");
                selector.select(1000);
                System.out.println("selector.select end");
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    handleInput(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        System.out.println("handleInput start");
        if (key.isValid()) {
            System.out.println("key.isValid() start");
            SocketChannel socketChannel = (SocketChannel) key.channel();

            if (key.isConnectable()) {
                System.out.println("key.isConnectable()");
                if (socketChannel.finishConnect()) {
                    System.out.println("socketChannel.finishConnect()");
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    doWrite(socketChannel);
                } else {
                    System.out.println("强制关闭");
                    System.exit(1);
                }

            }

            if (key.isReadable()) {
                System.out.println("key.isReadable()");
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                int read = socketChannel.read(readBuffer);
                if (read > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println("Now is " + body);
                } else if (read < 0) {
                    key.cancel();
                    socketChannel.close();
                } else {

                }

            }
            System.out.println("key.isValid() end");
        }
        System.out.println("handleInput end");
    }

    private void doConnect() throws IOException {
        System.out.println("doConnect start");
        boolean connect = socketChannel.connect(new InetSocketAddress(host, port));
        if (connect) {
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
        System.out.println("doConnect end");
    }

    private void doWrite(SocketChannel socketChannel) throws IOException {
        byte[] bytes = "QUERY TIME ORDER".getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        socketChannel.write(buffer);

        if (!buffer.hasRemaining()) {
            System.out.println("send 2 server succeed!");
        }

    }

    public void stop() {
        stop = true;
    }
}
