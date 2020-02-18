package bg.sofia.uni.fmi.mjt.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.backend.data.User;
import bg.sofia.uni.fmi.mjt.splitwise.backend.persistence.MockPersistence;
import bg.sofia.uni.fmi.mjt.splitwise.backend.persistence.Persistence;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.DebtService;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.GroupService;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.UserService;
import bg.sofia.uni.fmi.mjt.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.CommandParser;
import bg.sofia.uni.fmi.mjt.splitwise.server.execution.CommandExecutor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        UserService userService = new UserService(new MockPersistence(), Path.of(""));
        GroupService groupService = new GroupService(new MockPersistence(), Path.of(""));
        DebtService debtService = new DebtService();
        CommandExecutor commandExecutor = new CommandExecutor(userService, groupService, debtService);

        Server server = new Server(new CommandParser(), commandExecutor);

        server.runServer();

        int[] parts = {1,2,3};
        var a = Arrays.asList(parts);
        System.out.println(a.getClass());
        // Unfortunately I am nowhere near completing the project yet. I will need today and tomorrow to finish it.
        // Sorry for the inconvenience and I would understand a reduced grade as a consequence.

        //TODO: Add shutdownhook for the executor service in CommandExecutor
    }
}
