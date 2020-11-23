package org.nirvana.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author gzm
 * @date 2020/11/23 7:30 下午
 * @desc
 */
public class ServerNioNonBlocking {
    public static void main(String[] args) throws IOException {
        int port = 9999;
        ServerNonBlocking server = new ServerNonBlocking(port);
        server.start();
    }

    static class ServerNonBlocking {
        ServerSocketChannel server ;
        Selector selector;

        public ServerNonBlocking(int port) throws IOException {
            server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(port));
            server.configureBlocking(false);

            selector = Selector.open();
            // server.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_ACCEPT | SelectionKey.OP_READ);
            server.register(selector, SelectionKey.OP_ACCEPT);
        }

        public void start() throws IOException {

            while (true) {
                int select = selector.select(TimeUnit.MILLISECONDS.toMillis(2000));
                // 说明有事件连接待处理
                if(select > 0) {
                    // 这边选择的keys 需要移除, 所以需要使用迭代器的方式进行移除
                    Set<SelectionKey> keys = selector.selectedKeys();
                    // fixme: 为什么这边一直都有key进入.[因为退出的时候没有将selectionKey 进行移除]
                    // System.out.println("keys.size: "+keys.size());
                    Iterator<SelectionKey> keysIterator = keys.iterator();
                    while (keysIterator.hasNext()) {
                        SelectionKey key = keysIterator.next();
                        keysIterator.remove();

                        System.out.println("--->>> key: "+key.toString());
                        if(key.isAcceptable()) {
                            // 可接受的
                            ServerSocketChannel ss = (ServerSocketChannel) key.channel();
                            SocketChannel client = ss.accept();
                            client.configureBlocking(false);

                            client.register(selector, SelectionKey.OP_READ);
                        }else if(key.isReadable()) {
                            readMsg(key);
                        }
                    }
                }

                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void readMsg(SelectionKey key) throws IOException {
            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int readLen = 0;
            while ((readLen = client.read(buffer)) > 0) {
                buffer.flip();
//                 byte[] msgs = new byte[buffer.limit()];
//                 buffer.get(msgs);

                System.out.println(String.format("[host=%s] [port=%s] [msg=%s]",
                        client.socket().getInetAddress().getHostAddress(),
                        client.socket().getPort(),
                        new String(buffer.array(), "utf-8")));
                buffer.clear();
            }

            // 如果客户端断开了连接. 将key 移除掉
            if(readLen == -1) {
                System.out.println("移除 selectKey");
                client.close();
                key.cancel();
            }
        }
    }
}
