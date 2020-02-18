package bg.sofia.uni.fmi.mjt.splitwise.server.command;

import java.util.List;

public class Command {
    private String user;
    private CommandType commandType;

    private List<String> arguments;

    public String getUser() {
        return user;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public Command(String user, CommandType commandType, List<String> arguments) {
        this.user = user;
        this.commandType = commandType;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "Command{" +
                "user='" + user + '\'' +
                ", commandType=" + commandType +
                ", arguments=" + arguments +
                '}';
    }
}
