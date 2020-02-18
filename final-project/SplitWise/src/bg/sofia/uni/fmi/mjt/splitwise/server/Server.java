package bg.sofia.uni.fmi.mjt.splitwise.server;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.CommandParser;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.exception.InvalidCommandException;
import bg.sofia.uni.fmi.mjt.splitwise.server.execution.CommandExecutor;
import bg.sofia.uni.fmi.mjt.splitwise.server.execution.action.exception.UnsupportedCommandTypeException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Java NIO Server that accepts TCP connections.
 */
public class Server {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 7777;
    private static final int BUFFER_SIZE = 2048;

    private static ByteBuffer messageBuffer = ByteBuffer.allocate(BUFFER_SIZE);

    private Logger logger = Logger.getLogger(Server.class.getName());

    private CommandParser commandParser;
    private CommandExecutor commandExecutor;

    public Server(CommandParser commandParser, CommandExecutor commandExecutor) {
        this.commandParser = commandParser;
        this.commandExecutor = commandExecutor;
    }

    /**
     * Starts the server on SERVER_PORT
     */
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
                    try {
                        if (key.isReadable()) {
                            SocketChannel sc = (SocketChannel) key.channel();

                            buffer.clear();
                            int r = sc.read(buffer);
                            if (r <= 0) {
                                logger.log(Level.FINE, "nothing to read, will close channel");

                                sc.close();
                                break;
                            }

                            // parse the string, move this to a different function
                            buffer.flip();

                            String fullCommand = StandardCharsets.UTF_8.decode(buffer).toString();
                            executeCommand(fullCommand, sc);

                        } else if (key.isAcceptable()) {
                            ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                            SocketChannel accepted = sockChannel.accept();
                            accepted.configureBlocking(false);
                            accepted.register(selector, SelectionKey.OP_READ);

                            logger.info("Accepted incoming connection.");
                        }

                        keyIterator.remove();
                    } catch (IOException e) {
                        key.cancel();
                    }
                }

            }

        } catch (IOException e) {
            logger.severe("There is a problem with the server socket.");
            e.printStackTrace();
        }
    }

    /**
     * Parses the given command through commandParser and sends it for execution to the commandExecutor
     * @param fullCommand String containing the full command received from the client
     * @param client The socket which the command was received from
     */
    private void executeCommand(String fullCommand, SocketChannel client) {
        Command command = null;
        try {
            command = commandParser.parse(fullCommand);
        } catch (InvalidCommandException e) {
            writeMessageToChannel(e.getMessage(), client);
        }

        try {
            commandExecutor.execute(command, client);
        } catch (IllegalArgumentException e) {
            String errMsg = "This is an invalid command.";
            writeMessageToChannel(errMsg, client);
        } catch (UnsupportedCommandTypeException e) {
            String errMsg = "This is an unsupported command.";
            writeMessageToChannel(errMsg, client);
        }
    }

    public static void writeMessageToChannel(String message, SocketChannel channel) {
        messageBuffer.clear();

        messageBuffer.put(message.getBytes());
        messageBuffer.flip();

        try {
            channel.write(messageBuffer);
        } catch (IOException e2) {
            String errMsg = String.format("Failed to send message to client: %s.", channel.toString());
            System.out.println(errMsg);
        }
    }
}
