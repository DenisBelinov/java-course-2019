package bg.sofia.uni.fmi.mjt.splitwise.server.execution.action;

import bg.sofia.uni.fmi.mjt.splitwise.backend.service.DebtService;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.GroupService;

import java.util.List;
import java.util.Objects;

public class SplitAction extends Action {
    private static final String INSUFFICIENT_ARGUMENTS_MESSSAGE = "Insufficient arguments provided for split." +
            " Please provide <username> <amount>.";
    private static final String NOT_LOGGED_IN_MESSAGE = "Please log in before splitting.";
    private static final String INVALID_NUMBER_PASSED_MESSAGE = "Invalid amount passed.";
    private static final String NOT_FRIENDS_MESSAGE = "You are not friends with this user.";

    private static final String SUCCESSFUL_SPLIT_TEMPLATE = "Successfuly split %s between %s and %s.";

    private GroupService groupService;
    private DebtService debtService;

    public SplitAction(String currentUser, List<String> arguments, GroupService groupService, DebtService debtService) {
        super(currentUser, arguments);
        this.groupService = groupService;
        this.debtService = debtService;
    }

    @Override
    public String call() {
        if (arguments.size() < 2) {
            return INSUFFICIENT_ARGUMENTS_MESSSAGE;
        }

        if (Objects.isNull(currentUser) || currentUser.length() == 0) {
            return NOT_LOGGED_IN_MESSAGE;
        }

        String fromUser = arguments.get(0);
        Double amount;
        try {
            amount = Double.valueOf(arguments.get(1));
        } catch (NumberFormatException e) {
            return INVALID_NUMBER_PASSED_MESSAGE;
        }

        if (!groupService.verifyFriendship(currentUser, fromUser)) {
            return NOT_FRIENDS_MESSAGE;
        }

        debtService.addDebt(fromUser, currentUser, amount / 2);
        return String.format(SUCCESSFUL_SPLIT_TEMPLATE, amount, currentUser, fromUser);
    }
}
