package bg.sofia.uni.fmi.mjt.splitwise.server.execution.action;

import bg.sofia.uni.fmi.mjt.splitwise.backend.data.User;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.UserService;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.exception.UsernameTakenException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LoginAction extends Action {
    private static final String INSUFFICIENT_ARGUMENTS_MESSSAGE = "Insufficient arguments provided for Login." +
            " Please provide <username> <password>.";
    private static final String USER_ALREADY_LOGGED_MESSAGE = "You are already logged in.";
    private static final String USER_NOT_FOUND_MESSAGE = "This user does not exist.";
    private static final String INVALID_PASSWORD_MESSAGE = "Invalid password.";
    private static final String SUCCESSFUL_LOGIN_TEMPLATE = "<%s> Successful login!";


    private UserService userService;

    public LoginAction(String currentUser, List<String> arguments, UserService userService) {
        super(currentUser, arguments);
        this.userService = userService;
    }

    @Override
    public String call() throws Exception {
        if (arguments.size() < 2) {
            return INSUFFICIENT_ARGUMENTS_MESSSAGE;
        }

        if (!Objects.isNull(currentUser) && currentUser.length() != 0) {
            return USER_ALREADY_LOGGED_MESSAGE;
        }

        String username = arguments.get(0);
        String password = arguments.get(1);

        Optional<User> userOptional = userService.getUser(username);

        if (userOptional.isEmpty()) {
            return USER_NOT_FOUND_MESSAGE;
        }

        User user = userOptional.get();
        if (!user.getPassword().equals(password)) {
            return INVALID_PASSWORD_MESSAGE;
        }

        return String.format(SUCCESSFUL_LOGIN_TEMPLATE, username);
    }
}
