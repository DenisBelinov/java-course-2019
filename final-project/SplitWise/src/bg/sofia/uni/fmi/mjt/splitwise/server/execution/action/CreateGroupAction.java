package bg.sofia.uni.fmi.mjt.splitwise.server.execution.action;

import bg.sofia.uni.fmi.mjt.splitwise.backend.data.Group;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.GroupService;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.UserService;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.exception.GroupAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.exception.InvalidGroupSizeException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CreateGroupAction extends Action {
    private static final String INSUFFICIENT_ARGUMENTS_MESSSAGE = "Insufficient arguments provided for create-group." +
            " Please provide <gorup-name> <username> <username>.";
    private static final String NOT_LOGGED_IN_MESSAGE = "Please log in before creating a group.";
    private static final String GROUP_EXISTS_MESSAGE = "Group with these users and name already exists.";
    private static final String INSUFFICIENT_UNIQUE_MEMBERS_MESSAGE = "A group has to have more than 2 unique members.";
    private static final String SUCCESSFUL_GROUP_CREATION_MESSAGE = "Successfully created group!";

    private static final String UNREGISTERED_MEMBERS_TEMPLATE = "%s are not registered users. Cannot create group.";


    protected UserService userService;
    protected GroupService groupService;

    public CreateGroupAction(String currentUser,
                             List<String> arguments,
                             UserService userService,
                             GroupService groupService) {
        super(currentUser, arguments);
        this.userService = userService;
        this.groupService = groupService;
    }

    @Override
    public String call() {
        if (arguments.size() < 3) {
            return INSUFFICIENT_ARGUMENTS_MESSSAGE;
        }

        if (Objects.isNull(currentUser) || currentUser.length() == 0) {
            return NOT_LOGGED_IN_MESSAGE;
        }

        String groupName = arguments.get(0);

        List<String> members = new ArrayList<>(arguments.subList(1, arguments.size()));
        members.add(currentUser);

        List<String> unregisteredMembers = getUnregisteredMembers(members);
        if (!unregisteredMembers.isEmpty()) {
            return String.format(UNREGISTERED_MEMBERS_TEMPLATE, unregisteredMembers.toString());
        }

        try {
            groupService.registerGroup(new Group(groupName, new HashSet<String>(members)));
        } catch (GroupAlreadyExistsException e) {
            return GROUP_EXISTS_MESSAGE;
        } catch (InvalidGroupSizeException e) {
            return INSUFFICIENT_UNIQUE_MEMBERS_MESSAGE;
        }

        return SUCCESSFUL_GROUP_CREATION_MESSAGE;
    }

    protected List<String> getUnregisteredMembers(List<String> members) {
        return members.stream().filter(u -> userService.getUser(u).isEmpty()).collect(Collectors.toList());
    }
}
