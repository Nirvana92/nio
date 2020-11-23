package org.nirvana.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @author gzm
 * @date 2020/11/23 2:57 下午
 * @desc
 *
 * fixme: 连接上服务器之后只能发送一次消息
 */
public class ClientNioBlocking {
    public static void main(String[] args) throws IOException {
        int port = 9999;

        Client client = new Client(port);
        String msg = "客户端连接上了";
        client.sendMsg(msg);

        msg = "第二次发送的消息";
        client.sendMsg(msg);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            msg = scanner.nextLine();
            client.sendMsg(msg);
        }
    }

    static class Client {
        SocketChannel client;
        int port;

        public Client(int port) throws IOException {
            this.port = port;
            client = SocketChannel.open(new InetSocketAddress(port));
        }

        public void sendMsg(String msg) throws IOException {
            // client = SocketChannel.open(new InetSocketAddress(port));
            byte[] bytes = msg.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            client.write(buffer);
        }
    }
}
