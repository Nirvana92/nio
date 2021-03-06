package org.nirvana.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketIoProperties {

    //server socket listen property:
    private static final int RECEIVE_BUFFER = 10;
    private static final int SO_TIMEOUT = 0;
    private static final boolean REUSE_ADDR = false;
    // 服务起来, 留多少个线程备用; 配置2 链接建立可以为3. 第四个连接过来 建立连接状态为 SYNC_RECV
    private static final int BACK_LOG = 2;
    //client socket listen property on server endpoint:
    // 心跳: 是否是长连接
    private static final boolean CLI_KEEPALIVE = false;
    // 发一个包试探一下
    private static final boolean CLI_OOB = false;
    // netstat -natp 查看
    private static final int CLI_REC_BUF = 20;
    //
    private static final boolean CLI_REUSE_ADDR = false;
    private static final int CLI_SEND_BUF = 20;
    private static final boolean CLI_LINGER = true;
    private static final int CLI_LINGER_N = 0;
    // 超时时间
    private static final int CLI_TIMEOUT = 0;
    // 是否积攒包一起发送, 否则每次发送不超过配置的 buf
    private static final boolean CLI_NO_DELAY = false;
/*
    StandardSocketOptions.TCP_NODELAY
    StandardSocketOptions.SO_KEEPALIVE
    StandardSocketOptions.SO_LINGER
    StandardSocketOptions.SO_RCVBUF
    StandardSocketOptions.SO_SNDBUF
    StandardSocketOptions.SO_REUSEADDR

 */

    public static void main(String[] args) throws IOException {
        ServerSocket server = null;

        server = new ServerSocket();
        server.bind(new InetSocketAddress(9090), BACK_LOG);
        server.setReceiveBufferSize(RECEIVE_BUFFER);
        server.setReuseAddress(REUSE_ADDR);
        server.setSoTimeout(SO_TIMEOUT);

        System.out.println("server up use 9090");

        while (true) {
            System.in.read();

            Socket client = server.accept();
            System.out.println("client port: " + client.getPort());
            client.setKeepAlive(CLI_KEEPALIVE);
            client.setOOBInline(CLI_OOB);
            client.setReceiveBufferSize(CLI_REC_BUF);
            client.setReuseAddress(CLI_REUSE_ADDR);
            client.setSendBufferSize(CLI_SEND_BUF);
            client.setSoLinger(CLI_LINGER, CLI_LINGER_N);
            client.setSoTimeout(CLI_TIMEOUT);
            client.setTcpNoDelay(CLI_NO_DELAY);

            new Thread(
                    () -> {
                        while (true) {
                            try {
                                InputStream in = client.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                char[] data = new char[1024];
                                int num = reader.read(data);

                                if (num > 0) {
                                    System.out.println("client read some data is :" + num + " val :" + new String(data, 0, num));
                                } else if (num == 0) {
                                    System.out.println("client readed nothing!");
                                    continue;
                                } else {
                                    System.out.println("client readed -1...");
                                    client.close();
                                    break;
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).start();
        }
    }
}
