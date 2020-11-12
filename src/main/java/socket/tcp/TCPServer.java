package socket.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Wu.D.J
 * @Create : 2020.11.11
 */
public class TCPServer {

    private final int port;

    private volatile boolean isFinished;

    private ServerSocket serverSocket;

    private final List<SocketThread> socketList;

    public TCPServer(int port) {
        this.port = port;
        socketList = new ArrayList<>();
    }

    public void start() {
        isFinished = false;
        try {
            serverSocket = new ServerSocket(port);
            while (!isFinished) {
                Socket socket = serverSocket.accept();
                SocketThread thread = new SocketThread(socket);
                socketList.add(thread);
                thread.start();
            }
        } catch (Exception e) {
            isFinished = true;
        }
    }

    public void stop() {
        isFinished = true;
        socketList.forEach(thread -> {
            thread.interrupt();
            thread.close();
        });
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class SocketThread extends Thread {

        private final Socket socket;
        private InputStream in;
        private OutputStream out;

        SocketThread(Socket socket) {
            this.socket = socket;
            try {
                this.in = socket.getInputStream();
                this.out = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                if (in != null) {
                    try {
                        int available = in.available();
                        if (available > 0) {
                            byte[] buffer = new byte[available];
                            int size = in.read(buffer);
                            if (size > 0) {
                                String request = new String(buffer, 0, size);
                                System.out.println("Receive Client Msg : [" + request + "]!");

                                String response = "Yet Receive Your Msg : [" + request + "]!";
                                out.write(response.getBytes());
                                out.flush();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void close() {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
