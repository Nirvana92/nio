package org.nirvana.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientIO {
    public static void main(String[] args) throws IOException {
        int port = 8888;
        Client client = new Client(port);

        client.sendMsg("哈哈哈");
        client.sendMsg("Hello Server");

        client.close();
    }

    static class Client {
        Socket socket;
        OutputStream os;

        public Client(int port) throws IOException {
            socket = new Socket();
            socket.connect(new InetSocketAddress(port));
            os = socket.getOutputStream();
        }

        public void sendMsg(String msg) throws IOException {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(os));
                writer.write(msg);
                writer.flush();
            }finally {
                if(writer != null) {
                    //writer.close();
                }
            }
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
