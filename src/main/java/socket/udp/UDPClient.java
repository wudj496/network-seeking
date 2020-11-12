package socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author : Wu.D.J
 * @Create : 2020.11.11
 */
public class UDPClient {

    public static final int BYTE_SIZE = 1024;

    private final int clientPort;

    private final String serverIp;

    private final int serverPort;

    private DatagramSocket socket;

    private ReadThread readThread;

    public UDPClient(int clientPort, String serverIp, int serverPort) {
        this.clientPort = clientPort;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void start() {
        try {
            socket = new DatagramSocket(clientPort);
            readThread = new ReadThread(socket);
            readThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        try {
            DatagramPacket requestPacket = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName(serverIp), serverPort);
            socket.send(requestPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (readThread != null) {
            readThread.interrupt();
        }
        if (socket != null) {
            socket.close();
        }
    }

    public static class ReadThread extends Thread {

        DatagramSocket datagramSocket;

        public ReadThread(DatagramSocket datagramSocket) {
            this.datagramSocket = datagramSocket;
        }
        @Override
        public void run() {
            DatagramPacket receivePacket = new DatagramPacket(new byte[BYTE_SIZE], BYTE_SIZE);
            while (!isInterrupted()) {
                try {
                    datagramSocket.receive(receivePacket);
                    int packetLength = receivePacket.getLength();
                    if (packetLength > 0) {
                        String receive = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        System.out.println("Receive Server Msg : [" + receive + "]!");
                    } else {
                        // busy-waiting
                        Thread.sleep(100);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
