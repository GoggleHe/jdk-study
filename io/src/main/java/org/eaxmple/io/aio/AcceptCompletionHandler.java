package org.eaxmple.io.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {

    @Override
    public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
        System.out.println("AcceptCompletionHandler.completed start");
        attachment.getServerSocketChannel().accept(attachment, this);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        result.read(buffer, buffer, new ReadCompletionHandler(result));
        System.out.println("AcceptCompletionHandler.completed end");
    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
        attachment.getCountDownLatch().countDown();
    }

}
