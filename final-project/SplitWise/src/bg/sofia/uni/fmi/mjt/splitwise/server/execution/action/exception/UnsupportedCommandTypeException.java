package bg.sofia.uni.fmi.mjt.splitwise.server.execution.action.exception;

public class UnsupportedCommandTypeException extends Exception {
    /**
     * Exception if a the factory is given a CommandType it does not support.
     * @param message what happened
     */
    public UnsupportedCommandTypeException(String message) {
        super(message);
    }
}
