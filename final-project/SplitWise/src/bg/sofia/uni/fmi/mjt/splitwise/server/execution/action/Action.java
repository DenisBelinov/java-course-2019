package bg.sofia.uni.fmi.mjt.splitwise.server.execution.action;

import java.util.List;
import java.util.concurrent.Callable;

public abstract class  Action implements Callable<String> {
    protected String currentUser;
    protected List<String> arguments;

    public Action(String currentUser, List<String> arguments) {
        this.currentUser = currentUser;
        this.arguments = arguments;
    }
}
