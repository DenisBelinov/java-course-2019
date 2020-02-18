package bg.sofia.uni.fmi.mjt.splitwise.server.execution.action;

import bg.sofia.uni.fmi.mjt.splitwise.backend.data.Debt;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.DebtService;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GetStatusAction extends Action {
    private static final String NOT_LOGGED_IN_MESSAGE = "You need to log in to get your status.";
    private static final String YOU_OWE_TEMPLATE = "You owe %s to %s";
    private static final String OWES_YOU_TEMPLATE = "%s owes you %s";
    private static final String NO_DEBTS_MESSAGE = "You are all clear.";

    DebtService debtService;

    public GetStatusAction(String currentUser, List<String> arguments, DebtService debtService) {
        super(currentUser, arguments);
        this.debtService = debtService;
    }

    @Override
    public String call() {
        if (Objects.isNull(currentUser) || currentUser.length() == 0) {
            return NOT_LOGGED_IN_MESSAGE;
        }

        Set<Debt> debts = debtService.getDebtsForUser(currentUser);

        String message = "";

        for (Debt debt : debts) {
            Double amount = debt.getAmount();
            if (amount == 0) {
                continue;
            }

            if (currentUser.equals(debt.getFromUsername())) {
                if (amount > 0) {
                    message += String.format(YOU_OWE_TEMPLATE, amount, debt.getToUsername()) + "\n";
                } else {
                    message += String.format(OWES_YOU_TEMPLATE, debt.getToUsername(), Math.abs(amount)) + "\n";
                }
            }
            else {
                // current user is toUserName
                if (amount > 0) {
                    message += String.format(OWES_YOU_TEMPLATE, debt.getFromUsername(), amount) + "\n";
                } else {
                    message += String.format(YOU_OWE_TEMPLATE, Math.abs(amount), debt.getFromUsername()) + "\n";
                }
            }
        }

        if (message.length() == 0) {
            return NO_DEBTS_MESSAGE;
        }

        return message;
    }
}
