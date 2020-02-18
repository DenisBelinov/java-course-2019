package bg.sofia.uni.fmi.mjt.splitwise.server.execution.action;

import bg.sofia.uni.fmi.mjt.splitwise.backend.data.Debt;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.DebtService;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.GroupService;

import java.util.List;
import java.util.Objects;

public class PaidAction extends Action {
    private static final String INSUFFICIENT_ARGUMENTS_MESSSAGE = "Insufficient arguments provided for paid." +
            " Please provide <username> <amount>.";
    private static final String NOT_LOGGED_IN_MESSAGE = "Please log in first..";
    private static final String NO_DEBT_BETWEEN_USERS_MESSAGE = "This user doesn't owe you anything.";
    private static final String INVALID_NUMBER_PASSED_MESSAGE = "Invalid amount passed.";
    private static final String SUCCESSFUL_PAYMENT_MESSGE = "Payment successful.";

    private DebtService debtService;

    public PaidAction(String currentUser, List<String> arguments, DebtService debtService) {
        super(currentUser, arguments);
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

        String toUser = arguments.get(0);
        Double amount;
        try {
            amount = Double.valueOf(arguments.get(1));
        } catch (NumberFormatException e) {
            return INVALID_NUMBER_PASSED_MESSAGE;
        }

        if (amount <= 0) {
            return INVALID_NUMBER_PASSED_MESSAGE;
        }

        Debt debt = debtService.getDebtForUsers(currentUser, toUser);
        if (debt.getAmount() == 0) {
            return NO_DEBT_BETWEEN_USERS_MESSAGE;
        }

        debtService.addDebt(currentUser, toUser, amount);
        return SUCCESSFUL_PAYMENT_MESSGE;
    }
}
