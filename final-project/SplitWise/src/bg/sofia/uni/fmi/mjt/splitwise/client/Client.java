package bg.sofia.uni.fmi.mjt.splitwise.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static ByteBuffer buffer = ByteBuffer.allocate(2048);

    private String user = "<>";

    public void runClient() {
        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            System.out.println("Connected to the server.");

            while (true) {
                System.out.print("Enter command: ");
                String fullCommand = scanner.nextLine();
                String[] parts = fullCommand.split(" ", 2);

                String command = parts[0];

                if ("disconnect".equals(command)) {
                    break;
                }

                String commandAndUser = user + " " + fullCommand;

                buffer.clear();
                buffer.put(commandAndUser.getBytes()); // buffer fill
                buffer.flip();
                socketChannel.write(buffer); // buffer drain

                buffer.clear();
                socketChannel.read(buffer); // buffer fill
                buffer.flip();
                String reply = new String(buffer.array(), 0, buffer.limit()); // buffer drain

                Matcher m = Pattern.compile("^(<.*>) (.*)").matcher(reply); //get the User if the server has sent us one
                if(m.matches())
                {
                    user = m.group(1);
                    reply = m.group(2);
                }

                System.out.println("[SERVER] " + reply);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
