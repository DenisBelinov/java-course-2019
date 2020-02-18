package bg.sofia.uni.fmi.mjt.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ChatClient {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static ByteBuffer buffer = ByteBuffer.allocate(1024);

    public void runClient() {
        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            socketChannel.configureBlocking(false);

            System.out.println("Connected to the server.");
            ServerInputHandler inputHandler = new ServerInputHandler(socketChannel);
            (new Thread(inputHandler)).start();

            while (true) {
                System.out.print("Enter message: ");
                String fullCommand = scanner.nextLine();
                String[] parts = fullCommand.split(" ", 2);
                //TODO: Handle empty messages
                String command = parts[0];

                if ("disconnect".equals(command)) {
                    socketChannel.close();
                    break;
                }

                buffer.clear();
                buffer.put(fullCommand.getBytes()); // buffer fill
                buffer.flip();
                socketChannel.write(buffer); // buffer drain
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}