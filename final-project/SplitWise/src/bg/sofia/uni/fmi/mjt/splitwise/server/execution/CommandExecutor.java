package bg.sofia.uni.fmi.mjt.splitwise.server.execution;

import bg.sofia.uni.fmi.mjt.splitwise.backend.service.DebtService;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.GroupService;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.UserService;
import bg.sofia.uni.fmi.mjt.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.server.execution.action.*;
import bg.sofia.uni.fmi.mjt.splitwise.server.execution.action.exception.UnsupportedCommandTypeException;

import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * This class is responsible to execute all the actions requested from the server.
 */
public class CommandExecutor {
    UserService userService;
    GroupService groupService;
    DebtService debtService;

    ExecutorService executorService;
    BlockingQueue<FutureSocketTuple> actionsQueue;

    private Logger logger = Logger.getLogger(CommandExecutor.class.getName());


    public CommandExecutor(UserService userService, GroupService groupService, DebtService debtService) {
        this.userService = userService;
        this.groupService = groupService;
        this.debtService = debtService;

        this.executorService = Executors.newSingleThreadExecutor(); // TODO: Add more threads here
        this.actionsQueue = new LinkedBlockingQueue<>();

        // Start processing the actions
        new Thread(this::processQueue).start();
    }

    /**
     * Container class for Future and Socker Tuple.
     */
    private class FutureSocketTuple {
        private final Future<String> future;
        private final SocketChannel client;

        public FutureSocketTuple(Future<String> future, SocketChannel client) {
            this.future = future;
            this.client = client;
        }

        public Future<String> getFuture() {
            return future;
        }

        public SocketChannel getClient() {
            return client;
        }
    }

    /**
     * Factory class for creating different actions.
     */
    private class ActionFactory {
        public Action getAction(Command command) throws UnsupportedCommandTypeException {
            String user = command.getUser();
            List<String> arguments = command.getArguments();

            CommandType type = command.getCommandType();
            switch (type) {
                case REGISTER:
                    return new RegisterAction(command.getUser(),
                            command.getArguments(),
                            CommandExecutor.this.userService);
                case LOGIN:
                    return new LoginAction(command.getUser(),
                            command.getArguments(),
                            CommandExecutor.this.userService);
                case CREATE_GROUP:
                    return new CreateGroupAction(command.getUser(),
                            command.getArguments(),
                            CommandExecutor.this.userService,
                            CommandExecutor.this.groupService);
                case ADD_FRIEND:
                    return new AddFriendAction(command.getUser(),
                            command.getArguments(),
                            CommandExecutor.this.userService,
                            CommandExecutor.this.groupService);
                case SPLIT:
                    return new SplitAction(command.getUser(),
                            command.getArguments(),
                            CommandExecutor.this.groupService,
                            CommandExecutor.this.debtService);
                case SPLIT_GROUP:
                    return new SplitGroupAction(command.getUser(),
                            command.getArguments(),
                            CommandExecutor.this.groupService,
                            CommandExecutor.this.debtService);
                case GET_STATUS:
                    return new GetStatusAction(command.getUser(),
                            command.getArguments(),
                            CommandExecutor.this.debtService);
                case PAID:
                    return new PaidAction(command.getUser(),
                            command.getArguments(),
                            CommandExecutor.this.debtService);
                default:
                    String errMsg = String.format("Unsupported command type: %s", type.toString());
                    throw new UnsupportedCommandTypeException(errMsg);
            }
        }
    }

    /**
     * Adds the command to be processed in the pool.
     */
    public void execute(Command command, SocketChannel client) throws UnsupportedCommandTypeException {
        if (Objects.isNull(command)) {
            throw new IllegalArgumentException("No command passed");
        }

        logger.info("Executing command: " + command.toString());
        ActionFactory actionFactory = new ActionFactory();
        Action action = actionFactory.getAction(command);

        Future<String> actionFuture = executorService.submit(action);
        actionsQueue.add(new FutureSocketTuple(actionFuture, client)); //TODO: DO it with offer and a check
    }

    /**
     * Waits for each executed command to finish and sends the result back to the Client.
     */
    private void processQueue() {
        try {
            while (true) {
                FutureSocketTuple currentTuple = actionsQueue.take();
                Future<String> currentFuture = currentTuple.getFuture();
                SocketChannel client = currentTuple.getClient();

                try {
                    String result = currentFuture.get();

                    Server.writeMessageToChannel(result, client);
                } catch (ExecutionException e) {
                    Server.writeMessageToChannel(e.getMessage(), client);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
