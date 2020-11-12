package command;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author : Wu.D.J
 * @Create : 2020.11.09
 * https://blog.csdn.net/qq_36270361/article/details/106015088
 */
public class CommandLineServer {

    private static String currentPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8801);
        while (true) {
            Socket socket = serverSocket.accept();
            boolean isClose = false;
            while (!isClose) {
                if (socket.getReceiveBufferSize() > 0) {
                    byte[] bytes = new byte[socket.getReceiveBufferSize()];
                    socket.getInputStream().read(bytes);
                    String receive = new String(bytes);
                    String responseMsg = "";

                    if (receive.startsWith("pwd")) {
                        responseMsg = currentPath;
                    } else if (receive.startsWith("cd")) {
                        String command = receive.substring(2);
                        if (command.trim().startsWith("..")) {
                            if (currentPath.endsWith("/")) {
                                currentPath = currentPath.substring(0, currentPath.length() - 1);
                                currentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
                            } else {
                                currentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
                            }
                        }
                        responseMsg = currentPath;
                    } else if (receive.startsWith("ls")) {
                        File file = new File(currentPath);
                        responseMsg = String.join("\n", file.list());
                    } else if (receive.startsWith("quit")) {
                        isClose = true;
                        responseMsg = "关闭客户端连接!";

                    } else {
                        responseMsg = "暂不支持[" + receive + "]命令!";
                    }
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(responseMsg.getBytes());
                    outputStream.flush();

                    if (isClose) {
                        socket.close();
                        socket.shutdownInput();
                        socket.shutdownOutput();
                    }
                }
            }
        }
    }
}
