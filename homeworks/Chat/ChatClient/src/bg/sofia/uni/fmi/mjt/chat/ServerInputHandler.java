package bg.sofia.uni.fmi.mjt.chat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * Is responsible for handling the messages coming form the server
 */
public class ServerInputHandler implements Runnable {
    private static final int BUFFER_SIZE = 1024;

    private SocketChannel server;
    public ServerInputHandler(SocketChannel server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            Selector selector = Selector.open();
            server.register(selector, SelectionKey.OP_READ);
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            while (true) {
                selector.select();

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (!key.isValid()) {
                        break;
                    }

                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();

                        buffer.clear();
                        int r = sc.read(buffer);
                        if (r <= 0) {
                            System.out.println("nothing to read, will close channel");
                            sc.close();
                            break;
                        }

                        sc.read(buffer); // buffer fill
                        buffer.flip();
                        String reply = new String(buffer.array(), 0, buffer.limit()); // buffer drain

                        System.out.println();
                        System.out.println("[SERVER] " + reply);
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            System.out.println("There is a problem with the server socket");
            e.printStackTrace();
        }
    }
}
