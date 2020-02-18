package bg.sofia.uni.fmi.mjt.splitwise.server.execution.action;

import bg.sofia.uni.fmi.mjt.splitwise.backend.data.Group;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.DebtService;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.GroupService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SplitGroupAction extends Action {
    private static final String INSUFFICIENT_ARGUMENTS_MESSSAGE = "Insufficient arguments provided for split-group." +
            " Please provide <group-name> <amount>.";
    private static final String NOT_LOGGED_IN_MESSAGE = "Please log in before splitting.";
    private static final String NOT_MEMBER_MESSAGE = "You are not a member of such group";
    private static final String INVALID_NUMBER_PASSED_MESSAGE = "Invalid amount passed.";

    private static final String SUCCESSFUL_SPLIT_TEMPLATE = "Successfuly split %s in group %s";

    private GroupService groupService;
    private DebtService debtService;

    public SplitGroupAction(String currentUser, List<String> arguments, GroupService groupService, DebtService debtService) {
        super(currentUser, arguments);
        this.groupService = groupService;
        this.debtService = debtService;
    }

    @Override
    public String call() throws Exception {
        if (arguments.size() < 2) {
            return INSUFFICIENT_ARGUMENTS_MESSSAGE;
        }

        if (Objects.isNull(currentUser) || currentUser.length() == 0) {
            return NOT_LOGGED_IN_MESSAGE;
        }

        String groupName = arguments.get(0);
        Double amount;
        try {
            amount = Double.valueOf(arguments.get(1));
        } catch (NumberFormatException e) {
            return INVALID_NUMBER_PASSED_MESSAGE;
        }

        Optional<Group> groupOptional  = groupService.getGroup(groupName, currentUser);
        if (groupOptional.isEmpty()) {
            return NOT_MEMBER_MESSAGE;
        }

        Group group = groupOptional.get();
        Double amountPerUser = amount / group.getMembers().size();

        for (String member : group.getMembers()) {
            if (member.equals(currentUser)) {
                continue;
            }

            debtService.addDebt(member, currentUser, amountPerUser);
        }

        return String.format(SUCCESSFUL_SPLIT_TEMPLATE, amount, groupName);
    }
}
