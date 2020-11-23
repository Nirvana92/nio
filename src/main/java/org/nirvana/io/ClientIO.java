package org.nirvana.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientIO {
    public static void main(String[] args) throws IOException {
        int port = 8888;
        Client client = new Client(port);
        client.sendMsg("客户端连接上了");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String msg = scanner.nextLine();
            client.sendMsg(msg);
            client.receiveMsg();
        }
        client.close();
    }

    static class Client {
        Socket socket;
        OutputStream os;
        InputStream is;

        public Client(int port) throws IOException {
            socket = new Socket();
            socket.connect(new InetSocketAddress(port));
            os = socket.getOutputStream();
            is = socket.getInputStream();
        }

        public void sendMsg(String msg) throws IOException {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(os));
                writer.write(msg);
                writer.flush();
                writer.newLine();
                writer.flush();
            }finally {
                if(writer != null) {
                    // writer.close();
                }
            }
        }

        public void receiveMsg() throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String readLine = reader.readLine();
            System.out.println("接受的消息: "+readLine);
        }

        public void close() throws IOException {
            if(os != null) {
                os.close();
            }

            if(socket != null) {
                socket.close();
            }
        }
    }
}
