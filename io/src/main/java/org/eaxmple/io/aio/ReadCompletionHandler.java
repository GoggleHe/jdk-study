package org.eaxmple.io.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel channel;

    public ReadCompletionHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        System.out.println("ReadCompletionHandler.completed start");
        byte[] bytes = new byte[attachment.remaining()];

        attachment.get(bytes);

        String body = new String(bytes, StandardCharsets.UTF_8);

        String format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? format : "BAD REQUEST";

        doWrite(currentTime);
        System.out.println("ReadCompletionHandler.completed end");
    }

    private void doWrite(String currentTime) {
        System.out.println("ReadCompletionHandler.doWrite start");
        if (currentTime != null && currentTime.trim().length() > 0) {
            byte[] bytes = currentTime.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            channel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    if (attachment.hasRemaining()) {
                        channel.write(attachment, attachment, this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    try {
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        System.out.println("ReadCompletionHandler.doWrite end");
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.out.println("failed");
        try {
            this.channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
