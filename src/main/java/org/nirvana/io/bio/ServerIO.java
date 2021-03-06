package org.nirvana.io.bio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerIO {
    public static void main(String[] args) throws IOException {
        int port = 8888;
        int poolSize = 3;

        Server server = new Server(port, poolSize);
        server.start();
    }

    static class Server {
        ServerSocket server;
        ExecutorService threadPool;

        public Server(int port, int poolSize) throws IOException {
            threadPool = Executors.newFixedThreadPool(poolSize);
            server = new ServerSocket(port);
            // server.bind(new InetSocketAddress());
        }

        public void start() throws IOException {
            while (true) {
                Socket client = server.accept();

                threadPool.execute(() -> {
                    try {
                        handler(client);
                        // System.out.println("接受的消息: "+receiveMsg);
                        // sendMsg(client, receiveMsg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

        public void handler(Socket client) throws IOException {
            InputStream is = client.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            // String readLine = reader.readLine();
            String readLine = reader.readLine();
            while ((readLine = reader.readLine()) != null) {
                String printMsg = "[host: %s] [port: %s] [msg: %s]";
                System.out.println(String.format(printMsg, client.getInetAddress().getHostAddress(), client.getPort(), readLine));

                sendMsg(client, readLine);
            }
        }

        public void sendMsg(Socket client, String msg) {
            OutputStream os = null;
            BufferedWriter writer = null;
            try {
                os = client.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(os));

                writer.write(msg);
                writer.flush();
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
