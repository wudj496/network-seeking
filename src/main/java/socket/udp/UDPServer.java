package socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @Author : Wu.D.J
 * @Create : 2020.11.11
 * https://www.cnblogs.com/zhengtu2015/p/7294477.html
 */
public class UDPServer {

    public static final int BYTE_SIZE = 1024;

    private final int port;

    private DatagramSocket socket;

    private volatile boolean isFinished;

    public UDPServer(int port) {
        this.port = port;
    }

    public void start() {
        isFinished = false;
        try {
            socket = new DatagramSocket(port);
            DatagramPacket receivePacket = new DatagramPacket(new byte[BYTE_SIZE], BYTE_SIZE);
            while (!isFinished) {
                socket.receive(receivePacket);
                int packetLength = receivePacket.getLength();
                if (packetLength > 0) {
                    String receive = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("Receive Client Msg : [" + receive + "]!");

                    String response = "Yet Receive Your Msg : [" + receive + "]!";
                    DatagramPacket responsePacket = new DatagramPacket(response.getBytes(), response.length(), receivePacket.getAddress(), receivePacket.getPort());
                    socket.send(responsePacket);
                    receivePacket.setLength(BYTE_SIZE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        isFinished = true;
        if (socket != null) {
            socket.close();
        }
    }
}
