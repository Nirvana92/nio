package org.nirvana.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author gzm
 * @date 2020/11/23 2:57 下午
 * @desc: 不适用nio 的方式
 */
public class ServerNioBlocking {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 9999;
        int threadPoolSize = 5;
        Server server = new Server(port, threadPoolSize);
        server.start();
    }

    static class Server {
        ServerSocketChannel server;
        ExecutorService threadPool;

        public Server(int port, int threadPoolSize) throws IOException {
            server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(port));

            threadPool = Executors.newFixedThreadPool(threadPoolSize);
        }

        public void start() throws IOException, InterruptedException {
            while (true) {
                SocketChannel client = server.accept();
                if(client != null) {
                    handler(client);
                }

                TimeUnit.SECONDS.sleep(2);
            }
        }

        public static void handler(SocketChannel client) throws IOException {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (client.read(buffer) != 0) {
                buffer.flip();
                byte[] msgs = new byte[buffer.limit()];
                buffer.get(msgs);

                System.out.println(String.format("[host=%s] [port=%s] [msg=%s]",
                        client.socket().getInetAddress().getHostAddress(),
                        client.socket().getPort(),
                        new String(msgs, "utf-8")));
            }
        }
    }
}
