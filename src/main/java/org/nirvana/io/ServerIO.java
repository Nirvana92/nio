package org.nirvana.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            String readLine = null;
            while ((readLine = reader.readLine()) != null) {
                String printMsg = "[host: %s] [host: %s] [msg: %s]";
                System.out.println(String.format(printMsg, client.getInetAddress().getHostAddress(), client.getPort(), readLine));
            }
        }
    }
}
