package bg.sofia.uni.fmi.mjt.splitwise.backend.service.exception;

public class InvalidGroupSizeException extends Exception {
    /**
     * Exception if a group with one member is attempted to be registered.
     * @param message what happened
     */
    public InvalidGroupSizeException(String message) {
        super(message);
    }
}
