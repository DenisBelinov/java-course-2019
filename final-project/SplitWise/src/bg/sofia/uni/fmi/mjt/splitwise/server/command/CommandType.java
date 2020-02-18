package bg.sofia.uni.fmi.mjt.splitwise.server.command;

public enum CommandType {
    REGISTER("register"),
    LOGIN("login"),
    ADD_FRIEND("add-friend"),
    CREATE_GROUP("create-group"),
    SPLIT("split"),
    SPLIT_GROUP("split-group"),
    GET_STATUS("get-status"),
    PAID("paid");

    CommandType(String cliCommand) {
        this.cliCommand = cliCommand;
    }

    private final String cliCommand;

    public String getCliCommand() {
        return cliCommand;
    }
}
