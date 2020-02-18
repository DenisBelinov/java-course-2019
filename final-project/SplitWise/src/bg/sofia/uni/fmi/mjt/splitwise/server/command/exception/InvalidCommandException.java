package bg.sofia.uni.fmi.mjt.splitwise.server.command.exception;

public class InvalidCommandException extends Exception {

    /**
     * Exception if a command sent from the client is in an invalid format.
     * @param message what happened
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}
