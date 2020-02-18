package bg.sofia.uni.fmi.mjt.splitwise.backend.service.exception;

public class UsernameTakenException extends Exception {
    /**
     * Exception if a user tries to register with a taken username.
     * @param message what happened
     */
    public UsernameTakenException(String message) {
        super(message);
    }
}
