package bg.sofia.uni.fmi.mjt.splitwise.server.execution.action;

import bg.sofia.uni.fmi.mjt.splitwise.backend.data.User;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.UserService;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.exception.UsernameTakenException;

import java.util.List;

public class RegisterAction extends Action {
    private static final String INSUFFICIENT_ARGUMENTS_MESSSAGE = "Insufficient arguments provided for Register." +
            " Please provide <username> <password> <full name>.";
    private static final String USERNAME_TAKEN_MESSAGE = "Username is already taken, try with another.";
    private static final String USER_CREATED_MESSAGE = "User created successfully.";

    private UserService userService;

    public RegisterAction(String currentUser, List<String> arguments, UserService userService) {
        super(currentUser, arguments);
        this.userService = userService;
    }

    @Override
    public String call() {
        if (arguments.size() < 3) {
            return INSUFFICIENT_ARGUMENTS_MESSSAGE;
        }

        String username = arguments.get(0);
        String password = arguments.get(1);

        StringBuilder strBuilder = new StringBuilder("");
        for (String name : arguments.subList(2, arguments.size())) {
            strBuilder.append(name).append(" ");
        }
        String fullName = strBuilder.toString();

        try {
            userService.registerUser(new User(username, password, fullName));
        } catch (UsernameTakenException e) {
            return USERNAME_TAKEN_MESSAGE;
        }

        return USER_CREATED_MESSAGE;
    }
}
