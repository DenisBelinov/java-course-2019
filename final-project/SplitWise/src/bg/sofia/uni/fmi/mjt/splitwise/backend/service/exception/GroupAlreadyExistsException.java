package bg.sofia.uni.fmi.mjt.splitwise.backend.service.exception;

public class GroupAlreadyExistsException extends Exception {
    /**
     * Exception if a group with the same name and members is added.
     * @param message what happened
     */
    public GroupAlreadyExistsException(String message) {
        super(message);
    }
}
