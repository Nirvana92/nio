package org.nirvana.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author gzm
 * @date 2020/11/23 7:31 下午
 * @desc
 */
public class ClientNioNonBlocking {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 9999;

        ClientNonBlocking client = new ClientNonBlocking(port);
        //TimeUnit.SECONDS.sleep(3);
        client.sendMsg("哈哈");
        //TimeUnit.SECONDS.sleep(3);
        client.sendMsg("hello world");
//        String msg = "";
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()) {
//            msg = scanner.nextLine();
//            client.sendMsg(msg);
//        }

        client.close();
    }

    static class ClientNonBlocking {
        SocketChannel client ;
//        Selector selector;

        public ClientNonBlocking(int port) throws IOException {
            client = SocketChannel.open(new InetSocketAddress(port));
//            selector = Selector.open();
//            client.register(selector, SelectionKey.OP_WRITE);
        }

        public void sendMsg(String msg) throws IOException {
            byte[] bytes = msg.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            client.write(buffer);
        }

        public void close() throws IOException {
            if(client != null) {
                client.finishConnect();
                client.close();
            }
        }
    }
}
