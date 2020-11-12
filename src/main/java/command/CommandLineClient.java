package command;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author : Wu.D.J
 * @Create : 2020.11.09
 * https://blog.csdn.net/qq_36270361/article/details/106015088
 */
public class CommandLineClient {

    public static void main(String[] args) throws IOException {
        Scanner serverScanner = new Scanner(System.in);
        System.out.println("请输入要连接的IP地址:");
        String ip = serverScanner.nextLine();
        System.out.println("输入的IP地址为:" + ip);
        System.out.println("请输入要连接的端口号:");
        int port = serverScanner.nextInt();
        System.out.println("输入的端口号为:" + port);

        Socket socket = new Socket();
        InetSocketAddress address = new InetSocketAddress(ip, port);
        socket.connect(address);
        System.out.println(String.format("服务端[%s:%d]连接成功!", ip, port));

        Scanner commandScanner = new Scanner(System.in);
        System.out.print("please enter command : ");
        while (commandScanner.hasNext()) {
            String command = commandScanner.nextLine();
            socket.getOutputStream().write(command.getBytes());
//            socket.getOutputStream().flush();

            while (true) {
                if (socket.getReceiveBufferSize() > 0) {
                    byte[] bytes = new byte[socket.getReceiveBufferSize()];
                    socket.getInputStream().read(bytes);
                    String response = new String(bytes);
                    System.out.println(response);
                    break;
                }
            }
            System.out.print("please enter command : ");
        }
    }
}
