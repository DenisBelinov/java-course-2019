package bg.sofia.uni.fmi.mjt.splitwise.server.command;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.exception.InvalidCommandException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandParser {
    // The user will be placed first by the client and will be surrounded by <>
    // Example <denis> get-status
    private final String USER_PATTERN = "^<.*>$";

    public Command parse(String command) throws InvalidCommandException {
        String[] parts = command.split(" ");

        if (parts.length < 2) {
            throw new InvalidCommandException("Invalid empty command. Are you sure you are using the client?");
        }

        String user = parts[0];
        if (!user.matches(USER_PATTERN)) {
            throw new InvalidCommandException("Invalid command format. User is not authenticated." +
                                                        " Are you sure you are using the client?");
        }

        user = pruneUser(user);

        String cliCommand = parts[1];
        CommandType commandType = getActionFromCliCommand(cliCommand);

        List<String> arguments = parts.length > 2 ? Arrays.asList(parts).subList(2, parts.length) : new ArrayList<>();


        return new Command(user, commandType, arguments);
    }

    private CommandType getActionFromCliCommand(String cliCommand) throws InvalidCommandException {
        Optional<CommandType> actionOptional = Arrays.stream(CommandType.values())
                                                .filter(a -> cliCommand.equals(a.getCliCommand()))
                                                .findFirst();

        if (actionOptional.isEmpty()) {
            throw new InvalidCommandException("Invalid cli command: " + cliCommand);
        }

        return actionOptional.get();
    }

    /**
     * Removes the starting '<' and ending '>' from the user sent from the client.
     */
    private String pruneUser(String user) {
        return user.replace("<", "").replace(">","");
    }

}
