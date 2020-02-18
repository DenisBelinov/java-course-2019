package bg.sofia.uni.fmi.mjt.splitwise.backend.service.exception;

public class UserNotFoundException extends Exception {
    /**
     * Exception if a user that is not registered is referenced by username.
     * @param message what happened
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
