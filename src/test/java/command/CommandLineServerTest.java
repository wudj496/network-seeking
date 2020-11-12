package command;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;

/**
 * @Author : Wu.D.J
 * @Create : 2020.11.09
 */
public class CommandLineServerTest extends TestCase {

    @Test
    public void testCD() {
        String currentPath = "/Users/wudj/Workspace/DJIN/network-seeking/target/classes/";
        String receive = "cd ..";

        String command = receive.substring(2);
        if (command.trim().startsWith("..")) {
            if (currentPath.endsWith("/")) {
                currentPath = currentPath.substring(0, currentPath.length() - 1);
                currentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
            } else {
                currentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
            }
        }
        System.out.println(currentPath);
    }

    @Test
    public void testLS() {
        File file = new File("/Users/wudj/Workspace/DJIN/network-seeking/target");
        System.out.println(String.join("\n", file.list()));
    }
}
