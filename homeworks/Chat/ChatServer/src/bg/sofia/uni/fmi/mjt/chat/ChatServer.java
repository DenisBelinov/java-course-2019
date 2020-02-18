package bg.sofia.uni.fmi.mjt.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ChatServer {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 7777;
    private static final int BUFFER_SIZE = 1024;

    private Map<SocketChannel, String> nicks = new HashMap<>();
    private ByteBuffer messageBuffer = ByteBuffer.allocate(BUFFER_SIZE);

    public void runServer() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            while (true) {
                selector.select();

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();

                        buffer.clear();
                        int r = sc.read(buffer);
                        if (r <= 0) {
                            System.out.println("nothing to read, will close channel");

                            // remove this client from nicks
                            nicks.remove(sc);
                            sc.close();
                            break;
                        }

                        // parse the string, move this to a different function
                        buffer.flip();

                        //TODO: Do these with an executor
                        String fullCommand = StandardCharsets.UTF_8.decode(buffer).toString();
                        executeCommand(fullCommand, sc);

                    } else if (key.isAcceptable()) {
                        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                        SocketChannel accepted = sockChannel.accept();
                        accepted.configureBlocking(false);
                        accepted.register(selector, SelectionKey.OP_READ);

                        nicks.put(accepted, "");
                    }

                    keyIterator.remove();
                }

            }

        } catch (IOException e) {
            System.out.println("There is a problem with the server socket");
            e.printStackTrace();
        }
    }

    /**
     * Executes one of the commands
     * nick
     * send
     * send-all
     * list-users
     *
     * Message validation is done client-side. This method expects the message to be in the correct format:
     * <<command>> <<content>>
     *
     * @param fullCommand the full message
     * @param client the client who sent the command
     */
    private void executeCommand(String fullCommand, SocketChannel client) {
        String[] parts = fullCommand.split(" ", 2);

        String command = parts[0];
        String content = "";

        if (parts.length > 1) {
            content = parts[1];
        }

        //TODO: refactor these ifs to command pattern or a factory or something
        //TODO: ATLEAST FUNCTIONS
        // change nick
        if (command.equals("nick")) {
            nicks.put(client, content);
            return;
        }

        String clientNick = nicks.get(client);

        // send message
        if (command.equals("send")) {
            String[] contentParts = content.split(" ", 2);

            String targetNick = contentParts[0];
            String message = contentParts[1];

            Optional<SocketChannel> targetOptional = getClientByNick(targetNick);
            if (targetOptional.isPresent()) {
                String messageWithSender = "[" + clientNick + "] " + message;

                sendMessage(messageWithSender, targetOptional.get());
            } else {
                String userNotFoundMessage = String.format("[%s] seems to be offline", targetNick);
                sendMessage(userNotFoundMessage, client);
            }
        }

        if (command.equals("send-all")) {
            String messageWithSender = "[" + clientNick + "] " + content;

            nicks.entrySet().stream()
                            .filter(e -> !e.getValue().equals(clientNick))
                            .map(Map.Entry::getKey)
                            .forEach(c -> sendMessage(messageWithSender, c));
        }

        if (command.equals("list-users")) {
            String userList = String.join(",", nicks.values());

            sendMessage(userList, client);
        }
    }

    private Optional<SocketChannel> getClientByNick(String nick) {
        return nicks.entrySet().stream()
                               .filter(e -> e.getValue().equals(nick))
                               .map(Map.Entry::getKey)
                               .findFirst();
    }

    private void sendMessage(String message, SocketChannel client) {
        messageBuffer.clear();

        messageBuffer.put(message.getBytes());
        messageBuffer.flip();

        try {
            client.write(messageBuffer);
        } catch (IOException e) {
            System.out.println("Failed to send message to client. This message is lost forever...");
        }
    }
}
