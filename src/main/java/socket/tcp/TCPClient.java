package socket.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @Author : Wu.D.J
 * @Create : 2020.11.11
 */
public class TCPClient {

    private final String ip;

    private final int port;

    private Socket socket;

    private InputStream in;

    private OutputStream out;

    private ReadThread readThread;

    private volatile boolean isConnected;

    public TCPClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void connect() {
        try {
            this.socket = new Socket();
            SocketAddress address = new InetSocketAddress(ip, port);
            socket.connect(address);

            in = socket.getInputStream();
            out = socket.getOutputStream();

            readThread = new ReadThread(in);
            isConnected = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String request) {
        try {
            out.write(request.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void close() {
        if (readThread != null) {
            readThread.interrupt();
        }
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ReadThread extends Thread {

        private final InputStream in;

        public ReadThread(InputStream in) {
            this.in = in;
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    if (in != null) {
                        int available = in.available();
                        if (available > 0) {
                            byte[] buffer = new byte[available];
                            int size = in.read(buffer);
                            String response = new String(buffer, 0, size);
                            System.out.println("Receive Server Msg : [" + response + "]!");
                        } else {
                            Thread.sleep(100);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
